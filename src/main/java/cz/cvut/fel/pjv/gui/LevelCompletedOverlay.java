package cz.cvut.fel.pjv.gui;

import cz.cvut.fel.pjv.audio.AudioPlayer;
import cz.cvut.fel.pjv.gamestates.GameState;
import cz.cvut.fel.pjv.gamestates.Playing;
import cz.cvut.fel.pjv.main.Game;
import cz.cvut.fel.pjv.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import static cz.cvut.fel.pjv.utils.Constants.UI.URMButtons.*;

/**
 * Represents the overlay displayed when a level is completed.
 */
public class LevelCompletedOverlay {
    private Playing playing;
    private URMButtons menuButton, nextButton;
    private Image image;
    private int backgroundX, backgroundY, backgroundWidth, backgroundHeight;

    /**
     * Constructs a LevelCompletedOverlay with the specified Playing instance.
     *
     * @param playing the Playing instance associated with this overlay
     */
    public LevelCompletedOverlay(Playing playing) {
        this.playing = playing;
        initImage();
        initButtons();
    }

    /**
     * Initializes the buttons for the level completed overlay.
     */
    private void initButtons() {
        int menuX = (int) (370 * Game.SCALE);
        int nextX = (int) (450 * Game.SCALE);
        int Y = (int) (250 * Game.SCALE);
        nextButton = new URMButtons(nextX, Y, URM_SIZE, URM_SIZE, 3);
        menuButton = new URMButtons(menuX, Y, URM_SIZE, URM_SIZE, 2);
    }

    /**
     * Initializes the background image for the level completed overlay.
     */
    private void initImage() {
        image = LoadSave.getSpriteAtlas(LoadSave.levelCompleted);
        backgroundWidth = (int) (image.getWidth() / 2.5);
        backgroundHeight = (int) (image.getHeight() / 2.5);
        backgroundX = Game.GAME_WIDTH / 2 - backgroundWidth / 2;
        backgroundY = (int) (50 * Game.SCALE);
    }

    /**
     * Updates the state of the buttons.
     */
    public void update() {
        menuButton.update();
        nextButton.update();
    }

    /**
     * Draws the level completed overlay on the provided GraphicsContext.
     *
     * @param gc the GraphicsContext to draw on
     */
    public void draw(GraphicsContext gc) {
        gc.drawImage(image, backgroundX, backgroundY, backgroundWidth, backgroundHeight);
        menuButton.draw(gc);
        nextButton.draw(gc);
    }

    /**
     * Handles mouse moved events to update button hover states.
     *
     * @param e the mouse event
     */
    public void mouseMoved(MouseEvent e) {
        nextButton.setMouseOver(false);
        menuButton.setMouseOver(false);

        if (isIn(menuButton, e)) {
            menuButton.setMouseOver(true);
        } else if (isIn(nextButton, e)) {
            nextButton.setMouseOver(true);
        }
    }

    /**
     * Handles mouse released events for the level completed overlay buttons.
     *
     * @param e the mouse event
     */
    public void mouseReleased(MouseEvent e) {
        if (isIn(menuButton, e)) {
            if (menuButton.isMousePressed()) {
                playing.resetAll();
                playing.setGameState(GameState.MENU);
            }
        } else if (isIn(nextButton, e)) {
            if (nextButton.isMousePressed()) {
                playing.loadNextLevel();
                playing.getGame().getAudioPlayer().playSong(AudioPlayer.LEVEL);
            }
        }

        menuButton.resetBools();
        nextButton.resetBools();
    }

    /**
     * Handles mouse pressed events for the level completed overlay buttons.
     *
     * @param e the mouse event
     */
    public void mousePressed(MouseEvent e) {
        if (isIn(menuButton, e)) {
            menuButton.setMousePressed(true);
        } else if (isIn(nextButton, e)) {
            nextButton.setMousePressed(true);
        }
    }

    /**
     * Checks if the mouse event is within the bounds of the specified button.
     *
     * @param button the button to check
     * @param e the mouse event
     * @return true if the mouse event is within the bounds of the button, false otherwise
     */
    private boolean isIn(URMButtons button, MouseEvent e) {
        return button.getBounds().contains(e.getX(), e.getY());
    }
}
