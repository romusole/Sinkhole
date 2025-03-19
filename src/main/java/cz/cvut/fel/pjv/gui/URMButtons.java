package cz.cvut.fel.pjv.gui;

import cz.cvut.fel.pjv.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import static cz.cvut.fel.pjv.utils.Constants.UI.URMButtons.*;

/**
 * Represents a button in the pause menu with different states (mouse over, mouse pressed).
 * This button can be used for actions such as unpause, replay, or menu navigation.
 */
public class URMButtons extends PauseButton {
    private Image[] images;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed;

    /**
     * Constructs a URMButton with specified position, size, and row index for the sprite.
     *
     * @param x        the x position of the button
     * @param y        the y position of the button
     * @param width    the width of the button
     * @param height   the height of the button
     * @param rowIndex the row index in the sprite sheet for the button image
     */
    public URMButtons(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImgs();
    }

    /**
     * Loads the images for the URMButton.
     */
    private void loadImgs() {
        Image temp = LoadSave.getSpriteAtlas(LoadSave.urmButtons);
        images = new Image[3];
        for (int i = 0; i < images.length; i++) {
            images[i] = new WritableImage(temp.getPixelReader(), i * URM_SIZE_DEFAULT, rowIndex * URM_SIZE_DEFAULT, URM_SIZE_DEFAULT, URM_SIZE_DEFAULT);
        }
    }

    /**
     * Updates the state of the URMButton, including its visual representation.
     */
    public void update() {
        index = 0;
        if (mouseOver) {
            index = 1;
        }
        if (mousePressed) {
            index = 2;
        }
    }

    /**
     * Draws the URMButton on the provided GraphicsContext.
     *
     * @param g the GraphicsContext used for drawing
     */
    public void draw(GraphicsContext g) {
        g.drawImage(images[index], x, y, URM_SIZE, URM_SIZE);
    }

    /**
     * Resets the mouse interaction booleans to false.
     */
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
}
