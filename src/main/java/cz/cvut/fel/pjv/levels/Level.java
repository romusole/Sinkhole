package cz.cvut.fel.pjv.levels;

import cz.cvut.fel.pjv.characters.Cat;
import cz.cvut.fel.pjv.characters.Dog;
import cz.cvut.fel.pjv.main.Game;
import cz.cvut.fel.pjv.objects.GameContainer;
import cz.cvut.fel.pjv.objects.Star;
import cz.cvut.fel.pjv.objects.Potion;
import cz.cvut.fel.pjv.objects.Spike;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import java.util.ArrayList;

import static cz.cvut.fel.pjv.utils.AdditionalMethods.*;

/**
 * Represents a game level, containing all relevant data such as enemies, objects, and the layout.
 */
public class Level {
    private Image image;
    private int[][] levelData;
    private ArrayList<Dog> dogs;
    private ArrayList<Cat> cats;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Star> stars;
    private int levelTilesWide;
    private int maxTilesOffset;
    private int maxLevelOffset;
    private Point2D playerSpawn;

    /**
     * Constructs a Level object from the specified image.
     *
     * @param image the image representing the level
     */
    public Level(Image image) {
        this.image = image;
        createLevelData();
        createEnemies();
        createPotions();
        createContainers();
        createSpikes();
        createStars();
        calculateOffset();
        calculatePlayerSpawn();
    }

    /**
     * Loads the stars for this level from the image.
     */
    private void createStars() {
        stars = loadStars(image);
    }

    /**
     * Loads the spikes for this level from the image.
     */
    private void createSpikes() {
        spikes = loadSpikes(image);
    }

    /**
     * Loads the potions for this level from the image.
     */
    private void createPotions() {
        potions = loadPotions(image);
    }

    /**
     * Loads the containers for this level from the image.
     */
    private void createContainers() {
        containers = loadContainers(image);
    }

    /**
     * Calculates the player's spawn position based on the image.
     */
    private void calculatePlayerSpawn() {
        playerSpawn = getPlayerSpawnPosition(image);
    }

    /**
     * Calculates the maximum level offset for scrolling based on the level's width.
     */
    private void calculateOffset() {
        levelTilesWide = (int) image.getWidth();
        maxTilesOffset = levelTilesWide - Game.TILES_IN_WIDTH;
        maxLevelOffset = Game.TILES_SIZE * maxTilesOffset;
    }

    /**
     * Loads the enemies for this level from the image.
     */
    private void createEnemies() {
        dogs = loadDogs(image);
        cats = loadCats(image);
    }

    /**
     * Creates the level data (layout) from the image.
     */
    private void createLevelData() {
        levelData = loadLevelData(image);
    }

    /**
     * Gets the sprite index for the specified tile coordinates.
     *
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     * @return the sprite index for the specified tile
     */
    public int getSpriteIndex(int x, int y) {
        return levelData[y][x];
    }

    public int[][] getLevelData() {
        return levelData;
    }

    public int getLevelOffset() {
        return maxLevelOffset;
    }

    public ArrayList<Dog> getDogs() {
        return dogs;
    }

    public ArrayList<Cat> getCats() {
        return cats;
    }

    public Point2D getPlayerSpawn() {
        return playerSpawn;
    }

    public ArrayList<Potion> getPotions() {
        return potions;
    }

    public ArrayList<GameContainer> getContainers() {
        return containers;
    }

    public ArrayList<Spike> getSpikes() {
        return spikes;
    }

    public ArrayList<Star> getStars() {
        return stars;
    }
}
