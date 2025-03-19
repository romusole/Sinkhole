package cz.cvut.fel.pjv.gui;

import cz.cvut.fel.pjv.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import static cz.cvut.fel.pjv.utils.Constants.UI.VolumeButtons.*;

/**
 * Represents a volume control button that includes a slider for adjusting values.
 */
public class VolumeButtons extends PauseButton {

    private Image[] images;
    private Image slider;
    private int index = 0;
    private boolean mouseOver, mousePressed;
    private int buttonX;
    private int minX, maxX;
    private double value = 0d;

    /**
     * Constructs a VolumeButton with specified position, size, and height.
     *
     * @param x the x position of the button
     * @param y the y position of the button
     * @param width the width of the button
     * @param height the height of the button
     */
    public VolumeButtons(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_WIDTH, height);
        bounds.setX(getX() - VOLUME_WIDTH / 2);
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        minX = x + VOLUME_WIDTH / 2;
        maxX = x + width - VOLUME_WIDTH / 2;
        loadImgs();
    }

    /**
     * Loads images for the volume button and slider.
     */
    private void loadImgs() {
        Image temp = LoadSave.getSpriteAtlas(LoadSave.volumeButtons);
        images = new Image[3];
        for (int i = 0; i < images.length; i++) {
            images[i] = new WritableImage(temp.getPixelReader(), i * VOLUME_WIDTH_DEFAULT, 0, VOLUME_WIDTH_DEFAULT, VOLUME_HEIGHT_DEFAULT);
        }

        slider = new WritableImage(temp.getPixelReader(), 3 * VOLUME_WIDTH_DEFAULT, 0, SLIDER_WIDTH_DEFAULT, VOLUME_HEIGHT_DEFAULT);
    }

    /**
     * Updates the button's state, changing its visual representation based on user interaction.
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
     * Draws the volume button and slider on the provided GraphicsContext.
     *
     * @param g the GraphicsContext used for drawing
     */
    public void draw(GraphicsContext g) {
        g.drawImage(slider, x, y, width, height);
        g.drawImage(images[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height);
    }

    /**
     * Changes the x position of the button based on user interaction and updates the value.
     *
     * @param x the new x position of the button
     */
    public void changeX(int x) {
        if (x < minX) {
            buttonX = minX;
        } else if (x > maxX) {
            buttonX = maxX;
        } else {
            buttonX = x;
        }
        updateValue();
        bounds.setX(buttonX - VOLUME_WIDTH / 2);
    }

    /**
     * Updates the value based on the button's current position.
     */
    private void updateValue() {
        double range = maxX - minX;
        double newValue = buttonX - minX;
        value = newValue / range;
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

    public double getValue() {
        return value;
    }
}
