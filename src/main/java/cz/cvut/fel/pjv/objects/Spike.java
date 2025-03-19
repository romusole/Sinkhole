package cz.cvut.fel.pjv.objects;

import cz.cvut.fel.pjv.main.Game;

/**
 * Represents a spike object in the game that can harm the player.
 */
public class Spike extends GameObject {

    /**
     * Constructs a Spike object with the specified position and type.
     *
     * @param x the x-coordinate of the spike
     * @param y the y-coordinate of the spike
     * @param objectType the type of the spike
     */
    public Spike(int x, int y, int objectType) {
        super(x, y, objectType);
        initHitBox(32, 11);
        xDrawOffset = 0;
        yDrawOffset = (int) (22 * Game.SCALE);
        hitBox.setY(hitBox.getY() + yDrawOffset);
    }
}
