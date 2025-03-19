package cz.cvut.fel.pjv.characters;

import cz.cvut.fel.pjv.gamestates.Playing;
import cz.cvut.fel.pjv.levels.Level;
import cz.cvut.fel.pjv.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import static cz.cvut.fel.pjv.utils.Constants.EnemyConstants.*;

/**
 * Manages the enemies in the game, including loading images, updating states, and rendering.
 */
public class EnemyManager {
    private Playing playing;
    private Image[][] dogArray, catArray;
    private ArrayList<Dog> dogs = new ArrayList<>();
    private ArrayList<Cat> cats = new ArrayList<>();
    public boolean isAnyActiveEnemy;

    /**
     * Constructs an EnemyManager instance and loads enemy images.
     * @param playing the current playing state
     */
    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImages();
    }

    /**
     * Loads enemies for the given level.
     * @param level the level containing enemies
     */
    public void loadEnemies(Level level) {
        dogs = level.getDogs();
        cats = level.getCats();
    }

    /**
     * Loads enemy images from resources.
     */
    private void loadEnemyImages() {
        dogArray = new Image[5][6];
        Image temp = LoadSave.getSpriteAtlas(LoadSave.dogSprite);
        for (int j = 0; j < dogArray.length; j++) {
            for (int i = 0; i < dogArray[j].length; i++) {
                dogArray[j][i] = new WritableImage(temp.getPixelReader(), i * ENEMY_DEFAULT_WIDTH, j * ENEMY_DEFAULT_HEIGHT, ENEMY_DEFAULT_WIDTH, ENEMY_DEFAULT_HEIGHT);
            }
        }

        catArray = new Image[5][6];
        Image temp2 = LoadSave.getSpriteAtlas(LoadSave.catSprite);
        for (int j = 0; j < catArray.length; j++) {
            for (int i = 0; i < catArray[j].length; i++) {
                catArray[j][i] = new WritableImage(temp2.getPixelReader(), i * ENEMY_DEFAULT_WIDTH, j * ENEMY_DEFAULT_HEIGHT, ENEMY_DEFAULT_WIDTH, ENEMY_DEFAULT_HEIGHT);
            }
        }
    }

    /**
     * Updates the state of all active enemies.
     * @param levelData the data of the current level
     * @param player the player interacting with enemies
     */
    public void update(int[][] levelData, Player player) {
        isAnyActiveEnemy = false;
        for (Dog d : dogs) {
            if (d.isActive()) {
                d.update(levelData, player);
                isAnyActiveEnemy = true;
            }
        }
        for (Cat c : cats) {
            if (c.isActive()) {
                c.update(levelData, player);
                isAnyActiveEnemy = true;
            }
        }
    }

    /**
     * Draws all active enemies on the canvas.
     * @param gc the graphics context to draw on
     * @param xLevelOffset the horizontal offset for level scrolling
     */
    public void draw(GraphicsContext gc, int xLevelOffset) {
        drawDog(gc, xLevelOffset);
        drawCat(gc, xLevelOffset);
    }

    /**
     * Draws all active cats on the canvas.
     * @param gc the graphics context to draw on
     * @param xLevelOffset the horizontal offset for level scrolling
     */
    private void drawCat(GraphicsContext gc, int xLevelOffset) {
        for (Cat c : cats) {
            if (c.isActive()) {
                gc.drawImage(catArray[c.getState()][c.getAnimIndex()], (int) (c.getHitBox().getX() - CAT_DRAW_OFFSET_X - xLevelOffset) + c.flipX(), (int) c.getHitBox().getY() - CAT_DRAW_OFFSET_Y, ENEMY_WIDTH * c.flipW(), ENEMY_HEIGHT);
//                c.drawHitBox(gc, xLevelOffset);
//                c.drawAttackBox(gc, xLevelOffset);
            }
        }
    }

    /**
     * Draws all active dogs on the canvas.
     * @param gc the graphics context to draw on
     * @param xLevelOffset the horizontal offset for level scrolling
     */
    private void drawDog(GraphicsContext gc, int xLevelOffset) {
        for (Dog d : dogs) {
            if (d.isActive()) {
                gc.drawImage(dogArray[d.getState()][d.getAnimIndex()], (int) (d.getHitBox().getX() - DOG_DRAW_OFFSET_X - xLevelOffset) + d.flipX(), (int) d.getHitBox().getY() - DOG_DRAW_OFFSET_Y, ENEMY_WIDTH * d.flipW(), ENEMY_HEIGHT);
//                d.drawHitBox(gc, xLevelOffset);
//                d.drawAttackBox(gc, xLevelOffset);
            }
        }
    }

    /**
     * Checks if any enemy is hit by the player's attack box.
     * @param attackBox the attack box of the player
     */
    public void checkEnemyHit(Rectangle attackBox) {
        for (Dog d : dogs) {
            if (d.isActive()) {
                if (attackBox.intersects(d.getHitBox().getX(), d.getHitBox().getY(), d.getHitBox().getWidth(), d.getHitBox().getHeight())) {
                    d.hurt(10);
                    return;
                }
            }
        }
        for (Cat c : cats) {
            if (c.isActive()) {
                if (attackBox.intersects(c.getHitBox().getX(), c.getHitBox().getY(), c.getHitBox().getWidth(), c.getHitBox().getHeight())) {
                    c.hurt(10);
                    return;
                }
            }
        }
    }

    /**
     * Resets all enemies to their initial state.
     */
    public void resetAllEnemies() {
        for (Dog d : dogs) {
            d.resetEnemy();
        }
        for (Cat c : cats) {
            c.resetEnemy();
        }
    }
}
