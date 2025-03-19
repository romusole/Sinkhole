package cz.cvut.fel.pjv.gamestates;

import cz.cvut.fel.pjv.audio.AudioPlayer;
import cz.cvut.fel.pjv.gui.MenuButton;
import cz.cvut.fel.pjv.main.Game;
import javafx.scene.input.MouseEvent;

/**
 * The State class serves as a base class for different states in the game.
 */
public class State {
    protected Game game;

    /**
     * Constructs a new State instance.
     *
     * @param game the Game instance
     */
    public State(Game game) {
        this.game = game;
    }

    /**
     * Checks if a given mouse event is within the bounds of a MenuButton.
     *
     * @param e the MouseEvent
     * @param mb the MenuButton to check
     * @return true if the event is within the button's bounds, false otherwise
     */
    public boolean isIn(MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    public Game getGame() {
        return game;
    }

    /**
     * Sets the current game state and plays the corresponding audio.
     *
     * @param state the GameState to set
     */
    public void setGameState(GameState state) {
        switch (state) {
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU);
            case PLAYING -> game.getAudioPlayer().playSong(AudioPlayer.LEVEL);
        }

        GameState.state = state;
    }
}
