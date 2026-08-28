import java.time.LocalDate;

/**
 * Makes sense of a raw command line typed by the user: works out which
 * command it is and pulls out the pieces that command needs (task number,
 * description, dates). Anything malformed is reported as a
 * {@link DennisException} carrying a message meant for the user.
 *
 * <p>Design note: keeping this apart from {@code Dennis} lets the command
 * loop read as "what to do", while the fiddly string handling ("where does
 * the description end", "which token is the date") lives in one place. The
 * task classes still validate their own content (for example rejecting an
 * empty description), so this class only splits the input; it does not
 * judge whether the pieces make a sensible task.</p>
 *
 * <p>Every method is {@code static}: parsing here needs no state, so there
 * is nothing to gain from creating a {@code Parser} object.</p>
 */
public final class Parser {

    /** Text that marks the due date in a {@code deadline} command. */
    private static final String BY_MARKER = " /by ";

    /** Text that marks the start time in an {@code event} command. */
    private static final String FROM_MARKER = " /from ";

    /** Text that marks the end time in an {@code event} command. */
    private static final String TO_MARKER = " /to ";

    /** Shown whenever the input cannot be understood at all. */
    private static final String DONT_UNDERSTAND =
            "I'm sorry, I don't understand what you are trying to say :(";

    /** The two pieces of a {@code deadline} command. */
    public record DeadlineParts(String description, String by) {
    }

    /** The three pieces of an {@code event} command. */
    public record EventParts(String description, String from, String to) {
    }

    /** Prevents instantiation; this is a utility class. */
    private Parser() {
    }

    /**
     * Classifies a command line by its first word.
     *
     * @param input the raw line
     * @return the matching {@link CommandType}, or {@code UNKNOWN}
     */
    public static CommandType parseCommandType(String input) {
        return CommandType.from(input);
    }

    /**
     * Checks that a keyword-only command (such as {@code bye} or
     * {@code list}) was typed on its own, with no trailing text.
     *
     * @param input   the raw line
     * @param keyword the exact word the command must equal
     * @throws DennisException if {@code input} is not exactly {@code keyword}
     */
    public static void requireBareCommand(String input, String keyword)
            throws DennisException {
        if (!input.equals(keyword)) {
            throw new DennisException(DONT_UNDERSTAND);
        }
    }

    /**
     * Reads and validates the task-number argument of a
     * {@code mark}/{@code unmark}/{@code delete} command.
     *
     * @param input     the raw line, e.g. {@code "delete 2"}
     * @param keyword   the command word, e.g. {@code "delete"}
     * @param taskCount how many tasks exist (the upper bound)
     * @return the task number as typed, guaranteed to be in
     *         {@code 1..taskCount}
     * @throws DennisException if the argument is missing, not an integer,
     *                         or out of range
     */
    public static int parseTaskNumber(String input, String keyword,
            int taskCount) throws DennisException {
        String number = input.substring(keyword.length()).trim();

        if (number.isEmpty()) {
            throw new DennisException("Please enter a task number.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new DennisException("The task number must be an integer.");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new DennisException("That task number exceeds the tasks.");
        }

        return taskNumber;
    }

    /**
     * Extracts the description from a {@code todo} command. The text is
     * returned as-is (it may be empty); {@link Todo} decides whether it is
     * acceptable.
     *
     * @param input the raw line, e.g. {@code "todo read book"}
     * @return the text after {@code todo}, trimmed
     */
    public static String parseTodo(String input) {
        return input.substring("todo".length()).trim();
    }

    /**
     * Splits a {@code deadline} command into its description and due-date
     * text around the {@code /by} marker.
     *
     * @param input the raw line, e.g.
     *              {@code "deadline return book /by 2019-12-01"}
     * @return the description and the raw {@code by} text
     * @throws DennisException if the {@code /by} marker is missing
     */
    public static DeadlineParts parseDeadline(String input)
            throws DennisException {
        int byIndex = input.indexOf(BY_MARKER);
        if (byIndex < 0) {
            throw new DennisException("Use /by to specify the deadline.");
        }

        String description =
                input.substring("deadline".length(), byIndex).trim();
        String by = input.substring(byIndex + BY_MARKER.length()).trim();
        return new DeadlineParts(description, by);
    }

    /**
     * Splits an {@code event} command into its description, start text and
     * end text around the {@code /from} and {@code /to} markers.
     *
     * @param input the raw line, e.g.
     *              {@code "event meeting /from 2019-12-02 /to 2019-12-05"}
     * @return the description and the raw {@code from} and {@code to} text
     * @throws DennisException if {@code /from} or {@code /to} is missing,
     *                         or they appear in the wrong order
     */
    public static EventParts parseEvent(String input) throws DennisException {
        int fromIndex = input.indexOf(FROM_MARKER);
        int toIndex = input.indexOf(TO_MARKER);

        if (fromIndex < 0 || toIndex < 0 || toIndex < fromIndex) {
            throw new DennisException("Use /from and /to to specify "
                    + "the duration of the event.");
        }

        String description =
                input.substring("event".length(), fromIndex).trim();
        String from = input.substring(
                fromIndex + FROM_MARKER.length(), toIndex).trim();
        String to = input.substring(toIndex + TO_MARKER.length()).trim();
        return new EventParts(description, from, to);
    }

    /**
     * Reads the date argument of an {@code on} command and parses it.
     *
     * @param input the raw line, e.g. {@code "on 2019-12-01"}
     * @return the parsed date
     * @throws DennisException if no date was given, or it is not a valid
     *                         {@code yyyy-MM-dd} date
     */
    public static LocalDate parseOnDate(String input) throws DennisException {
        String date = input.substring("on".length()).trim();
        if (date.isEmpty()) {
            throw new DennisException(
                    "Please enter a date, e.g. on 2019-12-01.");
        }
        return Task.parseDate(date, "The date");
    }
}
