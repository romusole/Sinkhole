package cz.cvut.fel.pjv.characters;

import cz.cvut.fel.pjv.audio.AudioPlayer;
import cz.cvut.fel.pjv.gamestates.Playing;
import cz.cvut.fel.pjv.main.Game;
import cz.cvut.fel.pjv.utils.LoadSave;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static cz.cvut.fel.pjv.utils.AdditionalMethods.*;
import static cz.cvut.fel.pjv.utils.Constants.PlayerConstants.*;
import static cz.cvut.fel.pjv.utils.Constants.*;

/**
 * Represents the player character in the game.
 */
public class Player extends Character {
    private Playing playing;
    private Image[][] animations;
    private boolean moving = false, attacking = false;
    private boolean left, right, jump;
    private int[][] levelData;
    private double xOffset = 9 * Game.SCALE, yOffset = 14 * Game.SCALE;
    private boolean attackChecked = false;

    private double jumpSpeed = -2.25d * Game.SCALE;
    private double fallSpeedAfterCollision = 0.5d * Game.SCALE;

    private Image healtBarImage;
    private int statusBarWidth = (int) (200 * Game.SCALE);
    private int statusBarHeight = (int) (50 * Game.SCALE);
    private int statusBarX = (int) (1 * Game.SCALE);
    private int statusBarY = (int) (3 * Game.SCALE);
    private int healthBarWidth = (int) (142 * Game.SCALE);
    private int healthBarHeight = (int) (17 * Game.SCALE);
    private int healthStartX = (int) (48 * Game.SCALE);
    private int healthStartY = (int) (17 * Game.SCALE);
    private int healthWidth = healthBarWidth;

    private int flipX = 0;
    private int flipW = 1;

    /**
     * Constructs a new player.
     *
     * @param x       the x-coordinate of the player
     * @param y       the y-coordinate of the player
     * @param width   the width of the player
     * @param height  the height of the player
     * @param playing the playing state of the game
     */
    public Player(double x, double y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        this.state = IDLE;
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
        this.walkSpeed = 1.0d * Game.SCALE;
        loadAnimations();
        initHitBox(20, 33);
        initAttackBox();
    }

    /**
     * Sets the spawn point of the player.
     *
     * @param spawn the spawn point
     */
    public void setSpawn(Point2D spawn) {
        this.x = spawn.getX();
        this.y = spawn.getY();
        hitBox.setX(x);
        hitBox.setY(y);
    }

    /**
     * Initializes the attack box.
     */
    private void initAttackBox() {
        attackBox = new Rectangle(x, y, (int) (20 * Game.SCALE), (int) (20 * Game.SCALE));
    }

    /**
     * Updates the state of the player.
     */
    public void update() {
        updateHealth();
        if (currentHealth <= 0) {
            if (state != DEATH) {
                state = DEATH;
                animTick = 0;
                animIndex = 0;
                playing.setPlayerDying(true);
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
            } else if (animIndex == GetSpriteAmount(DEATH) - 1 && animTick >= ANIMATION_SPEED - 1) {
                playing.setGameOver(true);
                playing.getGame().getAudioPlayer().stopSong();
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
            } else {
                updateAnimation();
            }
            return;
        }
        updateAttackBox();
        updatePosition();
        if (moving) {
            checkObjectTouched();
            checkSpikesTouched();
        }
        if (attacking) {
            checkAttack();
        }
        updateAnimation();
        setAnimation();
    }

    /**
     * Checks if the player has touched spikes.
     */
    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this);
    }

    /**
     * Checks if the player has touched any objects.
     */
    private void checkObjectTouched() {
        playing.checkObjectTouched(hitBox);
    }

    /**
     * Checks if the player has hit anything with their attack.
     */
    private void checkAttack() {
        if (attackChecked || animIndex != 3) {
            return;
        }
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
        playing.getGame().getAudioPlayer().playAttack();
    }

    /**
     * Updates the attack box position.
     */
    private void updateAttackBox() {
        if (right) {
            attackBox.setX(hitBox.getX() + hitBox.getWidth() + (int) (7 * Game.SCALE));
        } else if (left) {
            attackBox.setX(hitBox.getX() - hitBox.getWidth() + (int) (10 * Game.SCALE));
        }
        attackBox.setY(hitBox.getY() + (int) (10 * Game.SCALE));
    }

    /**
     * Updates the player's health bar width based on the current health.
     */
    private void updateHealth() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    /**
     * Renders the player and the UI elements.
     *
     * @param gc          the graphics context to draw on
     * @param levelOffset the offset for level scrolling
     */
    public void render(GraphicsContext gc, int levelOffset) {
        gc.drawImage(animations[state][animIndex], (hitBox.getX() - xOffset) - levelOffset + flipX, hitBox.getY() - yOffset, width * flipW, height);
//        drawHitBox(gc, levelOffset);
//        drawAttackBox(gc, levelOffset);
        drawIU(gc);
    }

    /**
     * Draws the UI elements such as the health bar.
     *
     * @param gc the graphics context to draw on
     */
    private void drawIU(GraphicsContext gc) {
        gc.drawImage(healtBarImage, statusBarX, statusBarY, statusBarWidth, statusBarHeight);
        gc.setFill(Color.rgb(255, 40, 77));
        gc.fillRect(healthStartX + statusBarX, healthStartY + statusBarY, healthWidth, healthBarHeight);
    }

    /**
     * Loads the player animations from sprite sheets.
     */
    private void loadAnimations() {
        Image image = LoadSave.getSpriteAtlas(LoadSave.playerAtlas);
        animations = new Image[12][8];
        int width = (int) image.getWidth() / 8;
        int height = (int) image.getHeight() / 12;

        for (int j = 0; j < 12; j++) { // j - height
            for (int i = 0; i < 8; i++) { // i - width
                animations[j][i] = new WritableImage(image.getPixelReader(), i * width, j * height, width, height);
            }
        }

        healtBarImage = LoadSave.getSpriteAtlas(LoadSave.heathBar);
    }

    /**
     * Loads the level data for collision detection.
     *
     * @param levelData the 2D array of the level layout
     */
    public void loadLevelData(int[][] levelData) {
        this.levelData = levelData;
        if (!isEntityOnFloor(hitBox, levelData)) {
            inAir = true;
        }
    }

    /**
     * Updates the player's position based on input and physics.
     */
    private void updatePosition() {
        moving = false;

        if (jump) {
            jump();
        }

        if (!inAir) {
            if ((!left && !right) || (left && right)) {
                return;
            }
        }

        double xSpeed = 0;

        if (left) {
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }
        if (right) {
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }

        if (!inAir) {
            if (!isEntityOnFloor(hitBox, levelData)) {
                inAir = true;
            }
        }

        if (inAir) {
            if (canMoveHere(hitBox.getX(), hitBox.getY() + airSpeed, hitBox.getWidth(), hitBox.getHeight(), levelData)) {
                hitBox.setY(hitBox.getY() + airSpeed);
                airSpeed += GRAVITY;
                updateXPosition(xSpeed);
            } else {
                hitBox.setY(getCharacterPositionAboveBellow(hitBox, airSpeed) + Game.TILES_SIZE); // Adjust the position slightly
                if (airSpeed > 0) {
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPosition(xSpeed);
            }
        } else {
            updateXPosition(xSpeed);
        }
        moving = true;
    }

    /**
     * Makes the player jump.
     */
    private void jump() {
        if (inAir) {
            return;
        }
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
        inAir = true;
        airSpeed = jumpSpeed;
    }

    /**
     * Resets the in-air state and vertical speed of the player.
     */
    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    /**
     * Updates the player's horizontal position based on speed and collisions.
     *
     * @param xSpeed the speed to move horizontally
     */
    private void updateXPosition(double xSpeed) {
        if (canMoveHere(hitBox.getX() + xSpeed, hitBox.getY(), hitBox.getWidth(), hitBox.getHeight(), levelData)) {
            hitBox.setX(hitBox.getX() + xSpeed);
        } else {
            hitBox.setX(getEntityPosNextToWall(hitBox, xSpeed));
        }
    }

    /**
     * Changes the player's health by a specified value.
     *
     * @param value the amount to change the health by
     */
    public void changeHealth(int value) {
        currentHealth += value;
        if (currentHealth <= 0) {
            currentHealth = 0;
        } else if (currentHealth >= maxHealth) {
            currentHealth = maxHealth;
        }
    }

    /**
     * Kills the player by setting their health to 0.
     */
    public void kill() {
        currentHealth = 0;
    }

    /**
     * Sets the animation state based on the player's actions.
     */
    private void setAnimation() {
        int startAnim = state;

        if (moving) {
            state = RUN;
        } else {
            state = IDLE;
        }

        if (inAir) {
            if (airSpeed < 0) {
                state = JUMP;
            } else {
                state = FALL;
            }
        }

        if (attacking) {
            state = ATTACK1;
        }

        if (startAnim != state) {
            resetAnimation();
        }
    }

    /**
     * Resets the animation tick and index.
     */
    private void resetAnimation() {
        animTick = 0;
        animIndex = 0;
    }

    /**
     * Updates the animation tick and index.
     */
    private void updateAnimation() {
        animTick++;
        if (animTick >= ANIMATION_SPEED) {
            animTick = 0;
            animIndex++;
            if (animIndex >= GetSpriteAmount(state)) {
                animIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    /**
     * Resets the directional booleans (left, right).
     */
    public void resetDirecBooleans() {
        left = right = false;
    }

    /**
     * Resets the player's state to the initial state.
     */
    public void resetPlayer() {
        resetDirecBooleans();
        inAir = false;
        airSpeed = 0;
        attacking = false;
        moving = false;
        jump = false;
        state = IDLE;
        currentHealth = maxHealth;
        resetAnimation();
        hitBox.setX(x);
        hitBox.setY(y);

        if (!isEntityOnFloor(hitBox, levelData)) {
            inAir = true;
        }
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean isAttacking() {
        return attacking;
    }
}
