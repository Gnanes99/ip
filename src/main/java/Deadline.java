import java.time.LocalDate;

public class Deadline extends Task {
    /** The date the task is due. Stored as a real date, not free text. */
    private final LocalDate by;

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

        return rejectSeparator(description, "A task description");
    }

    private static LocalDate validateBy(String by)
            throws DennisException {
        if (by.isBlank()) {
            throw new DennisException(
                    "deadline must contain a date and time.");
        }

        // parseDate enforces the yyyy-MM-dd format; a LocalDate can never
        // contain the save-file separator, so rejectSeparator is not needed.
        return parseDate(by, "A deadline date");
    }

    @Override
    public String toFileFormat() {
        // by.toString() is ISO yyyy-MM-dd, the same form parseDate accepts.
        return "D | " + getStatusNumber() + " | " + description + " | " + by;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return by.equals(date);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + formatDate(by) + ")";
    }
}
