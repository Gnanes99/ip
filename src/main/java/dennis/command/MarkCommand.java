package dennis.command;

import dennis.DennisException;
import dennis.storage.Storage;
import dennis.task.Task;
import dennis.task.TaskList;
import dennis.ui.Ui;

/**
 * Marks one task as done and saves the change.
 */
public class MarkCommand extends TaskCommand {
    /**
     * @param taskNumber position of the task to mark (1 = first task)
     */
    public MarkCommand(int taskNumber) {
        super(taskNumber);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws DennisException {
        checkInRange(tasks);

        Task task = tasks.get(taskNumber - 1);
        task.markAsDone();
        storage.save(tasks.asList());

        ui.showMarkedTask(task);
    }
}
