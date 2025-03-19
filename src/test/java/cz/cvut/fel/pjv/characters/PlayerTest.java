package cz.cvut.fel.pjv.characters;

import cz.cvut.fel.pjv.gamestates.Playing;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player;
    private Playing playing;
    private int[][] mockLevelData;
    private GraphicsContext mockGraphicsContext;

    @BeforeEach
    void setUp() {
        player = new Player(0, 0, 50, 50, playing);
        mockLevelData = new int[10][10]; // Example level data
        Canvas canvas = new Canvas(100, 100); // Mock canvas for GraphicsContext
        mockGraphicsContext = canvas.getGraphicsContext2D();
    }

    @Test
    void testInitialHealth() {
        assertEquals(100, player.getCurrentHealth());
    }

    @Test
    void testSetSpawn() {
        Point2D spawnPoint = new Point2D(100, 200);
        player.setSpawn(spawnPoint);
        assertEquals(100, player.getHitBox().getX());
        assertEquals(200, player.getHitBox().getY());
    }

    @Test
    void testUpdateHealth() {
        player.changeHealth(-10);
        assertEquals(90, player.getCurrentHealth());

        player.changeHealth(-100);
        assertEquals(0, player.getCurrentHealth());

        player.changeHealth(150);
        assertEquals(100, player.getCurrentHealth());
    }


    @Test
    void testAttack() {
        player.setAttacking(true);
        player.update();
        assertTrue(player.isAttacking());
    }

    @Test
    void testRender() {
        player.render(mockGraphicsContext, 0);
        // Since this is a visual test, we mainly ensure no exceptions are thrown during rendering
        assertTrue(true);
    }

    // Add more tests as needed to cover other functionalities
}