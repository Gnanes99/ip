import java.time.LocalDate;

public class Event extends Task {
    /** Start date of the event. Stored as a real date, not free text. */
    private final LocalDate from;
    /** End date of the event. */
    private final LocalDate to;

    public Event(String description, String from, String to)
            throws DennisException {
        super(validateDescription(description));
        this.from = validateFrom(from);
        this.to = validateTo(to);
    }

    private static String validateDescription(String description)
            throws DennisException {
        if (description.isBlank()) {
            throw new DennisException(
                    "I'm sorry, event must contain a task.");
        }

        return rejectSeparator(description, "A task description");
    }

    private static LocalDate validateFrom(String from)
            throws DennisException {
        if (from.isBlank()) {
            throw new DennisException(
                    "The start of an event cannot be empty.");
        }

        return parseDate(from, "An event start");
    }

    private static LocalDate validateTo(String to)
            throws DennisException {
        if (to.isBlank()) {
            throw new DennisException(
                    "The end of an event cannot be empty.");
        }

        return parseDate(to, "An event end");
    }

    @Override
    public String toFileFormat() {
        // from/to are written as ISO yyyy-MM-dd, the form parseDate accepts.
        return "E | " + getStatusNumber() + " | " + description
                + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + formatDate(from) + " to: " + formatDate(to) + ")";
    }
}
