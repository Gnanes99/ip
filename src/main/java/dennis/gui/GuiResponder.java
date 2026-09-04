package dennis.gui;

import dennis.DennisException;
import dennis.command.Command;
import dennis.parser.Parser;
import dennis.storage.Storage;
import dennis.task.TaskList;

/**
 * Runs one user command at a time for the GUI and returns Dennis's reply as
 * text.
 *
 * <p>This is the graphical counterpart of the read-parse-execute loop in
 * {@code Dennis}. It owns the same collaborators (a {@link Storage} for the
 * save file, a {@link TaskList} in memory, and a {@link DialogUi} that
 * captures output), but instead of looping over standard input it exposes a
 * single {@link #getResponse(String)} call for the controller to invoke once
 * per button press. The console {@code Dennis} class is not modified or
 * reused directly, so both front ends keep working independently.</p>
 */
public class GuiResponder {
    /** Persists the task list after every change. */
    private final Storage storage;

    /** The tasks for this session, loaded from disk at start-up. */
    private final TaskList tasks;

    /** Captures each command's output so it can be returned as a string. */
    private final DialogUi ui;

    /**
     * Sets up the session and loads any previously saved tasks, so the GUI
     * resumes where the last run left off (console or GUI, same save file).
     */
    public GuiResponder() {
        storage = new Storage();
        tasks = new TaskList(storage.load());
        ui = new DialogUi();
    }

    /**
     * Parses and runs one command line, returning what Dennis says in reply.
     *
     * @param input the raw text entered by the user
     * @return Dennis's reply, or an explanation if the input was invalid
     */
    public String getResponse(String input) {
        try {
            Command command = Parser.parse(input);
            command.execute(tasks, ui, storage);
        } catch (DennisException e) {
            ui.showError(e.getMessage());
        }
        return ui.drain();
    }

    /**
     * Returns the greeting shown in the window when the GUI opens.
     *
     * @return Dennis's welcome message
     */
    public String getGreeting() {
        return "Hi, my name is Dennis. It is lovely to meet you!"
                + System.lineSeparator()
                + "How may I help you today?";
    }
}
