import java.util.Scanner;
import java.util.ArrayList;


// reused existing code, changed banner and filename
public class Dennis {
    private static final String line = "_____________________________________________________";

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

                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + task);
            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7).trim());
                Task task = tasks[taskNumber - 1];
                task.markAsNotDone();

                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + task);
            } else {
                tasks[taskCount] = new Task(command);
                taskCount++;
                System.out.println("added: " + command);
            }
        }

    }
}
