package cz.cvut.fel.pjv.objects;

import cz.cvut.fel.pjv.main.Game;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static cz.cvut.fel.pjv.utils.Constants.*;
import static cz.cvut.fel.pjv.utils.Constants.ObjectConstants.*;

/**
 * Represents a generic game object with common properties and methods.
 */
public class GameObject {

    protected int x, y, objectType;
    protected Rectangle hitBox;
    protected boolean doAnimation, active = true;
    protected int animTick, animIndex;
    protected int xDrawOffset, yDrawOffset;

    /**
     * Constructs a GameObject with the specified position and type.
     *
     * @param x the x-coordinate of the object
     * @param y the y-coordinate of the object
     * @param objectType the type of the object
     */
    public GameObject(int x, int y, int objectType) {
        this.x = x;
        this.y = y;
        this.objectType = objectType;
    }

    /**
     * Updates the animation state of the object.
     */
    protected void updateAnimation() {
        animTick++;
        if (animTick >= ANIMATION_SPEED) {
            animTick = 0;
            animIndex++;
            if (animIndex >= getSpriteAmount(objectType)) {
                animIndex = 0;
                if (objectType == BARREL || objectType == BOX) {
                    doAnimation = false;
                    active = false;
                }
            }
        }
    }

    /**
     * Resets the object's animation and state.
     */
    public void reset() {
        animTick = 0;
        animIndex = 0;
        active = true;
        if (objectType == BARREL || objectType == BOX) {
            doAnimation = false;
        } else {
            doAnimation = true;
        }
    }

    /**
     * Initializes the hitbox for the object.
     *
     * @param width the width of the hitbox
     * @param height the height of the hitbox
     */
    protected void initHitBox(double width, double height) {
        hitBox = new Rectangle(x, y, width * Game.SCALE, height * Game.SCALE);
    }

    /**
     * Draws the hitbox of the object for debugging purposes.
     *
     * @param gc the GraphicsContext to use for drawing
     * @param xlevelOffset the x-offset for the level
     */
    protected void drawHitBox(GraphicsContext gc, int xlevelOffset) {
        Color translucent = new Color(0.5, 0.7, 0.7, 0.5);
        gc.setStroke(translucent);
        gc.strokeRect(hitBox.getX() - xlevelOffset, hitBox.getY(), hitBox.getWidth(), hitBox.getHeight());
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }

    public void setDoAnimation(boolean doAnimation) {
        this.doAnimation = doAnimation;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public int getAnimIndex() {
        return animIndex;
    }

    public boolean isDoAnimation() {
        return doAnimation;
    }
}
