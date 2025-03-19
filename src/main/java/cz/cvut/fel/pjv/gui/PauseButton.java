package cz.cvut.fel.pjv.gui;

import javafx.scene.shape.Rectangle;

/**
 * Represents a button used in the pause menu.
 */
public class PauseButton {
    protected int x, y, width, height;
    protected Rectangle bounds;

    /**
     * Constructs a PauseButton with the specified position and dimensions.
     *
     * @param x the x position of the button
     * @param y the y position of the button
     * @param width the width of the button
     * @param height the height of the button
     */
    public PauseButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        createBounds();
    }

    /**
     * Creates the bounding rectangle for the button.
     */
    private void createBounds() {
        bounds = new Rectangle(x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }
}
