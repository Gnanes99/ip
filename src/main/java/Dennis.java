import java.util.Scanner;
import java.util.ArrayList;


// reused existing code, changed banner and filename
public class Dennis {
    private static final String line = "_____________________________________________________";
    private static void printAddedTask(Task task, int taskCount) {
        System.out.println("Understood. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

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
        String banner = " ____                   _     \n"
                + "|  _ \\  ___ _ __  _ __ (_)___ \n"
                + "| | | |/ _ \\ '_ \\| '_ \\| / __|\n"
                + "| |_| |  __/ | | | | | | \\__ \\\n"
                + "|____/ \\___|_| |_|_| |_|_|___/\n";
        System.out.println(banner);
        System.out.println("Hi, my name is Dennis. It is lovely to meet you!");
        System.out.println("How may I help you today?");
        System.out.println(line);

        Scanner scan = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        while (scan.hasNextLine()) {
            String command = scan.nextLine();
            CommandType commandType = CommandType.from(command);

            try {
                switch (commandType) {
                    case BYE:
                        if (!command.equals("bye")) {
                            throw new DennisException(
                                    "I'm sorry, I don't understand "
                                            + "what you are trying to say :(");
                        }

                        System.out.println(
                                "Bye. Looking forward to seeing you again!");
                        System.out.println(line);
                        return;

                    case LIST:
                        if (!command.equals("list")) {
                            throw new DennisException(
                                    "I'm sorry, I don't understand "
                                            + "what you are trying to say :(");
                        }

                        System.out.println(
                                "Here are the tasks in your list:");

                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println(
                                    (i + 1) + "." + tasks.get(i));
                        }
                        break;

                    case MARK:
                        int markNumber = parseTaskNumber(
                                command, "mark".length(), tasks.size());

                        Task markedTask = tasks.get(markNumber - 1);
                        markedTask.markAsDone();

                        System.out.println(
                                "Excellent! I've marked this task as done:");
                        System.out.println("  " + markedTask);
                        break;

                    case UNMARK:
                        int unmarkNumber = parseTaskNumber(
                                command, "unmark".length(), tasks.size());

                        Task unmarkedTask = tasks.get(unmarkNumber - 1);
                        unmarkedTask.markAsNotDone();

                        System.out.println(
                                "Alright, I've marked this task as not done yet:");
                        System.out.println("  " + unmarkedTask);
                        break;

                    case DELETE:
                        int deleteNumber = parseTaskNumber(
                                command, "delete".length(), tasks.size());

                        Task removedTask =
                                tasks.remove(deleteNumber - 1);

                        System.out.println(
                                "Understood. I've removed this task:");
                        System.out.println("  " + removedTask);
                        System.out.println(
                                "Now there are " + tasks.size()
                                        + " tasks in the list.");
                        break;

                    case TODO:
                        Task todo = new Todo(
                                command.substring("todo".length()).trim());

                        tasks.add(todo);
                        printAddedTask(todo, tasks.size());
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
                        printAddedTask(deadline, tasks.size());
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
                        printAddedTask(event, tasks.size());
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
                System.out.println("ERROR!! " + e.getMessage());
                System.out.println(line);
            }
        }
    }
}
