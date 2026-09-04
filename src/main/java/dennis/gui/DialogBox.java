package dennis.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * One chat bubble: an {@link HBox} holding a single wrapped-text label.
 *
 * <p>The layout comes from {@code DialogBox.fxml}, loaded with the
 * {@code fx:root} technique so this object is both the controller and the
 * root node. User bubbles stay on the right; Dennis's bubbles are
 * {@link #flip() flipped} to the left.</p>
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;

    /**
     * Builds a bubble showing the given text.
     *
     * @param text the message to display
     */
    private DialogBox(String text) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    DialogBox.class.getResource("/view/DialogBox.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not load /view/DialogBox.fxml", e);
        }

        dialog.setText(text);
    }

    /** Aligns this bubble to the left, for one of Dennis's replies. */
    private void flip() {
        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Creates a right-aligned bubble for something the user typed.
     *
     * @param text the user's message
     * @return the bubble
     */
    public static DialogBox getUserDialog(String text) {
        return new DialogBox(text);
    }

    /**
     * Creates a left-aligned bubble for one of Dennis's replies.
     *
     * @param text Dennis's message
     * @return the bubble
     */
    public static DialogBox getDennisDialog(String text) {
        DialogBox box = new DialogBox(text);
        box.flip();
        return box;
    }
}
