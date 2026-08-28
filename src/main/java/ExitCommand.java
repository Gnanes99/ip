/**
 * Says goodbye and signals that the program should stop.
 */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
        ui.showLine();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
