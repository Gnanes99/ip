package dennis.command;

import dennis.storage.Storage;
import dennis.task.TaskList;
import dennis.ui.Ui;

/**
 * Shows every task in the list, in order, numbered from 1.
 */
public class ListCommand extends Command {
    /** Creates the list command. */
    public ListCommand() {
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMessage("Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            ui.showMessage((i + 1) + "." + tasks.get(i));
        }
    }
}
