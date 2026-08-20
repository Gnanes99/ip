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
        String[] tasks = new String[100];
        int taskCount = 0;

        while (scan.hasNextLine()) {
            String command = scan.nextLine();

            if (command.equals("bye")) {
                System.out.println("Bye. Looking forward to seeing you again!");
                System.out.println(line);
                break;
            }

            if (command.equals("list")) {
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else {
                    tasks[taskCount] = command;
                    taskCount++;
                    System.out.println("added: " + command);
                }
            System.out.println(line);
        }

    }
}
