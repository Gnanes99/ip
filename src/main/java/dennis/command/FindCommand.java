package dennis.command;

import dennis.storage.Storage;
import dennis.task.Task;
import dennis.task.TaskList;
import dennis.ui.Ui;

/**
 * Lists every task whose description contains a given keyword.
 *
 * <p>Matches are shown renumbered from 1, in list order. The search is
 * case-sensitive and looks at the description text only, so the numbers
 * shown here do not line up with {@code mark}/{@code unmark}/{@code delete}.</p>
 */
public class FindCommand extends Command {
    /** The keyword to search task descriptions for. */
    private final String keyword;

    /**
     * Creates a command that searches for the given keyword.
     *
     * @param keyword the text to look for in task descriptions
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMessage("Here are the matching tasks in your list:");

        int matchCount = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.matches(keyword)) {
                matchCount++;
                ui.showMessage(matchCount + "." + task);
            }
        }

        if (matchCount == 0) {
            ui.showMessage("No matching tasks found.");
        }
    }
}
