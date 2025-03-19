package cz.cvut.fel.pjv.gamestates;

import cz.cvut.fel.pjv.gui.AudioOptions;
import cz.cvut.fel.pjv.gui.PauseButton;
import cz.cvut.fel.pjv.gui.URMButtons;
import cz.cvut.fel.pjv.main.Game;
import cz.cvut.fel.pjv.utils.LoadSave;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import static cz.cvut.fel.pjv.utils.Constants.UI.URMButtons.*;

/**
 * Represents the game options state where audio options and other settings can be configured.
 */
public class GameOptions extends State implements StateMethods {

    private AudioOptions audioOptions;
    private Image background;
    private URMButtons menuButton;

    /**
     * Constructs the GameOptions state with the given game instance.
     *
     * @param game the Game instance
     */
    public GameOptions(Game game) {
        super(game);
        background = LoadSave.getSpriteAtlas(LoadSave.optionsBackground);
        loadButtons();
        audioOptions = game.getAudioOptions();
    }

    /**
     * Loads the menu button and initializes its position.
     */
    private void loadButtons() {
        int menuX = (int) (410 * Game.SCALE);
        int menuY = (int) (350 * Game.SCALE);

        menuButton = new URMButtons(menuX, menuY, URM_SIZE, URM_SIZE, 2);
    }

    /**
     * Updates the state of the menu button and audio options.
     */
    @Override
    public void update() {
        menuButton.update();
        audioOptions.update();
    }

    /**
     * Draws the background, audio options, and menu button on the provided GraphicsContext.
     *
     * @param g the GraphicsContext used for drawing
     */
    @Override
    public void draw(GraphicsContext g) {
        g.drawImage(background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        audioOptions.draw(g);
        menuButton.draw(g);
    }

    /**
     * Handles the mouse dragged event and updates the audio options accordingly.
     *
     * @param e the MouseEvent triggered by dragging the mouse
     */
    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    /**
     * Handles the mouse pressed event, setting the menu button as pressed if applicable.
     *
     * @param e the MouseEvent triggered by pressing the mouse
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuButton)) {
            menuButton.setMousePressed(true);
        } else
            audioOptions.mousePressed(e);
    }

    /**
     * Handles the mouse released event, checking if the menu button was pressed and updating the game state.
     *
     * @param e the MouseEvent triggered by releasing the mouse
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuButton)) {
            if (menuButton.isMousePressed()) {
                GameState.state = GameState.MENU;
            }
        } else
            audioOptions.mouseReleased(e);

        menuButton.resetBools();
    }

    /**
     * Handles the mouse moved event, updating the visual state of the menu button and audio options.
     *
     * @param e the MouseEvent triggered by moving the mouse
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        menuButton.setMouseOver(false);

        if (isIn(e, menuButton)) {
            menuButton.setMouseOver(true);
        } else
            audioOptions.mouseMoved(e);
    }

    @Override
    public void KeyPressed(KeyEvent e) {
        // Intentionally left blank as no key press actions are defined for GameOptions
    }

    @Override
    public void KeyReleased(KeyEvent e) {
        // Intentionally left blank as no key release actions are defined for GameOptions
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Intentionally left blank as no mouse click actions are defined for GameOptions
    }

    /**
     * Checks if the mouse event is within the bounds of the specified PauseButton.
     *
     * @param e the MouseEvent to check
     * @param b the PauseButton to check against
     * @return true if the event is within the button's bounds, false otherwise
     */
    private boolean isIn(MouseEvent e, PauseButton b) {
        return (b.getBounds().contains(e.getX(), e.getY()));
    }
}
