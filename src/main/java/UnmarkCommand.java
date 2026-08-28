/**
 * Marks one task as not done and saves the change.
 */
public class UnmarkCommand extends TaskCommand {
    /**
     * @param taskNumber position of the task to unmark (1 = first task)
     */
    public UnmarkCommand(int taskNumber) {
        super(taskNumber);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws DennisException {
        checkInRange(tasks);

        Task task = tasks.get(taskNumber - 1);
        task.markAsNotDone();
        storage.save(tasks.asList());

        ui.showUnmarkedTask(task);
    }
}
