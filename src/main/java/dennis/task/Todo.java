package dennis.task;

import dennis.DennisException;

/**
 * A task with nothing but a description &mdash; no date attached.
 */
public class Todo extends Task {
    /**
     * Creates a todo with the given description.
     *
     * @param description the task text
     * @throws DennisException if {@code description} is blank or contains the
     *                         {@code '|'} save-file separator
     */
    public Todo(String description) throws DennisException {
        super(validateDescription(description));
    }

    /**
     * Checks the description and returns it unchanged when acceptable.
     *
     * @throws DennisException if it is blank or contains a {@code '|'}
     */
    private static String validateDescription(String description)
            throws DennisException {
        if (description.isBlank()) {
            throw new DennisException(
                    "I'm sorry, todo must contain a task.");
        }

        return rejectSeparator(description, "A task description");
    }

    @Override
    public String toFileFormat() {
        return "T | " + getStatusNumber() + " | " + description;
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
