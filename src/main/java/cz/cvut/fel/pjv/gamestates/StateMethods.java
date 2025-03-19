package cz.cvut.fel.pjv.gamestates;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * The StateMethods interface provides the structure for methods that need to be
 * implemented by various game states. These methods handle updates, rendering,
 * and input events for different states.
 */
public interface StateMethods {

    /**
     * Updates the state. This method is called to update the logic of the state.
     */
    public void update();

    /**
     * Draws the state. This method is called to render the state on the canvas.
     *
     * @param g the GraphicsContext used for drawing
     */
    public void draw(GraphicsContext g);

    /**
     * Handles mouse click events.
     *
     * @param e the MouseEvent representing the mouse click
     */
    public void mouseClicked(MouseEvent e);

    /**
     * Handles mouse press events.
     *
     * @param e the MouseEvent representing the mouse press
     */
    public void mousePressed(MouseEvent e);

    /**
     * Handles mouse release events.
     *
     * @param e the MouseEvent representing the mouse release
     */
    public void mouseReleased(MouseEvent e);

    /**
     * Handles mouse move events.
     *
     * @param e the MouseEvent representing the mouse move
     */
    public void mouseMoved(MouseEvent e);

    /**
     * Handles key press events.
     *
     * @param e the KeyEvent representing the key press
     */
    public void KeyPressed(KeyEvent e);

    /**
     * Handles key release events.
     *
     * @param e the KeyEvent representing the key release
     */
    public void KeyReleased(KeyEvent e);
}
