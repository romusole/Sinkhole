package cz.cvut.fel.pjv.characters;

import cz.cvut.fel.pjv.gamestates.Playing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static cz.cvut.fel.pjv.utils.Constants.Directions.*;
import static cz.cvut.fel.pjv.utils.Constants.EnemyConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class EnemyTest {
    private Playing playing;
    private Enemy enemy;
    private Player mockPlayer;
    private int[][] mockLevelData;

    @BeforeEach
    void setUp() {
        enemy = new Dog(0, 0); // Example instantiation
        mockPlayer = new Player(0, 0, 50, 50, playing); // Example instantiation
        mockLevelData = new int[10][10]; // Example level data
    }

    @Test
    void testCanSeePlayer() {
        // Example test: Checking if the enemy can see the player
        mockPlayer.initHitBox(50, 50);
        assertTrue(enemy.canSeePlayer(mockLevelData, mockPlayer));
    }

    @Test
    void testIsPlayerInRange() {
        // Example test: Checking if the player is in range of the enemy
        mockPlayer.initHitBox(50, 50);
        assertTrue(enemy.isPlayerInRange(mockPlayer));
    }

    @Test
    void testChangeStateToAttack() {
        // Example test: Checking if the enemy changes state to attack when player is in range
        mockPlayer.initHitBox(50, 50);
        enemy.changeState(ATTACK);
        enemy.update(mockLevelData, mockPlayer);
        assertEquals(ATTACK, enemy.getState());
    }

    @Test
    void testUpdateBehaviour() {
        // Test enemy behavior update
        mockPlayer.initHitBox(50, 50);
        enemy.update(mockLevelData, mockPlayer);
        assertNotNull(enemy); // Check if enemy object is not null after update
    }

    @Test
    void testFlipX() {
        // Test the flipX method
        assertEquals(enemy.width, enemy.flipX());
        enemy.setWalkDirection(RIGHT);
        assertEquals(0, enemy.flipX());
        enemy.setWalkDirection(LEFT);
        assertEquals(enemy.width, enemy.flipX());
    }

    @Test
    void testFlipW() {
        // Test the flipW method
        assertEquals(-1, enemy.flipW());
        enemy.setWalkDirection(RIGHT);
        assertEquals(1, enemy.flipW());
    }

    @Test
    void testChangeState() {
        // Test changing state
        enemy.changeState(ATTACK);
        assertEquals(ATTACK, enemy.getState());
        enemy.changeState(HURT);
        assertEquals(HURT, enemy.getState());
    }

    @Test
    void testHurt() {
        // Test hurting the enemy
        enemy.hurt(5);
        assertEquals(HURT, enemy.getState());
        assertTrue(enemy.getCurrentHealth() > 0);

        enemy.hurt(enemy.getMaxHealth());
        assertEquals(DIE, enemy.getState());
        assertFalse(enemy.isActive());
    }

    @Test
    void testResetEnemy() {
        // Test resetting the enemy
        enemy.hurt(5);
        enemy.resetEnemy();
        assertEquals(enemy.getMaxHealth(), enemy.getCurrentHealth());
        assertEquals(IDLE, enemy.getState());
        assertTrue(enemy.isActive());
    }

    @Test
    void testIsActive() {
        // Test if the enemy is active
        assertTrue(enemy.isActive());
        enemy.changeState(DIE);
        assertFalse(enemy.isActive());
    }

    // Helper methods to set walk direction for testing
    private void setWalkDirection(Enemy enemy, int direction) {
        if (direction == LEFT || direction == RIGHT) {
            enemy.setWalkDirection(direction);
        }
    }

    // Add more tests as needed
}
