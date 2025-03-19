package cz.cvut.fel.pjv.characters;

import cz.cvut.fel.pjv.main.Game;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The Character class represents an abstract base class for characters in the game.
 * It defines common properties and methods used by all character types.
 */
public abstract class Character {
    protected double x, y;
    protected int width, height;
    protected Rectangle hitBox;
    protected Rectangle attackBox;
    protected int animTick, animIndex;
    protected int state;
    protected double airSpeed;
    protected boolean inAir = false;
    protected int maxHealth;
    protected int currentHealth;
    protected double walkSpeed;

    /**
     * Constructs a new Character instance with the specified position and size.
     *
     * @param x the x-coordinate of the character's position
     * @param y the y-coordinate of the character's position
     * @param width the width of the character
     * @param height the height of the character
     */
    public Character(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Initializes the hitbox for the character.
     *
     * @param width the width of the hitbox
     * @param height the height of the hitbox
     */
    protected void initHitBox(double width, double height) {
        hitBox = new Rectangle(x, y, width * Game.SCALE, height * Game.SCALE);
    }

    /**
     * Draws the hitbox of the character for debugging purposes.
     *
     * @param gc the GraphicsContext to draw on
     * @param xlevelOffset the horizontal offset of the level
     */
    protected void drawHitBox(GraphicsContext gc, int xlevelOffset) {
        Color translucent = new Color(0.743233, 0.5, 0.6, 0.5);
        gc.setStroke(translucent);
        gc.strokeRect(hitBox.getX() - xlevelOffset, hitBox.getY(), hitBox.getWidth(), hitBox.getHeight());
    }

    /**
     * Draws the attack box of the character for debugging purposes.
     *
     * @param gc the GraphicsContext to draw on
     * @param levelOffset the horizontal offset of the level
     */
    protected void drawAttackBox(GraphicsContext gc, int levelOffset) {
        gc.setStroke(Color.PINK);
        gc.strokeRect(attackBox.getX() - levelOffset, attackBox.getY(), attackBox.getWidth(), attackBox.getHeight());
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public int getState() {
        return state;
    }

    public int getAnimIndex() {
        return animIndex;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isInAir() {
        return inAir;
    }
}