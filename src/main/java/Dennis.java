import java.time.LocalDate;



// reused existing code, changed banner and filename
public class Dennis {
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        // Level 7: reload any previously saved tasks on start-up, then save
        // back to ./data/dennis.txt after every change to the list.
        Storage storage = new Storage();
        TaskList tasks = new TaskList(storage.load());

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            CommandType commandType = Parser.parseCommandType(command);

            try {
                switch (commandType) {
                    case BYE:
                        Parser.requireBareCommand(command, "bye");

                        ui.showGoodbye();
                        ui.showLine();
                        return;

                    case LIST:
                        Parser.requireBareCommand(command, "list");

                        ui.showMessage("Here are the tasks in your list:");

                        for (int i = 0; i < tasks.size(); i++) {
                            ui.showMessage((i + 1) + "." + tasks.get(i));
                        }
                        break;

                    case MARK:
                        int markNumber = Parser.parseTaskNumber(
                                command, "mark", tasks.size());

                        Task markedTask = tasks.get(markNumber - 1);
                        markedTask.markAsDone();
                        storage.save(tasks.asList());

                        ui.showMarkedTask(markedTask);
                        break;

                    case UNMARK:
                        int unmarkNumber = Parser.parseTaskNumber(
                                command, "unmark", tasks.size());

                        Task unmarkedTask = tasks.get(unmarkNumber - 1);
                        unmarkedTask.markAsNotDone();
                        storage.save(tasks.asList());

                        ui.showUnmarkedTask(unmarkedTask);
                        break;

                    case DELETE:
                        int deleteNumber = Parser.parseTaskNumber(
                                command, "delete", tasks.size());

                        Task removedTask =
                                tasks.remove(deleteNumber - 1);
                        storage.save(tasks.asList());

                        ui.showRemovedTask(removedTask, tasks.size());
                        break;

                    case TODO:
                        Task todo = new Todo(Parser.parseTodo(command));

                        tasks.add(todo);
                        storage.save(tasks.asList());
                        ui.showAddedTask(todo, tasks.size());
                        break;

                    case DEADLINE:
                        Parser.DeadlineParts deadlineParts =
                                Parser.parseDeadline(command);

                        Task deadline = new Deadline(
                                deadlineParts.description(),
                                deadlineParts.by());

                        tasks.add(deadline);
                        storage.save(tasks.asList());
                        ui.showAddedTask(deadline, tasks.size());
                        break;

                    case EVENT:
                        Parser.EventParts eventParts =
                                Parser.parseEvent(command);

                        Task event = new Event(
                                eventParts.description(),
                                eventParts.from(),
                                eventParts.to());

                        tasks.add(event);
                        storage.save(tasks.asList());
                        ui.showAddedTask(event, tasks.size());
                        break;

                    case ON:
                        LocalDate onDate = Parser.parseOnDate(command);

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
