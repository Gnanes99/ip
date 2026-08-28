import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds the tasks for the current session together with the operations
 * that act on the collection as a whole: adding a task, deleting one by
 * position, and reading one back.
 *
 * <p>Design note: wrapping the {@link ArrayList} in its own type (instead
 * of passing the raw list around) keeps all list handling in one place and
 * gives the rest of the program a name to talk about. The positions used
 * here are 0-based, matching {@link ArrayList}, so callers that already
 * think in terms of a list index need no translation.</p>
 */
public class TaskList {
    /** Backing store; its order is the order shown to the user. */
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list backed by the given tasks, e.g. the ones just
     * read from disk by {@link Storage#load()}.
     *
     * <p>The list is adopted directly rather than copied: {@code load()}
     * returns a fresh list that nothing else keeps a reference to. If the
     * source were shared elsewhere, a defensive copy
     * ({@code new ArrayList<>(tasks)}) would stop outside code from
     * mutating this list unexpectedly.</p>
     *
     * @param tasks initial tasks, in display order
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the given position.
     *
     * @param index 0-based position of the task to remove
     * @return the task that was removed
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at the given position without removing it.
     *
     * @param index 0-based position of the task
     * @return the task at that position
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return the task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns a read-only view of the tasks in display order, for the
     * parts of the program that need to walk the whole list (for example
     * {@link Storage} when saving).
     *
     * @return an unmodifiable list of the current tasks
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }
}
