package dennis.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dennis.DennisException;
import dennis.task.Todo;

/**
 * Tests for {@link DialogUi}: that command output is accumulated into the
 * buffer, joined with line separators, and cleared by {@link DialogUi#drain()}.
 * The overridden {@code show*} methods just delegate to the buffer, so only a
 * representative few are exercised here.
 */
public class DialogUiTest {

    @Test
    public void drain_singleMessage_returnsThatMessage() {
        DialogUi ui = new DialogUi();
        ui.showMessage("hello");
        assertEquals("hello", ui.drain());
    }

    @Test
    public void drain_multipleMessages_joinedWithLineSeparator() {
        DialogUi ui = new DialogUi();
        ui.showMessage("first");
        ui.showMessage("second");
        assertEquals("first" + System.lineSeparator() + "second", ui.drain());
    }

    @Test
    public void drain_calledAgain_returnsEmptyString() {
        DialogUi ui = new DialogUi();
        ui.showMessage("only once");
        ui.drain();
        assertEquals("", ui.drain());
    }

    @Test
    public void showLine_appendsNothing() {
        DialogUi ui = new DialogUi();
        ui.showLine();
        assertEquals("", ui.drain());
    }

    @Test
    public void showError_isPrefixedWithErrorMarker() {
        DialogUi ui = new DialogUi();
        ui.showError("something went wrong");
        assertEquals("ERROR!! something went wrong", ui.drain());
    }

    @Test
    public void showAddedTask_formatsConfirmationOverThreeLines()
            throws DennisException {
        DialogUi ui = new DialogUi();
        Todo todo = new Todo("read book");

        ui.showAddedTask(todo, 1);

        String sep = System.lineSeparator();
        String expected = "Understood. I've added this task:" + sep
                + "  [T][ ] read book" + sep
                + "Now you have 1 tasks in the list.";
        assertEquals(expected, ui.drain());
    }
}
