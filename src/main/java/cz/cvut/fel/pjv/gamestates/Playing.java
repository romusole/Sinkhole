package cz.cvut.fel.pjv.gamestates;

import cz.cvut.fel.pjv.characters.EnemyManager;
import cz.cvut.fel.pjv.characters.Player;
import cz.cvut.fel.pjv.gui.GameCompletedOverlay;
import cz.cvut.fel.pjv.gui.GameOverOverlay;
import cz.cvut.fel.pjv.gui.LevelCompletedOverlay;
import cz.cvut.fel.pjv.gui.PauseOverlay;
import cz.cvut.fel.pjv.levels.LevelManager;
import cz.cvut.fel.pjv.main.Game;
import cz.cvut.fel.pjv.objects.ObjectManager;
import cz.cvut.fel.pjv.utils.LoadSave;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Represents the playing state of the game.
 */
public class Playing extends State implements StateMethods {
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private GameCompletedOverlay gameCompletedOverlay;
    private boolean paused = false;
    private ObjectManager objectManager;
    private boolean gameOver = false;
    private boolean levelCompleted = false;
    private boolean playerDying = false;
    private boolean gameCompleted = false;

    private int xLevelOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int maxLevelOffset;

    private Image background;

    /**
     * Constructs a new Playing state.
     *
     * @param game the game instance
     */
    public Playing(Game game) {
        super(game);
        initClasses();
        background = LoadSave.getSpriteAtlas(LoadSave.playingBackground);

        calculateLevelOffset();
        loadStartLevel();
    }

    /**
     * Loads the next level in the game.
     */
    public void loadNextLevel() {
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }

    /**
     * Loads the start level.
     */
    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    /**
     * Calculates the maximum level offset for scrolling.
     */
    private void calculateLevelOffset() {
        maxLevelOffset = levelManager.getCurrentLevel().getLevelOffset();
    }

    /**
     * Initializes the game classes.
     */
    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        player = new Player(200, 200, (int) (48 * Game.SCALE), (int) (48 * Game.SCALE), this);
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
        objectManager = new ObjectManager(this);
        gameCompletedOverlay = new GameCompletedOverlay(this);
    }

    /**
     * Updates the game state.
     */
    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (levelCompleted) {
            levelCompletedOverlay.update();
        } else if (gameOver) {
            gameOverOverlay.update();
        } else if (playerDying) {
            player.update();
        } else if (gameCompleted) {
            gameOverOverlay.update();
        } else {
            objectManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkCloseToBorder();
            checkForActive();
        }
    }

    /**
     * Checks if there are any active enemies or objects.
     */
    private void checkForActive() {
        if (!enemyManager.isAnyActiveEnemy && !objectManager.isAnyActiveStar)
            if (levelManager.getLevelIndex() + 1 >= levelManager.getAmountOfLevels()) {
                setGameCompleted(true);
            } else {
                setLevelCompletedTrue(true);
            }
    }

    /**
     * Draws the game state.
     *
     * @param g the graphics context to draw on
     */
    @Override
    public void draw(GraphicsContext g) {
        g.clearRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        g.drawImage(background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        levelManager.draw(g, xLevelOffset);
        player.render(g, xLevelOffset);
        objectManager.draw(g, xLevelOffset);
        enemyManager.draw(g, xLevelOffset);
        if (paused) {
            Color backColor = new Color(0, 0, 0, 0.7);
            g.setFill(backColor);
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (gameOver) {
            gameOverOverlay.draw(g);
        } else if (levelCompleted) {
            levelCompletedOverlay.draw(g);
        } else if (gameCompleted) {
            gameCompletedOverlay.draw(g);
        }
    }

    /**
     * Checks if the player is close to the screen border and updates the level offset.
     */
    private void checkCloseToBorder() {
        int playerX = (int) player.getHitBox().getX();
        int difference = playerX - xLevelOffset;
        if (difference > rightBorder) {
            xLevelOffset += difference - rightBorder;
        } else if (difference < leftBorder) {
            xLevelOffset += difference - leftBorder;
        }
        if (xLevelOffset > maxLevelOffset) {
            xLevelOffset = maxLevelOffset;
        } else if (xLevelOffset < 0) {
            xLevelOffset = 0;
        }
    }

    /**
     * Unpauses the game.
     */
    public void unpauseGame() {
        paused = false;
    }

    /**
     * Resets all game states and entities.
     */
    public void resetAll() {
        gameOver = false;
        paused = false;
        levelCompleted = false;
        playerDying = false;
        gameCompleted = false;
        player.resetPlayer();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
    }

    /**
     * Checks if the player's attack hit an enemy.
     *
     * @param attackBox the attack hitbox
     */
    public void checkEnemyHit(Rectangle attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    /**
     * Checks if the player has touched any objects.
     *
     * @param hitBox the player's hitbox
     */
    public void checkObjectTouched(Rectangle hitBox) {
        objectManager.checkObjectTouched(hitBox);
    }

    /**
     * Checks if the player's attack hit an object.
     *
     * @param attackBox the attack hitbox
     */
    public void checkObjectHit(Rectangle attackBox) {
        objectManager.checkObjectHit(attackBox);
    }

    /**
     * Checks if the player has touched spikes.
     *
     * @param player the player
     */
    public void checkSpikesTouched(Player player) {
        objectManager.checkSpikeTouched(player);
    }

    /**
     * Handles mouse click events.
     *
     * @param e the mouse event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver) {
            if (e.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                player.setAttacking(true);
            }
        }
    }

    /**
     * Handles mouse press events.
     *
     * @param e the mouse event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mousePressed(e);
            } else if (levelCompleted) {
                levelCompletedOverlay.mousePressed(e);
            }
        } else {
            gameOverOverlay.mousePressed(e);
        }
    }

    /**
     * Handles mouse release events.
     *
     * @param e the mouse event
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mouseReleased(e);
            } else if (levelCompleted) {
                levelCompletedOverlay.mouseReleased(e);
            }
        } else {
            gameOverOverlay.mouseReleased(e);
        }
    }

    /**
     * Handles mouse move events.
     *
     * @param e the mouse event
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mouseMoved(e);
            } else if (levelCompleted) {
                levelCompletedOverlay.mouseMoved(e);
            }
        } else {
            gameOverOverlay.mouseMoved(e);
        }
    }

    /**
     * Handles mouse drag events.
     *
     * @param e the mouse event
     */
    public void mouseDragged(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mouseDragged(e);
            }
        }
    }

    /**
     * Handles key press events.
     *
     * @param e the key event
     */
    @Override
    public void KeyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case A -> player.setLeft(true);
            case D -> player.setRight(true);
            case SPACE -> player.setJump(true);
            case ESCAPE -> paused = !paused;
            default -> {
            }
        }
    }

    /**
     * Handles key release events.
     *
     * @param e the key event
     */
    @Override
    public void KeyReleased(KeyEvent e) {
        if (!gameOver) {
            switch (e.getCode()) {
                case A -> player.setLeft(false);
                case D -> player.setRight(false);
                case SPACE -> player.setJump(false);
                default -> {
                }
            }
        }
        if (gameCompleted) {
            gameCompletedOverlay.KeyPressed(e);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public void setMaxLevelOffset(int xLevelOffset) {
        this.maxLevelOffset = xLevelOffset;
    }

    public void setLevelCompletedTrue(boolean levelCompleted) {
        this.levelCompleted = levelCompleted;
        if (levelCompleted) {
            game.getAudioPlayer().levelCompleted();
        }
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }

    public void setGameCompleted(boolean gameCompleted) {
        this.gameCompleted = gameCompleted;
        if (gameCompleted) {
            game.getAudioPlayer().levelCompleted();
        }
    }
}
