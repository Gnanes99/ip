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

        return description;
    }

    private static String validateFrom(String from)
            throws DennisException {
        if (from.isBlank()) {
            throw new DennisException(
                    "The start of an event cannot be empty.");
        }

        return from;
    }

    private static String validateTo(String to)
            throws DennisException {
        if (to.isBlank()) {
            throw new DennisException(
                    "The end of an event cannot be empty.");
        }

        return to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + from + " to: " + to + ")";
    }
}
