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
            String fullCommand = ui.readCommand();

            try {
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);

                if (command.isExit()) {
                    return;
                }
            } catch (DennisException e) {
                ui.showError(e.getMessage());
                ui.showLine();
            }
        }
    }
}
