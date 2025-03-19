package cz.cvut.fel.pjv.objects;

import cz.cvut.fel.pjv.main.Game;

import static cz.cvut.fel.pjv.utils.Constants.ObjectConstants.*;

/**
 * Represents a game container object such as a box or barrel.
 */
public class GameContainer extends GameObject {

    /**
     * Constructs a GameContainer object with the specified position and type.
     *
     * @param x the x-coordinate of the container
     * @param y the y-coordinate of the container
     * @param objectType the type of the container (e.g., box or barrel)
     */
    public GameContainer(int x, int y, int objectType) {
        super(x, y, objectType);
        createHitBox();
    }

    /**
     * Creates the hitbox for the container based on its type.
     */
    private void createHitBox() {
        if (objectType == BOX) {
            initHitBox(25, 18);
            xDrawOffset = (int) (7 * Game.SCALE);
            yDrawOffset = (int) (12 * Game.SCALE);
        } else {
            initHitBox(23, 25);
            xDrawOffset = (int) (8 * Game.SCALE);
            yDrawOffset = (int) (5 * Game.SCALE);
        }

        hitBox.setY(hitBox.getY() + yDrawOffset + (int) (2 * Game.SCALE));
        hitBox.setX(hitBox.getX() + xDrawOffset / 2);
    }

    /**
     * Updates the container, particularly its animation if applicable.
     */
    public void update() {
        if (doAnimation) {
            updateAnimation();
        }
    }
}
