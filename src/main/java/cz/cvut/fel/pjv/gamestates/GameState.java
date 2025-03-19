package cz.cvut.fel.pjv.gamestates;

/**
 * Represents the different states of the game.
 */
public enum GameState {

    PLAYING, MENU, OPTIONS, QUIT;

    /**
     * The current state of the game. Initialized to MENU by default.
     */
    public static GameState state = MENU;
}
