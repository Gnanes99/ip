package dennis.ui;

import java.util.Scanner;

import dennis.task.Task;

/**
 * Handles every interaction with the user: reading command lines from
 * standard input and printing Dennis's replies to standard output.
 *
 * <p>Design note: keeping all {@code System.in}/{@code System.out} details in
 * one class means the rest of the program never touches the console directly.
 * That makes the command logic easier to read and test, and it would let a
 * different front end (for example a GUI) be swapped in by replacing only this
 * class.</p>
 */
public class Ui {
    /** Horizontal rule Dennis prints to separate some of its replies. */
    private static final String LINE =
            "_____________________________________________________";

    /** Source of user input; wraps standard input for the whole session. */
    private final Scanner scanner;

    /** Creates a UI that reads user input from standard input. */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /** Prints the startup banner and greeting, followed by a divider line. */
    public void showWelcome() {
        String banner = " ____                   _     \n"
                + "|  _ \\  ___ _ __  _ __ (_)___ \n"
                + "| | | |/ _ \\ '_ \\| '_ \\| / __|\n"
                + "| |_| |  __/ | | | | | | \\__ \\\n"
                + "|____/ \\___|_| |_|_| |_|_|___/\n";
        System.out.println(banner);
        System.out.println("Hi, my name is Dennis. It is lovely to meet you!");
        System.out.println("How may I help you today?");
        showLine();
    }

    /** Prints the farewell message shown when the user types {@code bye}. */
    public void showGoodbye() {
        System.out.println("Bye. Looking forward to seeing you again!");
    }

    /**
     * Reports whether the user has entered another line of input.
     *
     * @return {@code true} if another command can be read
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next raw command line from the user.
     *
     * @return the line exactly as typed (no trimming)
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Prints the horizontal divider line on its own. */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Prints a single line of text to the user.
     *
     * @param message text to display
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Prints an error in Dennis's standard style ({@code ERROR!! ...}).
     *
     * @param message explanation of what went wrong
     */
    public void showError(String message) {
        System.out.println("ERROR!! " + message);
    }

    /**
     * Confirms that a task was added and reports the new list size.
     *
     * @param task      the task that was added
     * @param taskCount the number of tasks now in the list
     */
    public void showAddedTask(Task task, int taskCount) {
        System.out.println("Understood. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Confirms that a task was removed and reports the new list size.
     *
     * @param task      the task that was removed
     * @param taskCount the number of tasks left in the list
     */
    public void showRemovedTask(Task task, int taskCount) {
        System.out.println("Understood. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now there are " + taskCount + " tasks in the list.");
    }

    /**
     * Confirms that a task was marked as done.
     *
     * @param task the task that was marked
     */
    public void showMarkedTask(Task task) {
        System.out.println("Excellent! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /**
     * Confirms that a task was marked as not done.
     *
     * @param task the task that was unmarked
     */
    public void showUnmarkedTask(Task task) {
        System.out.println("Alright, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }
}
