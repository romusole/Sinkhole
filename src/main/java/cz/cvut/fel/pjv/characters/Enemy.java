package cz.cvut.fel.pjv.characters;

import cz.cvut.fel.pjv.main.Game;
import javafx.scene.shape.Rectangle;

import static cz.cvut.fel.pjv.utils.Constants.EnemyConstants.*;
import static cz.cvut.fel.pjv.utils.AdditionalMethods.*;
import static cz.cvut.fel.pjv.utils.Constants.Directions.*;
import static cz.cvut.fel.pjv.utils.Constants.*;

/**
 * The Enemy class is an abstract representation of a generic enemy character in the game.
 * It handles common behaviors and properties shared among all enemies.
 */
public abstract class Enemy extends Character {
    private int enemyType;
    private boolean firstUpdate = true;
    private int walkDirection = LEFT;
    private int yTile;
    private double attackDistance = 1 * Game.TILES_SIZE;
    private boolean active = true;
    private boolean attackChecked;

    /**
     * Constructs a new Enemy instance with the specified position, dimensions, and enemy type.
     *
     * @param x the x-coordinate of the enemy's position
     * @param y the y-coordinate of the enemy's position
     * @param width the width of the enemy
     * @param height the height of the enemy
     * @param enemyType the type of the enemy
     */
    public Enemy(double x, double y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        this.state = IDLE;
        this.walkSpeed = 0.5d * Game.SCALE;
        initAttackBox();
        maxHealth = getMaxEnemyHealth(enemyType);
        currentHealth = maxHealth;
    }

    private void initAttackBox() {
        attackBox = new Rectangle(x, y , 20 * Game.SCALE, 20 * Game.SCALE );
    }

    /**
     * Updates the enemy's behavior, animation, and attack box.
     *
     * @param levelData the level data for collision and environment interaction
     * @param player the player instance for interactions
     */
    public void update(int [][] levelData, Player player) {
        updateBehaviour(levelData, player);
        updateAnimation();
        updateAttackBox();
    }

    private void updateAnimation() {
        animTick++;
        if (animTick >= ANIMATION_SPEED) {
            animTick = 0;
            animIndex++;
            if (animIndex >= getSpriteAmount(enemyType, state)) {
                animIndex = 0;
                switch (state) {
                    case ATTACK, HURT -> state = IDLE;
                    case DIE -> active = false;
                }
            }
        }
    }

    private void updateAttackBox() {
        if(walkDirection == RIGHT) {
            attackBox.setX(hitBox.getX() + hitBox.getWidth() + (int) (7 * Game.SCALE));
        } else if (walkDirection == LEFT) {
            attackBox.setX(hitBox.getX() - hitBox.getWidth() + (int) (10 * Game.SCALE));
        }
        if (enemyType == DOG)
            attackBox.setY(hitBox.getY() + (int) (10 * Game.SCALE));
        else
            attackBox.setY(hitBox.getY() - (int) (5 * Game.SCALE));
    }

    /**
     * Updates the enemy's behavior based on its current state, environment, and player interactions.
     *
     * @param levelData the level data for collision and environment interaction
     * @param player the player instance for interactions
     */
    private void updateBehaviour(int[][] levelData, Player player) {
        // Check if this is the first update and if the enemy is in the air
        if (firstUpdate) {
            if (!isEntityOnFloor(hitBox, levelData)) {
                inAir = true;
            }
            firstUpdate = false;
        }

        // Handle behavior when the enemy is in the air
        if (inAir) {
            // Check if the enemy can move down (falling)
            if (canMoveHere(hitBox.getX(), hitBox.getY() + airSpeed, hitBox.getWidth(), hitBox.getWidth(), levelData)) {
                hitBox.setY(hitBox.getY() + airSpeed);
                airSpeed += GRAVITY;
            } else {
                // Enemy has landed
                inAir = false;
                hitBox.setY(getCharacterPositionAboveBellow(hitBox, airSpeed));
                yTile = (int) (hitBox.getY() / Game.TILES_SIZE);
            }
        } else {
            // Handle behavior based on the enemy's state
            switch (state) {
                case IDLE -> changeState(RUN); // Change state to RUN if currently IDLE
                case RUN -> {
                    // Check if the enemy can see the player
                    if (canSeePlayer(levelData, player)) {
                        turnTowardsPlayer(player);
                    }
                    // Check if the player is close enough to attack
                    if (isPlayerCloseForAttack(player)) {
                        changeState(ATTACK);
                    }

                    double xSpeed = 0;

                    // Set movement speed based on the walking direction
                    if (walkDirection == LEFT) {
                        xSpeed = -walkSpeed;
                    } else {
                        xSpeed = walkSpeed;
                    }

                    // Check if the enemy can move to the new position
                    if (canMoveHere(hitBox.getX() + xSpeed, hitBox.getY(), hitBox.getWidth(), hitBox.getHeight(), levelData)) {
                        // Check if there is a floor at the new position
                        if (isFloor(getHitBox(), xSpeed, levelData)) {
                            hitBox.setX(hitBox.getX() + xSpeed);
                            return;
                        }
                    }

                    // Change walking direction if movement is not possible
                    changeWalkDir();
                }
                case ATTACK -> {
                    // Reset attack check at the beginning of the attack animation
                    if (animIndex == 0) {
                        attackChecked = false;
                    }
                    // Check if the attack can hit the player
                    if (animIndex == 2 && !attackChecked) {
                        checkEnemyHit(player);
                    }
                }
            }
        }
    }


    /**
     * Returns the x-coordinate for flipping the enemy sprite.
     *
     * @return the x-coordinate for flipping the sprite
     */
    public int flipX() {
        if (walkDirection == RIGHT) {
            return 0;
        }
        return width;
    }

    /**
     * Returns the width for flipping the enemy sprite.
     *
     * @return the width for flipping the sprite
     */
    public int flipW() {
        if (walkDirection == RIGHT) {
            return 1;
        }
        return -1;
    }

    private void turnTowardsPlayer(Player player) {
        if (player.getHitBox().getX() > hitBox.getX()) {
            walkDirection = RIGHT;
        } else  {
            walkDirection = LEFT;
        }
    }

    /**
     * Checks if the enemy can see the player.
     *
     * @param levelData the level data for collision and environment interaction
     * @param player the player instance for interactions
     * @return true if the enemy can see the player, false otherwise
     */
    public boolean canSeePlayer(int[][] levelData, Player player) {
        int playerTileY = (int) (player.getHitBox().getY() / Game.TILES_SIZE);
        if (playerTileY == yTile) {
            if (isPlayerInRange(player)) {
                if (isSightClear(levelData, hitBox, player.getHitBox(), yTile)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the player is within the enemy's range.
     *
     * @param player the player instance for interactions
     * @return true if the player is within range, false otherwise
     */
    public boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitBox.getX() - hitBox.getX());
        return absValue <= attackDistance * 5;
    }

    /**
     * Checks if player is in range to attack player
     *
     * @param player
     * @return true if the player is close to attack, false otherwise
     */
    private boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitBox.getX() - hitBox.getX());
        return absValue <= attackDistance;
    }

    /**
     * CEnemy changed direction to opposite when reaches border
     */
    private void changeWalkDir() {
        if (walkDirection == LEFT) {
            walkDirection = RIGHT;
        } else {
            walkDirection = LEFT;
        }
    }

    /**
     * Changes the enemy's state to the specified state.
     *
     * @param enemyState the new state for the enemy
     */
    public void changeState (int enemyState) {
        this.state = enemyState;
        animTick = 0;
        animIndex = 0;
    }

    /**
     * Reduces the enemy's health by the specified damage amount.
     *
     * @param damage the amount of damage to inflict on the enemy
     */
    public void hurt(int damage) {
        currentHealth -= damage;
        if (currentHealth <= 0) {
            changeState(DIE);
        } else {
            changeState(HURT);
        }
    }

    /**
     * Checks if enemy hit player
     *
     * @param player
     */
    private void checkEnemyHit(Player player) {
        if(attackBox.intersects(player.getHitBox().getX(), player.getHitBox().getY(), player.getHitBox().getWidth(), player.getHitBox().getHeight())) {
            player.changeHealth(-getEnemyDamage(enemyType));
        }
        attackChecked = true;
    }

    /**
     * Resets the enemy's state and position to their initial values.
     */
    public void resetEnemy() {
        hitBox.setX(x);
        hitBox.setY(y);
        firstUpdate = true;
        currentHealth = maxHealth;
        changeState(IDLE);
        active = true;
        airSpeed = 0;
    }

    public boolean isActive() {
        return active;
    }

    public void setWalkDirection(int walkDirection) {
        this.walkDirection = walkDirection;
    }
}
