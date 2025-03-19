package cz.cvut.fel.pjv.levels;

import cz.cvut.fel.pjv.main.Game;
import cz.cvut.fel.pjv.utils.LoadSave;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.ArrayList;

/**
 * Manages the levels in the game, including loading, drawing, and updating levels.
 */
public class LevelManager {
    private Game game;
    private Image[] levelSprite;
    private ArrayList<Level> levels;
    private int levelIndex = 0;

    /**
     * Constructs a LevelManager with the specified game instance.
     *
     * @param game the game instance associated with this manager
     */
    public LevelManager(Game game) {
        this.game = game;
        importTilesSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    /**
     * Builds all levels by loading images from resources.
     */
    private void buildAllLevels() {
        Image[] allLevels = LoadSave.getAllLevels();
        for (Image img : allLevels) {
            levels.add(new Level(img));
        }
    }

    /**
     * Loads the next level in the sequence. If the current level is the last one, it loops back to the first level.
     */
    public void loadNextLevel() {
        levelIndex++;
        if (levelIndex >= levels.size()) {
            levelIndex = 0;
        }

        Level nextLevel = levels.get(levelIndex);
        game.getPlaying().getEnemyManager().loadEnemies(nextLevel);
        game.getPlaying().getPlayer().loadLevelData(nextLevel.getLevelData());
        game.getPlaying().setMaxLevelOffset(nextLevel.getLevelOffset());
        game.getPlaying().getObjectManager().loadObjects(nextLevel);
    }

    /**
     * Imports tile sprites from the level atlas.
     */
    private void importTilesSprites() {
        Image image = LoadSave.getSpriteAtlas(LoadSave.levelAtlas);
        levelSprite = new Image[81];
        int width = (int) image.getWidth() / 9;
        int height = (int) image.getHeight() / 4;

        for (int j = 0; j < 4; j++) { // j - height
            for (int i = 0; i < 9; i++) { // i - width
                int index = j * 9 + i;
                levelSprite[index] = new WritableImage(image.getPixelReader(), i * 32, j * 32, 32, 32);
            }
        }
    }

    /**
     * Draws the current level on the provided GraphicsContext.
     *
     * @param gc the GraphicsContext to draw on
     * @param levelOffset the x-offset for the level
     */
    public void draw(GraphicsContext gc, int levelOffset) {
        for (int j = 0; j < Game.TILES_IN_HEIGHT; j++) {
            for (int i = 0; i < levels.get(levelIndex).getLevelData()[0].length; i++) {
                int index = levels.get(levelIndex).getSpriteIndex(i, j);
                gc.drawImage(levelSprite[index], i * Game.TILES_SIZE - levelOffset, j * Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE);
            }
        }
    }

    public Level getCurrentLevel() {
        return levels.get(levelIndex);
    }

    public int getAmountOfLevels() {
        return levels.size();
    }

    public int getLevelIndex() {
        return levelIndex;
    }
}
