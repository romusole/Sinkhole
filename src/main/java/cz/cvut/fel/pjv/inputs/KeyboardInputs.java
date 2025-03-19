package cz.cvut.fel.pjv.inputs;

import cz.cvut.fel.pjv.gamestates.GameState;
import cz.cvut.fel.pjv.main.Panel;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Handles keyboard inputs for the game.
 */
public class KeyboardInputs implements EventHandler<KeyEvent> {
    private Panel panel;

    /**
     * Constructs a KeyboardInputs handler with the specified panel.
     *
     * @param panel the panel associated with this handler
     */
    public KeyboardInputs(Panel panel) {
        this.panel = panel;
    }

    /**
     * Handles the key event by delegating to the appropriate method based on the event type.
     *
     * @param event the key event to handle
     */
    @Override
    public void handle(KeyEvent event) {
        KeyCode keyCode = event.getCode();

        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            handleKeyPressed(event);
        } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            handleKeyReleased(event);
        }
    }

    private void handleKeyPressed(KeyEvent event) {
        switch (GameState.state) {
            case PLAYING -> panel.getGame().getPlaying().KeyPressed(event);
            case MENU -> panel.getGame().getMenu().KeyPressed(event);
            default -> {
            }
        }
    }

    private void handleKeyReleased(KeyEvent event) {
        switch (GameState.state) {
            case PLAYING -> panel.getGame().getPlaying().KeyReleased(event);
            case MENU -> panel.getGame().getMenu().KeyReleased(event);
            default -> {
            }
        }
    }
}
