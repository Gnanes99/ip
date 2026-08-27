public abstract class Task {
    protected final String description;
    private boolean isDone;

    protected Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the digit used in the save file to record completion status.
     *
     * @return "1" if the task is done, "0" otherwise
     */
    protected String getStatusNumber() {
        return isDone ? "1" : "0";
    }

    /**
     * Encodes this task as a single line for the save file. Each subclass
     * supplies its own type tag ("T", "D", "E") and fields, separated by
     * {@link #SAVE_SEPARATOR}. Example: {@code T | 1 | read book}.
     *
     * @return the save-file representation of this task
     */
    public abstract String toFileFormat();

    /** Field separator used in the save-file format (see {@link #toFileFormat()}). */
    public static final String SAVE_SEPARATOR = " | ";

    /**
     * Rejects field text that would clash with the save-file format. Because
     * fields are separated by "{@code  | }", a "{@code |}" inside a description
     * or date/time would make the saved line impossible to read back
     * unambiguously. Forbidding the character keeps the format simple; the
     * alternative would be an escaping scheme in {@code toFileFormat} and the
     * loader.
     *
     * @param value     user-supplied text for one field
     * @param fieldName  human-readable field name, used in the error message
     * @return {@code value} unchanged when it is safe to store
     * @throws DennisException if {@code value} contains a "{@code |}" character
     */
    protected static String rejectSeparator(String value, String fieldName)
            throws DennisException {
        if (value.contains("|")) {
            throw new DennisException(
                    fieldName + " cannot contain the '|' character.");
        }

        return value;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
