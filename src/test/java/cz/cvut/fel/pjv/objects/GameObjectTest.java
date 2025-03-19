package cz.cvut.fel.pjv.objects;

import cz.cvut.fel.pjv.main.Game;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static cz.cvut.fel.pjv.utils.Constants.ObjectConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class GameObjectTest {
    private GameObject gameObject;
    private GraphicsContext mockGraphicsContext;

    @BeforeEach
    void setUp() {
        gameObject = new GameObject(0, 0, BARREL);
        Canvas canvas = new Canvas(100, 100); // Mock canvas for GraphicsContext
        mockGraphicsContext = canvas.getGraphicsContext2D();
    }

    @Test
    void testUpdateAnimation() {
        // Test animation update
        gameObject.setDoAnimation(true);
        int initialAnimIndex = gameObject.getAnimIndex();
        for (int i = 0; i < 100; i++) {
            gameObject.updateAnimation();
        }
        assertNotEquals(initialAnimIndex, gameObject.getAnimIndex());
    }

    @Test
    void testReset() {
        // Test resetting the game object
        gameObject.setDoAnimation(true);
        gameObject.updateAnimation();
        gameObject.reset();
        assertEquals(0, gameObject.getAnimIndex());
        assertTrue(gameObject.isActive());
    }

    @Test
    void testInitHitBox() {
        // Test hitbox initialization
        gameObject.initHitBox(20, 20);
        Rectangle hitBox = gameObject.getHitBox();
        assertEquals(0, hitBox.getX());
        assertEquals(0, hitBox.getY());
        assertEquals(20 * Game.SCALE, hitBox.getWidth());
        assertEquals(20 * Game.SCALE, hitBox.getHeight());
    }

    @Test
    void testDrawHitBox() {
        // Test drawing the hitbox (primarily to ensure no exceptions)
        gameObject.initHitBox(20, 20);
        gameObject.drawHitBox(mockGraphicsContext, 0);
        assertTrue(true);
    }

    @Test
    void testGetAndSetObjectType() {
        // Test getting and setting object type
        gameObject.setObjectType(BOX);
        assertEquals(BOX, gameObject.getObjectType());
    }

    @Test
    void testGetAndSetDoAnimation() {
        // Test getting and setting doAnimation
        gameObject.setDoAnimation(false);
        assertFalse(gameObject.isDoAnimation());
    }

    @Test
    void testGetAndSetActive() {
        // Test getting and setting active status
        gameObject.setActive(false);
        assertFalse(gameObject.isActive());
    }

    @Test
    void testGetHitBox() {
        // Test getting the hitbox
        gameObject.initHitBox(20, 20);
        Rectangle hitBox = gameObject.getHitBox();
        assertNotNull(hitBox);
    }

    @Test
    void testGetAnimIndex() {
        // Test getting the animation index
        assertEquals(0, gameObject.getAnimIndex());
    }

    @Test
    void testGetAndSetDrawOffsets() {
        // Test getting and setting draw offsets
        gameObject.xDrawOffset = 10;
        gameObject.yDrawOffset = 15;
        assertEquals(10, gameObject.getxDrawOffset());
        assertEquals(15, gameObject.getyDrawOffset());
    }
}
