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

            if (command.equals("list")) {
                System.out.println("Here are the tasks in your list:");

                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5).trim());
                Task task = tasks[taskNumber - 1];
                task.markAsDone();

                System.out.println("Excellent! I've marked this task as done:");
                System.out.println("  " + task);
            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7).trim());
                Task task = tasks[taskNumber - 1];
                task.markAsNotDone();

                System.out.println("Alright, I've marked this task as not done yet:");
                System.out.println("  " + task);
            } else if (command.startsWith("todo ")) {
                Task task = new Todo(command.substring(5).trim());

                tasks[taskCount] = task;
                taskCount++;

                printAddedTask(task, taskCount);
            } else if (command.startsWith("deadline ")) {
                int byIndex = command.indexOf(" /by ");

                String description = command.substring(9, byIndex).trim();
                String by = command.substring(byIndex + 5).trim();

                Task task = new Deadline(description, by);

                tasks[taskCount] = task;
                taskCount++;

                printAddedTask(task, taskCount);
            } else if (command.startsWith("event ")) {
                int fromIndex = command.indexOf(" /from ");
                int toIndex = command.indexOf(" /to ", fromIndex + 7);

                String description = command.substring(6, fromIndex).trim();
                String from = command.substring(fromIndex + 7, toIndex).trim();
                String to = command.substring(toIndex + 5).trim();

                Task task = new Event(description, from, to);

                tasks[taskCount] = task;
                taskCount++;

                printAddedTask(task, taskCount);
            }
        }
    }
}
