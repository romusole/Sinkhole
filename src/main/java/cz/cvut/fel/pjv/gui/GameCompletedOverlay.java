package cz.cvut.fel.pjv.gui;

import cz.cvut.fel.pjv.gamestates.GameState;
import cz.cvut.fel.pjv.gamestates.Playing;
import cz.cvut.fel.pjv.main.Game;
import cz.cvut.fel.pjv.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;

/**
 * Represents the overlay displayed when the game is completed.
 */
public class GameCompletedOverlay {
    private Playing playing;
    private Image background;

    /**
     * Constructs a GameCompletedOverlay with the specified Playing instance.
     *
     * @param playing the Playing instance associated with this overlay
     */
    public GameCompletedOverlay(Playing playing) {
        this.playing = playing;
        background = LoadSave.getSpriteAtlas(LoadSave.gameCompleted);
    }

    /**
     * Draws the game completed overlay on the provided GraphicsContext.
     *
     * @param gc the GraphicsContext to draw on
     */
    public void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        gc.drawImage(background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
    }

    /**
     * Handles key pressed events to navigate from the game completed overlay.
     *
     * @param e the key event
     */
    public void KeyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case BACK_SPACE -> {
                playing.setGameState(GameState.MENU);
                playing.loadNextLevel();
                playing.unpauseGame();
            }
            default -> {
            }
        }
    }
}
