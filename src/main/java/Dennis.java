import java.time.LocalDate;



// reused existing code, changed banner and filename
public class Dennis {
    private static int parseTaskNumber(
            String command, int commandLength, int taskCount)
            throws DennisException {
        String number = command.substring(commandLength).trim();

        if (number.isEmpty()) {
            throw new DennisException(
                    "Please enter a task number.");
        }

        int taskNumber;

        try {
            taskNumber = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new DennisException(
                    "The task number must be an integer.");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new DennisException(
                    "That task number exceeds the tasks.");
        }

        return taskNumber;
    }

    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        // Level 7: reload any previously saved tasks on start-up, then save
        // back to ./data/dennis.txt after every change to the list.
        Storage storage = new Storage();
        TaskList tasks = new TaskList(storage.load());

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            CommandType commandType = CommandType.from(command);

            try {
                switch (commandType) {
                    case BYE:
                        if (!command.equals("bye")) {
                            throw new DennisException(
                                    "I'm sorry, I don't understand "
                                            + "what you are trying to say :(");
                        }

                        ui.showGoodbye();
                        ui.showLine();
                        return;

                    case LIST:
                        if (!command.equals("list")) {
                            throw new DennisException(
                                    "I'm sorry, I don't understand "
                                            + "what you are trying to say :(");
                        }

                        ui.showMessage("Here are the tasks in your list:");

                        for (int i = 0; i < tasks.size(); i++) {
                            ui.showMessage((i + 1) + "." + tasks.get(i));
                        }
                        break;

                    case MARK:
                        int markNumber = parseTaskNumber(
                                command, "mark".length(), tasks.size());

                        Task markedTask = tasks.get(markNumber - 1);
                        markedTask.markAsDone();
                        storage.save(tasks.asList());

                        ui.showMarkedTask(markedTask);
                        break;

                    case UNMARK:
                        int unmarkNumber = parseTaskNumber(
                                command, "unmark".length(), tasks.size());

                        Task unmarkedTask = tasks.get(unmarkNumber - 1);
                        unmarkedTask.markAsNotDone();
                        storage.save(tasks.asList());

                        ui.showUnmarkedTask(unmarkedTask);
                        break;

                    case DELETE:
                        int deleteNumber = parseTaskNumber(
                                command, "delete".length(), tasks.size());

                        Task removedTask =
                                tasks.remove(deleteNumber - 1);
                        storage.save(tasks.asList());

                        ui.showRemovedTask(removedTask, tasks.size());
                        break;

                    case TODO:
                        Task todo = new Todo(
                                command.substring("todo".length()).trim());

                        tasks.add(todo);
                        storage.save(tasks.asList());
                        ui.showAddedTask(todo, tasks.size());
                        break;

                    case DEADLINE:
                        int byIndex = command.indexOf(" /by ");

                        if (byIndex < 0) {
                            throw new DennisException(
                                    "Use /by to specify the deadline.");
                        }

                        String deadlineDescription = command.substring(
                                "deadline".length(), byIndex).trim();
                        String by = command.substring(byIndex + 5).trim();

                        Task deadline =
                                new Deadline(deadlineDescription, by);

                        tasks.add(deadline);
                        storage.save(tasks.asList());
                        ui.showAddedTask(deadline, tasks.size());
                        break;

                    case EVENT:
                        int fromIndex = command.indexOf(" /from ");
                        int toIndex = command.indexOf(" /to ");

                        if (fromIndex < 0
                                || toIndex < 0
                                || toIndex < fromIndex) {
                            throw new DennisException(
                                    "Use /from and /to to specify "
                                            + "the duration of the event.");
                        }

                        String eventDescription = command.substring(
                                "event".length(), fromIndex).trim();
                        String from = command.substring(
                                fromIndex + 7, toIndex).trim();
                        String to =
                                command.substring(toIndex + 5).trim();

                        Task event =
                                new Event(eventDescription, from, to);

                        tasks.add(event);
                        storage.save(tasks.asList());
                        ui.showAddedTask(event, tasks.size());
                        break;

                    case ON:
                        String onArg =
                                command.substring("on".length()).trim();

                        if (onArg.isEmpty()) {
                            throw new DennisException(
                                    "Please enter a date, e.g. on 2019-12-01.");
                        }

                        LocalDate onDate =
                                Task.parseDate(onArg, "The date");

                        ui.showMessage("Here are the tasks on "
                                + Task.formatDate(onDate) + ":");

                        // Show each match at its real position in the list so
                        // the number still works with mark/unmark/delete; this
                        // means the numbers can skip over non-matching tasks.
                        boolean anyOnDate = false;
                        for (int i = 0; i < tasks.size(); i++) {
                            Task task = tasks.get(i);
                            if (task.occursOn(onDate)) {
                                anyOnDate = true;
                                ui.showMessage((i + 1) + "." + task);
                            }
                        }

                        if (!anyOnDate) {
                            ui.showMessage(
                                    "You have no deadlines or events "
                                            + "on that date.");
                        }
                        break;

                    case UNKNOWN:
                        throw new DennisException(
                                "I'm sorry, I don't understand "
                                        + "what you are trying to say :(");

                    default:
                        throw new AssertionError(
                                "Unhandled command type: " + commandType);
                }
            } catch (DennisException e) {
                ui.showError(e.getMessage());
                ui.showLine();
            }
        }
    }
}
