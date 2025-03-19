package cz.cvut.fel.pjv.characters;

import static cz.cvut.fel.pjv.utils.Constants.EnemyConstants.*;

/**
 * The Dog class represents a specific type of enemy in the game.
 * It extends the Enemy class and initializes specific properties for the Dog enemy.
 */
public class Dog extends Enemy {

    /**
     * Constructs a new Dog instance with the specified position.
     *
     * @param x the x-coordinate of the dog's position
     * @param y the y-coordinate of the dog's position
     */
    public Dog(double x, double y) {
        super(x, y, ENEMY_WIDTH, ENEMY_HEIGHT, DOG);
        initHitBox(30, 30);
    }
}
