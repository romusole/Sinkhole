package cz.cvut.fel.pjv.gamestates;

import cz.cvut.fel.pjv.audio.AudioPlayer;
import cz.cvut.fel.pjv.gui.MenuButton;
import cz.cvut.fel.pjv.main.Game;
import cz.cvut.fel.pjv.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import static javafx.scene.input.KeyCode.ENTER;

/**
 * The Menu class represents the main menu state of the game.
 */
public class Menu extends State implements StateMethods {
    private MenuButton[] menuButtons = new MenuButton[3];
    private Image menuBackground;
    private Game game;

    /**
     * Constructs a new Menu instance.
     *
     * @param game the Game instance
     */
    public Menu(Game game) {
        super(game);
        this.game = game;
        loadButtons();
        menuBackground = LoadSave.getSpriteAtlas(LoadSave.menuBackgroud);
    }

    /**
     * Loads the menu buttons and initializes their positions.
     */
    private void loadButtons() {
        menuButtons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (190 * Game.SCALE), 0, GameState.PLAYING);
        menuButtons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (230 * Game.SCALE), 1, GameState.OPTIONS);
        menuButtons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int) (270 * Game.SCALE), 2, GameState.QUIT);
    }

    @Override
    public void update() {
        for (MenuButton button : menuButtons) {
            button.update();
        }
    }

    @Override
    public void draw(GraphicsContext g) {
        g.clearRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        g.drawImage(menuBackground, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        for (MenuButton button : menuButtons) {
            button.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton button : menuButtons) {
            if(isIn(e, button)) {
                button.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton button : menuButtons) {
            if(isIn(e, button)) {
                if(button.isMousePressed()) {
                    button.applyGameState();
                }
                if (button.getState() == GameState.PLAYING)
                    game.getAudioPlayer().playSong(AudioPlayer.LEVEL);
                break;
            }
        }
        resetButtons();
    }

    /**
     * Resets the state of all menu buttons.
     */
    private void resetButtons() {
        for (MenuButton button : menuButtons) {
            button.resetBools();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton button : menuButtons) {
            button.setMouseOver(false);
        }

        for (MenuButton button : menuButtons) {
            if (isIn(e, button)) {
                button.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void KeyPressed(KeyEvent e) {
        if(e.getCode() == ENTER) {
            GameState.state = GameState.PLAYING;
        }
    }

    @Override
    public void KeyReleased(KeyEvent e) {

    }
}
