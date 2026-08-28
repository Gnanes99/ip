package dennis;

import dennis.command.Command;
import dennis.parser.Parser;
import dennis.storage.Storage;
import dennis.task.TaskList;
import dennis.ui.Ui;

// reused existing code, changed banner and filename
public class Dennis {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    public Dennis() {
        ui = new Ui();
        storage = new Storage();

        // Level 7: reload any previously saved tasks on start-up, then save
        // back to ./data/dennis.txt after every change to the list.
        tasks = new TaskList(storage.load());
    }

    public void run() {
        ui.showWelcome();

        boolean isExit = false;
        while (!isExit && ui.hasNextCommand()) {
            String fullCommand = ui.readCommand();

            try {
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (DennisException e) {
                ui.showError(e.getMessage());
                ui.showLine();
            }
        }
    }

    public static void main(String[] args) {
        new Dennis().run();
    }
}
