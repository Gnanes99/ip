import java.util.Scanner;


// reused existing code, changed banner and filename
public class Dennis {
    private static final String line = "_____________________________________________________";
    private static void printAddedTask(Task task, int taskCount) {
        System.out.println("Understood. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
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
        Task[] tasks = new Task[100];
        int taskCount = 0;

        while (scan.hasNextLine()) {
            String command = scan.nextLine();

            if (command.equals("bye")) {
                System.out.println("Bye. Looking forward to seeing you again!");
                System.out.println(line);
                break;
            }

            try {
                if (command.equals("list")) {
                    System.out.println(
                            "Here are the tasks in your list:");

                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + "." + tasks[i]);
                    }
                } else if (command.equals("mark")
                        || command.startsWith("mark ")) {
                    String number = command.substring(4).trim();

                    if (number.isEmpty()) {
                        throw new DennisException(
                                "Please enter a task number.");
                    }

                    int taskNumber;

                    try {
                        taskNumber = Integer.parseInt(number);
                    } catch (NumberFormatException e) {
                        throw new DennisException(
                                "The task number must be a number.");
                    }

                    if (taskNumber < 1 || taskNumber > taskCount) {
                        throw new DennisException(
                                "That task number does not exist.");
                    }

                    Task task = tasks[taskNumber - 1];
                    task.markAsDone();

                    System.out.println(
                            "Excellent! I've marked this task as done:");
                    System.out.println("  " + task);
                } else if (command.equals("unmark")
                        || command.startsWith("unmark ")) {
                    String number = command.substring(6).trim();

                    if (number.isEmpty()) {
                        throw new DennisException(
                                "Please enter a task number.");
                    }

                    int taskNumber;

                    try {
                        taskNumber = Integer.parseInt(number);
                    } catch (NumberFormatException e) {
                        throw new DennisException(
                                "The task number must be a number.");
                    }

                    if (taskNumber < 1 || taskNumber > taskCount) {
                        throw new DennisException(
                                "That task number does not exist.");
                    }

                    Task task = tasks[taskNumber - 1];
                    task.markAsNotDone();

                    System.out.println(
                            "Alright, I've marked this task as not done yet:");
                    System.out.println("  " + task);
                } else if (command.equals("todo")
                        || command.startsWith("todo ")) {
                    Task task =
                            new Todo(command.substring(4).trim());

                    tasks[taskCount] = task;
                    taskCount++;

                    printAddedTask(task, taskCount);
                } else if (command.equals("deadline")
                        || command.startsWith("deadline ")) {
                    int byIndex = command.indexOf(" /by ");

                    if (byIndex < 0) {
                        throw new DennisException(
                                "Use /by to specify the deadline.");
                    }

                    String description =
                            command.substring(8, byIndex).trim();
                    String by =
                            command.substring(byIndex + 5).trim();

                    Task task = new Deadline(description, by);

                    tasks[taskCount] = task;
                    taskCount++;

                    printAddedTask(task, taskCount);
                } else if (command.equals("event")
                        || command.startsWith("event ")) {
                    int fromIndex = command.indexOf(" /from ");
                    int toIndex = command.indexOf(" /to ");

                    if (fromIndex < 0 || toIndex < 0
                            || toIndex < fromIndex) {
                        throw new DennisException(
                                "Use /from and /to for an event.");
                    }

                    String description =
                            command.substring(5, fromIndex).trim();
                    String from = command.substring(
                            fromIndex + 7, toIndex).trim();
                    String to =
                            command.substring(toIndex + 5).trim();

                    Task task = new Event(description, from, to);

                    tasks[taskCount] = task;
                    taskCount++;

                    printAddedTask(task, taskCount);
                } else {
                    throw new DennisException(
                            "I'm sorry, I don't understand "
                                    + "what you are saying :(");
                }
            } catch (DennisException e) {
                System.out.println("ERROR!! " + e.getMessage());
                System.out.println(line);
            }
        }
    }
}
