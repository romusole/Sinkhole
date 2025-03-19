package cz.cvut.fel.pjv.inputs;

import cz.cvut.fel.pjv.gamestates.GameState;
import cz.cvut.fel.pjv.main.Panel;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Handles mouse inputs for the game.
 */
public class MouseInputs implements EventHandler<MouseEvent> {
    private Panel panel;

    /**
     * Constructs a MouseInputs handler with the specified panel.
     *
     * @param panel the panel associated with this handler
     */
    public MouseInputs(Panel panel) {
        this.panel = panel;
    }

    /**
     * Handles the mouse event by delegating to the appropriate method based on the event type.
     *
     * @param event the mouse event to handle
     */
    @Override
    public void handle(MouseEvent event) {

        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            switch (GameState.state) {
                case PLAYING -> {
                    panel.getGame().getPlaying().mouseClicked(event);
                    panel.getGame().getPlaying().mousePressed(event);
                    panel.getGame().getPlaying().mouseReleased(event);
                }
                case MENU -> {
                    panel.getGame().getMenu().mousePressed(event);
                    panel.getGame().getMenu().mouseReleased(event);
                }
                case OPTIONS -> {
                    panel.getGame().getGameOptions().mousePressed(event);
                    panel.getGame().getGameOptions().mouseReleased(event);
                }
                default -> {
                }
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            switch (GameState.state) {
                case PLAYING -> panel.getGame().getPlaying().mouseReleased(event);
                case MENU -> panel.getGame().getMenu().mouseReleased(event);
                case OPTIONS -> panel.getGame().getGameOptions().mouseReleased(event);
                default -> {
                }
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
            switch (GameState.state) {
                case PLAYING -> panel.getGame().getPlaying().mouseMoved(event);
                case MENU -> panel.getGame().getMenu().mouseMoved(event);
                case OPTIONS -> panel.getGame().getGameOptions().mouseMoved(event);
                default -> {
                }
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            switch (GameState.state) {
                case PLAYING -> panel.getGame().getPlaying().mousePressed(event);
                case MENU -> panel.getGame().getMenu().mousePressed(event);
                case OPTIONS -> panel.getGame().getGameOptions().mousePressed(event);
                default -> {
                }
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            switch (GameState.state) {
                case PLAYING -> panel.getGame().getPlaying().mouseDragged(event);
                case OPTIONS -> panel.getGame().getGameOptions().mouseDragged(event);
                default -> {
                }
            }
        }
    }
}
