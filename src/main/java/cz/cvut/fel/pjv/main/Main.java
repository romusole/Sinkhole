package cz.cvut.fel.pjv.main;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    private Game game; // Store the Game instance

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        game = new Game(); // Create the Game instance
    }
}
