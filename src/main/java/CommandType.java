/**
 * Represents a command supported by the Dennis application.
 */
public enum CommandType {
    BYE,
    LIST,
    MARK,
    UNMARK,
    DELETE,
    TODO,
    DEADLINE,
    EVENT,
    ON,
    UNKNOWN;

    /**
     * Determines the command type from a line of user input.
     *
     * @param input user input to examine
     * @return the matching command type, or UNKNOWN
     */
    public static CommandType from(String input) {
        if (input == null || input.isBlank()) {
            return UNKNOWN;
        }

        String commandWord = input.trim().split("\\s+", 2)[0];

        return switch (commandWord) {
            case "bye" -> BYE;
            case "list" -> LIST;
            case "mark" -> MARK;
            case "unmark" -> UNMARK;
            case "delete" -> DELETE;
            case "todo" -> TODO;
            case "deadline" -> DEADLINE;
            case "event" -> EVENT;
            case "on" -> ON;
            default -> UNKNOWN;
        };
    }
}
