public class Deadline extends Task {
    private final String by;

    public Deadline(String description, String by)
            throws DennisException {
        super(validateDescription(description));
        this.by = validateBy(by);
    }

    private static String validateDescription(String description)
            throws DennisException {
        if (description.isBlank()) {
            throw new DennisException(
                    "I'm sorry, deadline must contain a task.");
        }

        return description;
    }

    private static String validateBy(String by)
            throws DennisException {
        if (by.isBlank()) {
            throw new DennisException(
                    "deadline must contain a date and time.");
        }

        return by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
