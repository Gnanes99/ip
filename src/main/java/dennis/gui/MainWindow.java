package dennis.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Controller for {@code MainWindow.fxml}: passes user input to the
 * {@link GuiResponder} and shows both sides of the conversation as
 * {@link DialogBox} bubbles.
 */
public class MainWindow {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;

    /** Produces Dennis's replies; injected by {@link MainApp} after loading. */
    private GuiResponder responder;

    /** Creates the controller; JavaFX then populates the {@code @FXML} fields. */
    public MainWindow() {
    }

    /**
     * Injects the backend session and shows Dennis's greeting. Called by
     * {@link MainApp} once the FXML has been loaded.
     *
     * @param guiResponder the session that generates replies
     */
    public void setResponder(GuiResponder guiResponder) {
        responder = guiResponder;
        dialogContainer.getChildren().add(
                DialogBox.getDennisDialog(responder.getGreeting()));
    }

    /** Keeps the scroll pane pinned to the latest message. */
    @FXML
    private void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Handles the Send button and the Enter key: shows the user's text, then
     * Dennis's reply, then clears the input field. Blank input is ignored.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isEmpty()) {
            return;
        }

        String response = responder.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input),
                DialogBox.getDennisDialog(response));
        userInput.clear();
    }
}
