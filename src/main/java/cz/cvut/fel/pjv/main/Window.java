package cz.cvut.fel.pjv.main;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Window class extends Stage and represents the main game window.
 * It sets up the window properties, manages focus, and handles keyboard events.
 */
public class Window extends Stage {

    /**
     * Constructs a Window object with the specified panel and scene.
     *
     * @param panel the Panel instance to be associated with this window
     * @param scene the Scene instance to be set on this window
     */
    public Window(Panel panel, Scene scene) {
        setTitle("Sinkhole: Escape from the Abyss");
        setResizable(false);
        setScene(scene);  // Use the passed scene
        show();

        // Add listener to handle window focus loss
        focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {  // Window lost focus
                panel.getGame().windowFocusLost();
            }
        });

        // Set event handlers on the scene for keyboard input
        scene.setOnKeyPressed(event -> panel.getGame().getKeyboardInputs().handle(event));
        scene.setOnKeyReleased(event -> panel.getGame().getKeyboardInputs().handle(event));
    }
}
