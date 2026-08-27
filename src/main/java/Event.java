public class Event extends Task {
    private final String from;
    private final String to;

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

    private static String validateFrom(String from)
            throws DennisException {
        if (from.isBlank()) {
            throw new DennisException(
                    "The start of an event cannot be empty.");
        }

        return rejectSeparator(from, "An event start");
    }

    private static String validateTo(String to)
            throws DennisException {
        if (to.isBlank()) {
            throw new DennisException(
                    "The end of an event cannot be empty.");
        }

        return rejectSeparator(to, "An event end");
    }

    @Override
    public String toFileFormat() {
        // "from" and "to" are kept as separate fields so they can be read
        // back individually in a later increment.
        return "E | " + getStatusNumber() + " | " + description
                + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + from + " to: " + to + ")";
    }
}
