public class Todo extends Task {
    public Todo(String description) throws DennisException {
        super(validateDescription(description));
    }

    private static String validateDescription(String description)
            throws DennisException {
        if (description.isBlank()) {
            throw new DennisException(
                    "I'm sorry, todo must contain a task.");
        }

        return description;
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
