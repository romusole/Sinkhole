package cz.cvut.fel.pjv.gui;

import cz.cvut.fel.pjv.audio.AudioPlayer;
import cz.cvut.fel.pjv.gamestates.GameState;
import cz.cvut.fel.pjv.gamestates.Playing;
import cz.cvut.fel.pjv.main.Game;
import cz.cvut.fel.pjv.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import static cz.cvut.fel.pjv.utils.Constants.UI.URMButtons.URM_SIZE;

/**
 * Represents the game over overlay displayed when the player loses the game.
 */
public class GameOverOverlay {
    private Playing playing;
    private Image image;
    private int imageX, imageY, imageWidth, imageHeight;
    private URMButtons menuButton, replayButton;

    /**
     * Constructs a GameOverOverlay with the specified Playing instance.
     *
     * @param playing the Playing instance associated with this overlay
     */
    public GameOverOverlay(Playing playing) {
        this.playing = playing;
        createImage();
        initButtons();
    }

    /**
     * Initializes the buttons for the game over overlay.
     */
    private void initButtons() {
        int menuX = (int) (360 * Game.SCALE);
        int replayX = (int) (440 * Game.SCALE);
        int Y = (int) (250 * Game.SCALE);
        replayButton = new URMButtons(replayX, Y, URM_SIZE, URM_SIZE, 1);
        menuButton = new URMButtons(menuX, Y, URM_SIZE, URM_SIZE, 2);
    }

    /**
     * Creates the game over image.
     */
    private void createImage() {
        image = LoadSave.getSpriteAtlas(LoadSave.gameOverScreen);
        imageWidth = (int) (image.getWidth() * Game.SCALE);
        imageHeight = (int) (image.getHeight() * Game.SCALE);
        imageX = Game.GAME_WIDTH / 2 - imageWidth / 2;
        imageY = (int) (75 * Game.SCALE);
    }

    /**
     * Updates the state of the buttons.
     */
    public void update() {
        menuButton.update();
        replayButton.update();
    }

    /**
     * Draws the game over overlay on the provided GraphicsContext.
     *
     * @param g the GraphicsContext to draw on
     */
    public void draw(GraphicsContext g) {
        g.setFill(Color.rgb(0, 0, 0, 0.5));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        g.drawImage(image, imageX, imageY, imageWidth, imageHeight);
        menuButton.draw(g);
        replayButton.draw(g);
    }

    /**
     * Handles mouse moved events to update button hover states.
     *
     * @param e the mouse event
     */
    public void mouseMoved(MouseEvent e) {
        replayButton.setMouseOver(false);
        menuButton.setMouseOver(false);

        if (isIn(menuButton, e)) {
            menuButton.setMouseOver(true);
        } else if (isIn(replayButton, e)) {
            replayButton.setMouseOver(true);
        }
    }

    /**
     * Handles mouse released events for the game over overlay buttons.
     *
     * @param e the mouse event
     */
    public void mouseReleased(MouseEvent e) {
        if (isIn(menuButton, e)) {
            if (menuButton.isMousePressed()) {
                playing.resetAll();
                playing.setGameState(GameState.MENU);
            }
        } else if (isIn(replayButton, e)) {
            if (replayButton.isMousePressed()) {
                playing.resetAll();
                playing.getGame().getAudioPlayer().playSong(AudioPlayer.LEVEL);
            }
        }

        menuButton.resetBools();
        replayButton.resetBools();
    }

    /**
     * Handles mouse pressed events for the game over overlay buttons.
     *
     * @param e the mouse event
     */
    public void mousePressed(MouseEvent e) {
        if (isIn(menuButton, e)) {
            menuButton.setMousePressed(true);
        } else if (isIn(replayButton, e)) {
            replayButton.setMousePressed(true);
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
