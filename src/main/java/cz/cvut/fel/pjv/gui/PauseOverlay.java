package cz.cvut.fel.pjv.gui;

import cz.cvut.fel.pjv.gamestates.GameState;
import cz.cvut.fel.pjv.gamestates.Playing;
import cz.cvut.fel.pjv.main.Game;
import cz.cvut.fel.pjv.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import static cz.cvut.fel.pjv.utils.Constants.UI.URMButtons.*;

/**
 * This class represents the overlay that appears when the game is paused.
 */
public class PauseOverlay {

    private Playing playing;
    private Image backgroundImage;
    private AudioOptions audioOptions;
    private int bgX, bgY, bgWidth, bgHeight;
    private URMButtons menuButton, replayButton, unpauseButton;

    /**
     * Constructs a PauseOverlay with the given Playing instance.
     *
     * @param playing the Playing instance representing the current game state
     */
    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        audioOptions = playing.getGame().getAudioOptions();
        createURMButtons();
    }

    /**
     * Initializes the URM (Unpause, Replay, Menu) buttons.
     */
    private void createURMButtons() {
        int menuX = (int) (340 * Game.SCALE);
        int replayX = (int) (410 * Game.SCALE);
        int unpauseX = (int) (480 * Game.SCALE);
        int urmY = (int) (370 * Game.SCALE);

        menuButton = new URMButtons(menuX, urmY, URM_SIZE, URM_SIZE, 2);
        replayButton = new URMButtons(replayX, urmY, URM_SIZE, URM_SIZE, 1);
        unpauseButton = new URMButtons(unpauseX, urmY, URM_SIZE, URM_SIZE, 0);
    }

    /**
     * Loads the background image for the pause overlay.
     */
    private void loadBackground() {
        backgroundImage = LoadSave.getSpriteAtlas(LoadSave.pauseBackground);
        bgWidth = (int) (backgroundImage.getWidth() * Game.SCALE);
        bgHeight = (int) (backgroundImage.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgWidth / 2;
        bgY = 50;
    }

    /**
     * Updates the state of the pause overlay, including buttons and audio options.
     */
    public void update() {
        menuButton.update();
        replayButton.update();
        unpauseButton.update();
        audioOptions.update();
    }

    /**
     * Draws the pause overlay, including the background and buttons.
     *
     * @param g the GraphicsContext used for drawing
     */
    public void draw(GraphicsContext g) {
        g.drawImage(backgroundImage, bgX, bgY, bgWidth, bgHeight);
        menuButton.draw(g);
        replayButton.draw(g);
        unpauseButton.draw(g);
        audioOptions.draw(g);
    }

    /**
     * Handles mouse dragged events.
     *
     * @param e the MouseEvent triggered when the mouse is dragged
     */
    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    /**
     * Handles mouse pressed events.
     *
     * @param e the MouseEvent triggered when the mouse is pressed
     */
    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuButton)) {
            menuButton.setMousePressed(true);
        } else if (isIn(e, replayButton)) {
            replayButton.setMousePressed(true);
        } else if (isIn(e, unpauseButton)) {
            unpauseButton.setMousePressed(true);
        } else {
            audioOptions.mousePressed(e);
        }
    }

    /**
     * Handles mouse released events.
     *
     * @param e the MouseEvent triggered when the mouse is released
     */
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuButton)) {
            if (menuButton.isMousePressed()) {
                menuButton.update();
                playing.setGameState(GameState.MENU);
                playing.resetAll();
                playing.unpauseGame();
            }
        } else if (isIn(e, replayButton)) {
            if (replayButton.isMousePressed()) {
                replayButton.update();
                playing.resetAll();
            }
        } else if (isIn(e, unpauseButton)) {
            if (unpauseButton.isMousePressed()) {
                unpauseButton.update();
                playing.unpauseGame();
            }
        } else {
            audioOptions.mouseReleased(e);
        }

        menuButton.resetBools();
        replayButton.resetBools();
        unpauseButton.resetBools();
    }

    /**
     * Handles mouse moved events.
     *
     * @param e the MouseEvent triggered when the mouse is moved
     */
    public void mouseMoved(MouseEvent e) {
        menuButton.setMouseOver(false);
        replayButton.setMouseOver(false);
        unpauseButton.setMouseOver(false);

        if (isIn(e, menuButton)) {
            menuButton.setMouseOver(true);
        } else if (isIn(e, replayButton)) {
            replayButton.setMouseOver(true);
        } else if (isIn(e, unpauseButton)) {
            unpauseButton.setMouseOver(true);
        } else {
            audioOptions.mouseMoved(e);
        }
    }

    /**
     * Checks if the mouse event occurred within the bounds of a button.
     *
     * @param e the MouseEvent
     * @param b the PauseButton to check
     * @return true if the event occurred within the button's bounds, false otherwise
     */
    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
