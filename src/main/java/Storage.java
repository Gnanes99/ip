import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves the task list to disk and loads it back so that tasks persist
 * between runs of the chatbot.
 *
 * <p>Design notes:</p>
 * <ul>
 *   <li><b>Location.</b> The file is {@code ./data/dennis.txt}, built from a
 *       relative path with {@link Path#of(String, String...)} so it works on
 *       any operating system and any machine. An absolute path such as
 *       {@code C:\data} would break when the project is moved.</li>
 *   <li><b>Missing file or folder.</b> On the first run the {@code data}
 *       folder and the file do not exist. {@link #save} creates the folder;
 *       {@link #load} simply returns an empty list.</li>
 *   <li><b>Corrupted file.</b> {@link #load} never lets a bad file stop
 *       start-up: unreadable lines are skipped, each with a note on the error
 *       stream, and the readable tasks are still loaded. The next change to
 *       the list rewrites the file in the correct format.</li>
 *   <li><b>I/O failures.</b> If the file cannot be read or written (no
 *       permission, disk full, path is a folder, ...), the error is reported
 *       on the error stream and the chatbot keeps running with whatever is in
 *       memory.</li>
 * </ul>
 */
public class Storage {
    /** Number of {@code " | "}-separated fields expected for each task type. */
    private static final int TODO_FIELDS = 3;
    private static final int DEADLINE_FIELDS = 4;
    private static final int EVENT_FIELDS = 5;

    /** Location of the save file (relative, OS-independent). */
    private final Path filePath;

    /**
     * Creates a storage that reads and writes {@code ./data/dennis.txt},
     * relative to the directory the program is started from.
     */
    public Storage() {
        this.filePath = Path.of("data", "dennis.txt");
    }

    /**
     * Writes every task to the save file, one encoded task per line,
     * replacing any previous contents. The parent {@code data} folder is
     * created first if it does not exist yet.
     *
     * <p>Any I/O problem is reported on the error stream and swallowed: the
     * in-memory task list is already updated, so the chatbot stays usable
     * even if this particular save did not reach the disk.</p>
     *
     * @param tasks the current task list to persist; a {@code null} list is
     *              treated as "nothing to do"
     */
    public void save(List<Task> tasks) {
        if (tasks == null) {
            return;
        }

        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(task.toFileFormat());
            }

            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Warning: could not save tasks to " + filePath
                    + " (" + e.getMessage() + "). "
                    + "Your latest change is kept in memory only.");
        }
    }

    /**
     * Reads the saved tasks back from the file, one task per line.
     *
     * @return the tasks loaded from disk; an empty list if the file is
     *         missing, empty, unreadable, or entirely corrupted
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();

        // Missing file is the normal "first run" case: start with no tasks.
        if (!Files.exists(filePath)) {
            return tasks;
        }

        // The path exists but is a folder, a broken link, or otherwise not a
        // plain readable file.
        if (!Files.isRegularFile(filePath)) {
            System.err.println("Warning: " + filePath + " is not a readable "
                    + "file; starting with an empty task list.");
            return tasks;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            System.err.println("Warning: " + filePath + " is not valid UTF-8 "
                    + "text; starting with an empty task list.");
            return tasks;
        } catch (IOException e) {
            System.err.println("Warning: could not read " + filePath + " ("
                    + e.getMessage() + "); starting with an empty task list.");
            return tasks;
        }

        int skipped = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            // Strip a UTF-8 byte-order mark that some editors add to line 1.
            if (i == 0 && !line.isEmpty() && line.charAt(0) == '\uFEFF') {
                line = line.substring(1);
            }

            if (line.isBlank()) {
                continue;
            }

            try {
                tasks.add(parseTask(line));
            } catch (DennisException e) {
                skipped++;
                System.err.println("Skipping unreadable line " + (i + 1)
                        + " in " + filePath + ": " + e.getMessage());
            }
        }

        if (skipped > 0) {
            System.err.println("Loaded " + tasks.size() + " task(s) from "
                    + filePath + "; " + skipped + " line(s) were ignored.");
        }

        return tasks;
    }

    /**
     * Turns one saved line back into a {@link Task}. The accepted format is
     * exactly what {@link Task#toFileFormat()} produces: fields separated by
     * {@link Task#SAVE_SEPARATOR}, a type tag of {@code T}, {@code D} or
     * {@code E}, and a done flag of {@code 0} or {@code 1}. Example:
     * {@code D | 0 | return book | Sunday}.
     *
     * @param line a single non-blank line from the save file
     * @return the reconstructed task
     * @throws DennisException if the line does not match the expected format
     */
    private Task parseTask(String line) throws DennisException {
        // limit -1 keeps trailing empty fields, so "T | 0 | " is seen as a
        // blank description rather than silently losing the field.
        String[] parts = line.split(" \\| ", -1);

        if (parts.length < TODO_FIELDS) {
            throw new DennisException("expected at least " + TODO_FIELDS
                    + " fields but found " + parts.length + " in \"" + line + "\"");
        }

        String type = parts[0].trim();
        boolean isDone = parseDoneFlag(parts[1].trim(), line);
        String description = parts[2].trim();

        Task task;
        switch (type) {
            case "T":
                requireExactFields(parts, TODO_FIELDS, line);
                task = new Todo(description);
                break;
            case "D":
                requireExactFields(parts, DEADLINE_FIELDS, line);
                task = new Deadline(description, parts[3].trim());
                break;
            case "E":
                requireExactFields(parts, EVENT_FIELDS, line);
                task = new Event(description, parts[3].trim(), parts[4].trim());
                break;
            default:
                throw new DennisException("unknown task type \"" + type
                        + "\" (expected T, D or E) in \"" + line + "\"");
        }

        if (isDone) {
            task.markAsDone();
        }

        return task;
    }

    /**
     * Parses the completion flag, which must be exactly {@code 0} or {@code 1}.
     *
     * @param flag the trimmed second field
     * @param line the whole line, for the error message
     * @return {@code true} for {@code 1}, {@code false} for {@code 0}
     * @throws DennisException if the flag is anything else
     */
    private static boolean parseDoneFlag(String flag, String line)
            throws DennisException {
        if (flag.equals("1")) {
            return true;
        }
        if (flag.equals("0")) {
            return false;
        }
        throw new DennisException("the done flag must be 0 or 1 but was \""
                + flag + "\" in \"" + line + "\"");
    }

    /**
     * Checks that a line split into exactly {@code expected} fields.
     *
     * @param parts    the fields produced by splitting the line
     * @param expected the number of fields this task type must have
     * @param line     the whole line, for the error message
     * @throws DennisException if the field count does not match
     */
    private static void requireExactFields(String[] parts, int expected,
            String line) throws DennisException {
        if (parts.length != expected) {
            throw new DennisException("expected " + expected
                    + " fields but found " + parts.length + " in \"" + line + "\"");
        }
    }
}
