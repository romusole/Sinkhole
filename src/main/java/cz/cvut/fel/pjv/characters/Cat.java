package cz.cvut.fel.pjv.characters;

import static cz.cvut.fel.pjv.utils.Constants.EnemyConstants.*;

/**
 * The Cat class represents an enemy of type Cat in the game.
 * It extends the abstract Enemy class and initializes specific properties for the Cat.
 */
public class Cat extends Enemy {

    /**
     * Constructs a new Cat instance with the specified position.
     *
     * @param x the x-coordinate of the Cat's position
     * @param y the y-coordinate of the Cat's position
     */
    public Cat(double x, double y) {
        super(x, y, ENEMY_WIDTH, ENEMY_HEIGHT, CAT);
        initHitBox(23, 19);
    }
}
