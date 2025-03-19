package cz.cvut.fel.pjv.main;

import cz.cvut.fel.pjv.audio.AudioPlayer;
import cz.cvut.fel.pjv.gamestates.GameOptions;
import cz.cvut.fel.pjv.gamestates.GameState;
import cz.cvut.fel.pjv.gamestates.Menu;
import cz.cvut.fel.pjv.gamestates.Playing;
import cz.cvut.fel.pjv.gui.AudioOptions;
import cz.cvut.fel.pjv.inputs.KeyboardInputs;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

/**
 * The main class for the game, responsible for initializing components,
 * starting the game loop, and managing game states.
 */
public class Game implements Runnable {
    private Window window;
    private Panel panel;
    private Thread gameLoopThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private KeyboardInputs keyboardInputs;
    private Playing playing;
    private Menu menu;
    private AudioOptions audioOptions;
    private GameOptions gameOptions;
    private AudioPlayer audioPlayer;

    public final static int TILES_DEFAULT_SIZE = 32; // may be changed later
    public final static double SCALE = 1.5d;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    /**
     * Constructs the Game object and initializes its components.
     */
    public Game() {
        initClasses();
        panel = new Panel(this);
        Scene scene = new Scene(panel, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        window = new Window(panel, scene);
        panel.requestFocus();
        startGameLoop();

        // Initialize keyboard inputs
        keyboardInputs = new KeyboardInputs(panel);
        scene.setOnKeyPressed(event -> keyboardInputs.handle(event));
        scene.setOnKeyReleased(event -> keyboardInputs.handle(event));
    }

    /**
     * Initializes various game components.
     */
    private void initClasses() {
        audioOptions = new AudioOptions(this);
        menu = new Menu(this);
        playing = new Playing(this);
        gameOptions = new GameOptions(this);
        audioPlayer = new AudioPlayer();
    }

    /**
     * Starts the game loop in a separate thread.
     */
    private void startGameLoop() {
        gameLoopThread = new Thread(this);
        gameLoopThread.start();
    }

    /**
     * Updates the game state based on the current GameState.
     */
    public void update() {
        switch (GameState.state) {
            case PLAYING -> playing.update();
            case MENU -> menu.update();
            case OPTIONS -> gameOptions.update();
            case QUIT -> System.exit(0);
            default -> throw new IllegalStateException("Unexpected value: " + GameState.state);
        }
    }

    /**
     * Renders the game state based on the current GameState.
     *
     * @param g the GraphicsContext to use for rendering
     */
    public void render(GraphicsContext g) {
        switch (GameState.state) {
            case PLAYING -> playing.draw(g);
            case MENU -> menu.draw(g);
            case OPTIONS -> gameOptions.draw(g);
            default -> {
            }
        }
    }

    /**
     * Runs the game loop, managing the updates and rendering based on set FPS and UPS.
     */
    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;
        long previousTime = System.nanoTime();
        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();
        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;
            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }
            if (deltaF >= 1) {
                panel.render();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + "| UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    /**
     * Handles the loss of window focus by resetting the player's directional booleans.
     */
    public void windowFocusLost() {
        if (GameState.state == GameState.PLAYING) {
            playing.getPlayer().resetDirecBooleans();
        }
    }

    public KeyboardInputs getKeyboardInputs() {
        return keyboardInputs;
    }

    public Panel getPanel() {
        return panel;
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public AudioOptions getAudioOptions() {
        return audioOptions;
    }

    public GameOptions getGameOptions() {
        return gameOptions;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }
}
