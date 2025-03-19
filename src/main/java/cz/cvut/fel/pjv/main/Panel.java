package cz.cvut.fel.pjv.main;

import cz.cvut.fel.pjv.inputs.KeyboardInputs;
import cz.cvut.fel.pjv.inputs.MouseInputs;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * The Panel class extends StackPane and serves as the main drawing surface for the game.
 * It handles mouse and keyboard inputs, and contains the game canvas.
 */
public class Panel extends StackPane {
    private final MouseInputs mouseInputs;
    private final KeyboardInputs keyboardInputs;
    private Game game;
    private Canvas canvas;

    public Panel(Game game) {
        mouseInputs = new MouseInputs(this);
        keyboardInputs = new KeyboardInputs(this);
        this.game = game;
        setCanvas();
    }

    /**
     * Sets up the canvas, configures its size, and binds mouse event handlers.
     */
    private void setCanvas() {
        canvas = new Canvas();
        canvas.setWidth(Game.GAME_WIDTH);
        canvas.setHeight(Game.GAME_HEIGHT);
        canvas.setFocusTraversable(true);

        canvas.setOnMouseClicked(mouseInputs);
        canvas.setOnMouseMoved(mouseInputs);
        canvas.setOnMouseDragged(mouseInputs);

        getChildren().add(canvas);
    }

    /**
     * Renders the game by drawing on the canvas's GraphicsContext.
     */
    public void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        game.render(gc);
    }

    public Game getGame() {
        return game;
    }

    public KeyboardInputs getKeyboardInputs() {
        return keyboardInputs;
    }
}
