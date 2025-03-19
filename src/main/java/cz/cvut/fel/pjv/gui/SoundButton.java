package cz.cvut.fel.pjv.gui;

import cz.cvut.fel.pjv.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import static cz.cvut.fel.pjv.utils.Constants.UI.PauseButtons.*;

/**
 * Represents a sound button used in the game's pause overlay.
 * This button allows the user to toggle sound on and off.
 */
public class SoundButton extends PauseButton {
    private Image[][] soundImages;
    private boolean mouseOver, mousePressed;
    private boolean muted;
    private int rowIndex, colIndex;

    /**
     * Constructs a SoundButton with specified position and size.
     *
     * @param x      the x position of the button
     * @param y      the y position of the button
     * @param width  the width of the button
     * @param height the height of the button
     */
    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        loadSoundImages();
    }

    /**
     * Loads the images for the sound button.
     */
    private void loadSoundImages() {
        Image temp = LoadSave.getSpriteAtlas(LoadSave.soundButtons);
        soundImages = new Image[2][3];
        for (int j = 0; j < soundImages.length; j++) {
            for (int i = 0; i < soundImages[j].length; i++) {
                soundImages[j][i] = new WritableImage(temp.getPixelReader(), i * SOUND_SIZE_DEFAULT, j * SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);
            }
        }
    }

    /**
     * Updates the state of the sound button, including its visual representation.
     */
    public void update() {
        rowIndex = muted ? 1 : 0;
        colIndex = mouseOver ? 1 : 0;
        if (mousePressed) {
            colIndex = 2;
        }
    }

    /**
     * Draws the sound button on the provided GraphicsContext.
     *
     * @param g the GraphicsContext used for drawing
     */
    public void draw(GraphicsContext g) {
        g.drawImage(soundImages[rowIndex][colIndex], x, y, width, height);
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

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }
}
