package dennis.command;

import java.util.List;

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
        List<Task> matches = tasks.asList().stream()
                .filter(task -> task.matches(keyword))
                .toList();

        ui.showMessage("Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            ui.showMessage((i + 1) + "." + matches.get(i));
        }

        if (matches.isEmpty()) {
            ui.showMessage("No matching tasks found.");
        }
    }
}
