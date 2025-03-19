package cz.cvut.fel.pjv.objects;

import cz.cvut.fel.pjv.main.Game;

/**
 * Represents a potion object in the game that can hover and animate.
 */
public class Potion extends GameObject {
    private double hoverOffset;
    private int maxHoverOffset, hoverDirection = 1;

    /**
     * Constructs a Potion object with the specified position and type.
     *
     * @param x the x-coordinate of the potion
     * @param y the y-coordinate of the potion
     * @param objectType the type of the potion
     */
    public Potion(int x, int y, int objectType) {
        super(x, y, objectType);
        doAnimation = true;
        initHitBox(7, 14);
        xDrawOffset = (int) (5 * Game.SCALE);
        yDrawOffset = (int) (2 * Game.SCALE);

        maxHoverOffset = (int) (10 * Game.SCALE);
    }

    /**
     * Updates the state of the potion, including its animation and hover effect.
     */
    public void update() {
        updateAnimation();
        updateHover();
    }

    /**
     * Updates the hover effect of the potion, making it move up and down.
     */
    private void updateHover() {
        hoverOffset += (0.075d * Game.SCALE * hoverDirection);
        if (hoverOffset >= maxHoverOffset) {
            hoverDirection = -1;
        } else if (hoverOffset < 0) {
            hoverDirection = 1;
        }

        hitBox.setY(y + hoverOffset);
    }
}
