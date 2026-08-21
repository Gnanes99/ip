/**
 * Represents a task and whether it has been completed.
 */
public class Task {
    private enum Type {
        TODO("T"),
        DEADLINE("D"),
        EVENT("E");

        private final String symbol;

        Type(String symbol) {
            this.symbol = symbol;
        }
    }

    private final String description;
    private final Type type;
    private final String from;
    private final String to;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description description of the task
     */
    public Task(String description) {
        this(description, Type.TODO, null, null);
    }

    private Task(String description, Type type, String from, String to) {
        this.description = description;
        this.type = type;
        this.from = from;
        this.to = to;
        this.isDone = false;
    }

    /**
     * Creates a deadline task that must be completed by the given date or time.
     *
     * @param description description of the task
     * @param by deadline supplied by the user
     * @return a new incomplete deadline task
     */
    public static Task deadline(String description, String by) {
        return new Task(description, Type.DEADLINE, null, by);
    }

    /**
     * Creates an event task with the given start and end times.
     *
     * @param description description of the event
     * @param from start date or time supplied by the user
     * @param to end date or time supplied by the user
     * @return a new incomplete event task
     */
    public static Task event(String description, String from, String to) {
        return new Task(description, Type.EVENT, from, to);
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the symbol used to display the task's status.
     *
     * @return {@code "X"} if completed, or a space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the task in its display format.
     *
     * @return status icon followed by the task description
     */
    @Override
    public String toString() {
        String task = "[" + type.symbol + "][" + getStatusIcon() + "] " + description;
        if (type == Type.DEADLINE) {
            return task + " (by: " + to + ")";
        }
        if (type == Type.EVENT) {
            return task + " (from: " + from + " to: " + to + ")";
        }
        return task;
    }
}
