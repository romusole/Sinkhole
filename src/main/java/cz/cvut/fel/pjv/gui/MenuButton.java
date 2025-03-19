package cz.cvut.fel.pjv.gui;

import cz.cvut.fel.pjv.gamestates.GameState;
import cz.cvut.fel.pjv.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Rectangle;

import static cz.cvut.fel.pjv.utils.Constants.UI.Buttons.*;

/**
 * Represents a button in the game's menu.
 */
public class MenuButton {
    private int xPos, yPos, rowIndex, index;
    private int xOffsetCenter = B_WIDTH / 2;
    private GameState state;
    private Image[] images;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;

    /**
     * Constructs a MenuButton with the specified position, row index, and game state.
     *
     * @param xPos the x position of the button
     * @param yPos the y position of the button
     * @param rowIndex the row index of the button in the sprite sheet
     * @param state the game state associated with this button
     */
    public MenuButton(int xPos, int yPos, int rowIndex, GameState state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImages();
        initBounds();
    }

    /**
     * Initializes the bounding rectangle for the button.
     */
    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
    }

    /**
     * Loads the button images from the sprite sheet.
     */
    private void loadImages() {
        images = new Image[3];
        Image temp = LoadSave.getSpriteAtlas(LoadSave.menuButtons);
        for (int i = 0; i < images.length; i++) {
            images[i] = new WritableImage(temp.getPixelReader(), i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
        }
    }

    /**
     * Draws the button on the provided GraphicsContext.
     *
     * @param g the GraphicsContext to draw on
     */
    public void draw(GraphicsContext g) {
        g.drawImage(images[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
    }

    /**
     * Updates the button's appearance based on mouse interaction.
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
     * Returns whether the mouse is currently over the button.
     *
     * @return true if the mouse is over the button, false otherwise
     */
    public boolean isMouseOver() {
        return mouseOver;
    }

    /**
     * Sets whether the mouse is currently over the button.
     *
     * @param mouseOver true if the mouse is over the button, false otherwise
     */
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    /**
     * Returns whether the mouse button is currently pressed on the button.
     *
     * @return true if the mouse button is pressed, false otherwise
     */
    public boolean isMousePressed() {
        return mousePressed;
    }

    /**
     * Sets whether the mouse button is currently pressed on the button.
     *
     * @param mousePressed true if the mouse button is pressed, false otherwise
     */
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    /**
     * Applies the game state associated with this button.
     */
    public void applyGameState() {
        GameState.state = state;
    }

    /**
     * Resets the button's interaction booleans.
     */
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public GameState getState() {
        return state;
    }
}
