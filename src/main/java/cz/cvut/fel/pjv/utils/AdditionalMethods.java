package cz.cvut.fel.pjv.utils;

import cz.cvut.fel.pjv.characters.Cat;
import cz.cvut.fel.pjv.characters.Dog;
import cz.cvut.fel.pjv.main.Game;
import cz.cvut.fel.pjv.objects.GameContainer;
import cz.cvut.fel.pjv.objects.Star;
import cz.cvut.fel.pjv.objects.Potion;
import cz.cvut.fel.pjv.objects.Spike;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import static cz.cvut.fel.pjv.utils.Constants.EnemyConstants.*;
import static cz.cvut.fel.pjv.utils.Constants.ObjectConstants.*;

/**
 * Utility class providing additional methods for various game-related functionalities.
 */
public class AdditionalMethods {

    /**
     * Checks if a given rectangular area can move to the specified position within the level data.
     *
     * @param x the x-coordinate of the area
     * @param y the y-coordinate of the area
     * @param width the width of the area
     * @param height the height of the area
     * @param levelData the level data representing the game world
     * @return true if the area can move to the specified position, false otherwise
     */
    public static boolean canMoveHere(double x, double y, double width, double height, int[][] levelData) {
        if (!isSolid(x, y, levelData)) {
            if (!isSolid(x + width, y + height, levelData)) {
                if (!isSolid(x + width, y, levelData)) {
                    if (!isSolid(x, y + height, levelData)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isSolid(double x, double y, int[][] levelData) {
        int maxWidth = levelData[0].length * Game.TILES_SIZE;
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= Game.GAME_HEIGHT)
            return true;
        double xIndex = x / Game.TILES_SIZE;
        double yIndex = y / Game.TILES_SIZE;

        return isTilesSolid((int) xIndex, (int) yIndex, levelData);
    }

    /**
     * Checks if a tile is solid based on its value in the level data.
     *
     * @param xTile the x-coordinate of the tile
     * @param yTile the y-coordinate of the tile
     * @param levelData the level data representing the game world
     * @return true if the tile is solid, false otherwise
     */
    public static boolean isTilesSolid(int xTile, int yTile, int[][] levelData) {
        int value = levelData[yTile][xTile];
        return value >= 45 || value < 0 || value != 44;
    }

    /**
     * Gets the next position of an entity next to a wall based on its current speed.
     *
     * @param hitBox the hitbox of the entity
     * @param xSpeed the current speed of the entity
     * @return the next position of the entity next to a wall
     */
    public static double getEntityPosNextToWall(Rectangle hitBox, double xSpeed) {
        int currentTile = (int) (hitBox.getX() / Game.TILES_SIZE);
        if (xSpeed > 0) {
            int tileXPosition = currentTile * Game.TILES_SIZE;
            int xOffset = (int) (Game.TILES_SIZE - hitBox.getWidth());
            return tileXPosition + xOffset - 1;
        } else {
            return currentTile * Game.TILES_SIZE;
        }
    }

    /**
     * Gets the character position above or below based on its current air speed.
     *
     * @param hitBox the hitbox of the character
     * @param airSpeed the current air speed of the character
     * @return the character position above or below
     */
    public static double getCharacterPositionAboveBellow(Rectangle hitBox, double airSpeed) {
        int currentTile = (int) (hitBox.getY() / Game.TILES_SIZE);
        if (airSpeed > 0) {
            int tileYPosition = currentTile * Game.TILES_SIZE;
            int yOffset = (int) (Game.TILES_SIZE - hitBox.getHeight());
            return tileYPosition + yOffset - 1;
        } else {
            return currentTile * Game.TILES_SIZE;
        }
    }

    /**
     * Checks if an entity is on the floor based on its hitbox and the level data.
     *
     * @param hitBox the hitbox of the entity
     * @param levelData the level data representing the game world
     * @return true if the entity is on the floor, false otherwise
     */
    public static boolean isEntityOnFloor(Rectangle hitBox, int[][] levelData) {
        if (!isSolid(hitBox.getX(), hitBox.getY() + hitBox.getHeight() + 1, levelData))
            if (!isSolid(hitBox.getX() + hitBox.getWidth(), hitBox.getY() + hitBox.getHeight() + 1, levelData))
                return false;
        return true;
    }

    /**
     * Checks if the floor is solid at the specified position and speed.
     *
     * @param hitBox the hitbox of the entity
     * @param xSpeed the current speed of the entity
     * @param levelData the level data representing the game world
     * @return true if the floor is solid, false otherwise
     */
    public static boolean isFloor(Rectangle hitBox, double xSpeed, int[][] levelData) {
        if (xSpeed > 0) {
            return isSolid(hitBox.getX() + hitBox.getWidth() + xSpeed, hitBox.getY() + hitBox.getHeight() + 32, levelData);
        } else
            return isSolid(hitBox.getX() + xSpeed, hitBox.getY() + hitBox.getHeight() + 32, levelData);
    }

    /**
     * Checks if all tiles are clear between the specified start and end positions.
     *
     * @param xStart the starting x-coordinate
     * @param xEnd the ending x-coordinate
     * @param y the y-coordinate
     * @param lvlData the level data representing the game world
     * @return true if all tiles are clear, false otherwise
     */
    public static boolean isAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++)
            if (isTilesSolid(xStart + i, y, lvlData))
                return false;
        return true;
    }

    /**
     * Checks if all tiles are walkable between the specified start and end positions.
     *
     * @param xStart the starting x-coordinate
     * @param xEnd the ending x-coordinate
     * @param y the y-coordinate
     * @param levelData the level data representing the game world
     * @return true if all tiles are walkable, false otherwise
     */
    public static boolean isAllTilesWalkable(int xStart, int xEnd, int y, int[][] levelData) {
        if (isAllTilesClear(xStart, xEnd, y, levelData)) {
            for (int i = 0; i < xEnd - xStart; i++) {
                if (!isTilesSolid(xStart + i, y + 1, levelData))
                    return false;
            }
        }
        return true;
    }

    /**
     * Checks if the sight between the enemy and the player is clear.
     *
     * @param levelData the level data representing the game world
     * @param hitBoxEnemy the hitbox of the enemy
     * @param hitBoxPlayer the hitbox of the player
     * @param yTile the y-coordinate tile
     * @return true if the sight is clear, false otherwise
     */
    public static boolean isSightClear(int[][] levelData, Rectangle hitBoxEnemy, Rectangle hitBoxPlayer, int yTile) {
        int enemyXTile = (int) (hitBoxEnemy.getX() / Game.TILES_SIZE);
        int playerXTile = (int) (hitBoxPlayer.getX() / Game.TILES_SIZE);

        if (enemyXTile > playerXTile) {
            return isAllTilesWalkable(playerXTile, enemyXTile, yTile, levelData);
        } else {
            return isAllTilesWalkable(enemyXTile, playerXTile, yTile, levelData);
        }
    }

    /**
     * Loads the level data from an image.
     *
     * @param image the image representing the level
     * @return a 2D array of integers representing the level data
     */
    public static int[][] loadLevelData(Image image) {
        int[][] levelData = new int[(int) image.getHeight()][(int) image.getWidth()];

        for (int j = 0; j < (int) image.getHeight(); j++) {
            for (int i = 0; i < (int) image.getWidth(); i++) {
                Color color = image.getPixelReader().getColor(i, j);
                int value = (int) Math.round(color.getRed() * 255);
                if (value >= 45)
                    value = 0;
                levelData[j][i] = value;
            }
        }

        return levelData;
    }

    /**
     * Loads the dog enemies from an image.
     *
     * @param image the image representing the level
     * @return an ArrayList of Dog objects
     */
    public static ArrayList<Dog> loadDogs(Image image) {
        ArrayList<Dog> list = new ArrayList<>();
        for (int j = 0; j < (int) image.getHeight(); j++) {
            for (int i = 0; i < (int) image.getWidth(); i++) {
                Color color = image.getPixelReader().getColor(i, j);
                int value = (int) Math.round(color.getGreen() * 255);
                if (value == DOG)
                    list.add(new Dog(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        }
        return list;
    }

    /**
     * Loads the cat enemies from an image.
     *
     * @param image the image representing the level
     * @return an ArrayList of Cat objects
     */
    public static ArrayList<Cat> loadCats(Image image) {
        ArrayList<Cat> list = new ArrayList<>();
        for (int j = 0; j < (int) image.getHeight(); j++) {
            for (int i = 0; i < (int) image.getWidth(); i++) {
                Color color = image.getPixelReader().getColor(i, j);
                int value = (int) Math.round(color.getGreen() * 255);
                if (value == CAT)
                    list.add(new Cat(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        }
        return list;
    }

    /**
     * Gets the player's spawn position from an image.
     *
     * @param image the image representing the level
     * @return a Point2D representing the player's spawn position
     */
    public static Point2D getPlayerSpawnPosition(Image image) {
        for (int j = 0; j < (int) image.getHeight(); j++) {
            for (int i = 0; i < (int) image.getWidth(); i++) {
                Color color = image.getPixelReader().getColor(i, j);
                int value = (int) Math.round(color.getGreen() * 255);
                if (value == 255)
                    return new Point2D(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
            }
        }
        return new Point2D(1 * Game.TILES_SIZE, 1 * Game.TILES_SIZE);
    }

    /**
     * Loads the health potions from an image.
     *
     * @param image the image representing the level
     * @return an ArrayList of Potion objects
     */
    public static ArrayList<Potion> loadPotions(Image image) {
        ArrayList<Potion> list = new ArrayList<>();
        for (int j = 0; j < (int) image.getHeight(); j++) {
            for (int i = 0; i < (int) image.getWidth(); i++) {
                Color color = image.getPixelReader().getColor(i, j);
                int value = (int) Math.round(color.getBlue() * 255);
                if (value == HEALTH_POTION)
                    list.add(new Potion(i * Game.TILES_SIZE, j * Game.TILES_SIZE, HEALTH_POTION));
            }
        }
        return list;
    }

    /**
     * Loads the game containers from an image.
     *
     * @param image the image representing the level
     * @return an ArrayList of GameContainer objects
     */
    public static ArrayList<GameContainer> loadContainers(Image image) {
        ArrayList<GameContainer> list = new ArrayList<>();
        for (int j = 0; j < (int) image.getHeight(); j++) {
            for (int i = 0; i < (int) image.getWidth(); i++) {
                Color color = image.getPixelReader().getColor(i, j);
                int value = (int) Math.round(color.getBlue() * 255);
                if (value == BARREL || value == BOX)
                    list.add(new GameContainer(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
            }
        }
        return list;
    }

    /**
     * Loads the spikes from an image.
     *
     * @param image the image representing the level
     * @return an ArrayList of Spike objects
     */
    public static ArrayList<Spike> loadSpikes(Image image) {
        ArrayList<Spike> list = new ArrayList<>();
        for (int j = 0; j < (int) image.getHeight(); j++) {
            for (int i = 0; i < (int) image.getWidth(); i++) {
                Color color = image.getPixelReader().getColor(i, j);
                int value = (int) Math.round(color.getBlue() * 255);
                if (value == SPIKE)
                    list.add(new Spike(i * Game.TILES_SIZE, j * Game.TILES_SIZE, SPIKE));
            }
        }
        return list;
    }

    /**
     * Loads the stars from an image.
     *
     * @param image the image representing the level
     * @return an ArrayList of Star objects
     */
    public static ArrayList<Star> loadStars(Image image) {
        ArrayList<Star> list = new ArrayList<>();
        for (int j = 0; j < (int) image.getHeight(); j++) {
            for (int i = 0; i < (int) image.getWidth(); i++) {
                Color color = image.getPixelReader().getColor(i, j);
                int value = (int) Math.round(color.getBlue() * 255);
                if (value == STAR)
                    list.add(new Star(i * Game.TILES_SIZE, j * Game.TILES_SIZE, STAR));
            }
        }
        return list;
    }
}