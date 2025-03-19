package cz.cvut.fel.pjv.utils;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Utility class for loading and saving game resources such as images and levels.
 */
public class LoadSave {
    public static final String playerAtlas = "Player.png";
    public static final String levelAtlas = "tiles_final.png";
    public static final String menuButtons = "buttons.png";
    public static final String menuBackgroud = "menu_background.png";
    public static final String pauseBackground = "pause.png";
    public static final String soundButtons = "sound_buttons.png";
    public static final String urmButtons = "urm_buttons.png";
    public static final String volumeButtons = "volume_buttons.png";
    public static final String playingBackground = "background.png";
    public static final String dogSprite = "dog.png";
    public static final String catSprite = "cat.png";
    public static final String heathBar = "health_bar.png";
    public static final String levelCompleted = "level_completed.png";
    public static final String boxSprites = "box_sprites.png";
    public static final String potionSprites = "health_potion.png";
    public static final String trapAtlas = "trap.png";
    public static final String starAtlas = "star.png";
    public static final String gameOverScreen = "game_over.png";
    public static final String optionsBackground = "options_background.png";
    public static final String gameCompleted = "game_completed.png";

    /**
     * Loads a sprite atlas from the resources.
     *
     * @param fileName the name of the file to load
     * @return the loaded Image object, or null if the resource could not be loaded
     */
    public static Image getSpriteAtlas(String fileName) {
        Image image = null;
        InputStream is = null;

        try {
            is = LoadSave.class.getResourceAsStream("/" + fileName);
            if (is != null) {
                image = new Image(is);
            } else {
                System.err.println("Failed to load resource: " + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return image;
    }

    /**
     * Loads all level images from the "all_levels" directory in the resources.
     *
     * @return an array of Images representing the levels
     */
    public static Image[] getAllLevels() {
        URL url = LoadSave.class.getResource("/all_levels");
        File file = null;
        try {
            assert url != null;
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        File[] files = file.listFiles();
        assert files != null;
        File[] filessorted = new File[files.length];
        for (int i = 0; i < filessorted.length; i++) {
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals("" + (i + 1) + ".png")) {
                    filessorted[i] = files[j];
                }
            }
        }

        Image[] images = new Image[filessorted.length];
        for (int i = 0; i < images.length; i++) {
            try (FileInputStream fis = new FileInputStream(filessorted[i])) {
                images[i] = new Image(fis);
            } catch (IOException e) {
                System.err.println("Failed to load image: " + filessorted[i].getName());
                e.printStackTrace();
            }
        }

        return images;
    }
}
