package dennis.gui;

import dennis.task.Task;
import dennis.ui.Ui;

/**
 * A {@link Ui} that collects Dennis's replies into a string instead of
 * printing them, so a graphical front end can show them in a dialog bubble.
 *
 * <p>Every {@code show*} method the commands call is overridden to append to
 * an internal buffer. {@link #drain()} returns the text built up since the
 * last call and clears the buffer, ready for the next command. The console
 * {@link Ui} is left completely untouched; this class only adds a second way
 * to receive its output.</p>
 */
public class DialogUi extends Ui {
    /** Collects the lines of the reply currently being built. */
    private final StringBuilder buffer = new StringBuilder();

    /** Creates a capturing UI with an empty buffer. */
    public DialogUi() {
    }

    /**
     * Returns everything appended since the last call and clears the buffer.
     *
     * @return the accumulated reply text, with no trailing line separator
     */
    public String drain() {
        String text = buffer.toString();
        buffer.setLength(0);
        return text;
    }

    /**
     * Appends one line to the reply buffer, inserting a line separator
     * before it unless it is the first line.
     *
     * @param line the text to add
     */
    private void append(String line) {
        if (buffer.length() > 0) {
            buffer.append(System.lineSeparator());
        }
        buffer.append(line);
    }

    @Override
    public void showMessage(String message) {
        append(message);
    }

    @Override
    public void showLine() {
        // The console divider rule has no place in a chat bubble.
    }

    @Override
    public void showError(String message) {
        append("ERROR!! " + message);
    }

    @Override
    public void showGoodbye() {
        append("Bye. Looking forward to seeing you again!");
    }

    @Override
    public void showAddedTask(Task task, int taskCount) {
        append("Understood. I've added this task:");
        append("  " + task);
        append("Now you have " + taskCount + " tasks in the list.");
    }

    @Override
    public void showRemovedTask(Task task, int taskCount) {
        append("Understood. I've removed this task:");
        append("  " + task);
        append("Now there are " + taskCount + " tasks in the list.");
    }

    @Override
    public void showMarkedTask(Task task) {
        append("Excellent! I've marked this task as done:");
        append("  " + task);
    }

    @Override
    public void showUnmarkedTask(Task task) {
        append("Alright, I've marked this task as not done yet:");
        append("  " + task);
    }
}
