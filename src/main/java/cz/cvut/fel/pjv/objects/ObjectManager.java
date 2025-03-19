package cz.cvut.fel.pjv.objects;

import cz.cvut.fel.pjv.characters.Player;
import cz.cvut.fel.pjv.gamestates.Playing;
import cz.cvut.fel.pjv.levels.Level;
import cz.cvut.fel.pjv.utils.LoadSave;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

import static cz.cvut.fel.pjv.utils.Constants.ObjectConstants.*;

public class ObjectManager {
    private Playing playing;
    private Image[][] containerImage, potionImage, starImage;
    private Image spikeImage;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Star> stars;
    public boolean isAnyActiveStar;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImages();
    }

    public void loadObjects(Level nextLevel) {
        potions = new ArrayList<>(nextLevel.getPotions());
        containers = new ArrayList<>(nextLevel.getContainers());
        spikes = new ArrayList<>(nextLevel.getSpikes());
        stars = new ArrayList<>(nextLevel.getStars());
    }

    private void loadImages() {
        Image potionSprites = LoadSave.getSpriteAtlas(LoadSave.potionSprites);
        potionImage = new Image[1][7];
        for (int j = 0; j < potionImage.length; j++) {
            for (int i = 0; i < potionImage[j].length; i++) {
                potionImage[j][i] = new WritableImage(potionSprites.getPixelReader(), 12 * i, 15 * j, 12, 15);
            }
        }

        Image containerSprite = LoadSave.getSpriteAtlas(LoadSave.boxSprites);
        containerImage = new Image[2][8];
        for (int j = 0; j < containerImage.length; j++) {
            for (int i = 0; i < containerImage[j].length; i++) {
                containerImage[j][i] = new WritableImage(containerSprite.getPixelReader(), 40 * i, 30 * j, 40, 30);
            }
        }

        Image hammerSprites = LoadSave.getSpriteAtlas(LoadSave.starAtlas);
        starImage = new Image[1][7];
        for (int j = 0; j < starImage.length; j++) {
            for (int i = 0; i < starImage[j].length; i++) {
                starImage[j][i] = new WritableImage(hammerSprites.getPixelReader(), 16 * i, 16 * j, 16, 16);
            }
        }

        spikeImage = LoadSave.getSpriteAtlas(LoadSave.trapAtlas);
    }


    public void update() {
        isAnyActiveStar = false;
        for (Potion p : potions) {
            if (p.active){
                p.update();
            }
        }
        for (GameContainer c : containers) {
            if (c.active){
                c.update();
            }
        }
        for (Star h : stars) {
            if (h.active){
                h.update();
                isAnyActiveStar = true;
            }
        }
    }

    public void draw(GraphicsContext gc, int xLevelOffset) {
        drawPotions(gc, xLevelOffset);
        drawContainers(gc, xLevelOffset);
        drawTraps(gc, xLevelOffset);
        drawStars(gc, xLevelOffset);
    }

    private void drawStars(GraphicsContext gc, int xLevelOffset) {
        for (Star s : stars) {
            if (s.active){
                gc.drawImage(starImage[0][s.getAnimIndex()], (int) (s.getHitBox().getX() + s.getxDrawOffset() - xLevelOffset), (int) (s.getHitBox().getY() + s.getyDrawOffset()), STAR_WIDTH, STAR_HEIGHT);
            }
        }
    }

    private void drawTraps(GraphicsContext gc, int xLevelOffset) {
        for (Spike s : spikes) {
            gc.drawImage(spikeImage, (int) (s.getHitBox().getX() - s.getxDrawOffset() - xLevelOffset), (int) (s.getHitBox().getY() - s.getyDrawOffset()), SPIKE_WIDTH, SPIKE_HEIGHT);
        }
    }

    private void drawContainers(GraphicsContext gc, int xLevelOffset) {
        for (GameContainer c : containers) {
            if (c.active){
                int type = 0;
                if (c.getObjectType() == BARREL)
                    type = 1;
                gc.drawImage(containerImage[type][c.getAnimIndex()], (int) (c.getHitBox().getX() - c.getxDrawOffset() - xLevelOffset), (int) (c.getHitBox().getY() - c.getyDrawOffset()), CONTAINER_WIDTH, CONTAINER_HEIGHT);
            }
        }
    }

    private void drawPotions(GraphicsContext gc, int xLevelOffset) {
        for (Potion p : potions) {
            if (p.active){
                gc.drawImage(potionImage[0][p.getAnimIndex()], (int) (p.getHitBox().getX() - p.getxDrawOffset() - xLevelOffset), (int) (p.getHitBox().getY() - p.getyDrawOffset()), POTION_WIDTH, POTION_HEIGHT);
            }
        }
    }

    public void checkSpikeTouched(Player player) {
        for (Spike s : spikes) {
            if (s.getHitBox().intersects(player.getHitBox().getX(), player.getHitBox().getY(), player.getHitBox().getWidth(), player.getHitBox().getHeight())) {
                player.kill();
            }
        }
    }

    public void checkObjectTouched(Rectangle hitBox) {
        for (Potion p : potions) {
            if (p.active){
                if (hitBox.intersects(p.getHitBox().getX(), p.getHitBox().getY(), p.getHitBox().getWidth(), p.getHitBox().getHeight())) {
                    p.setActive(false);
                    applyEffectOnPlayer(p);
                }
            }
        }
        for (Star s : stars) {
            if (s.active) {
                if (hitBox.intersects(s.getHitBox().getX(), s.getHitBox().getY(), s.getHitBox().getWidth(), s.getHitBox().getHeight())) {
                    s.setActive(false);
                }
            }
        }
    }

    public void applyEffectOnPlayer(Potion p) {
        playing.getPlayer().changeHealth(HEALTH_POTION_VALUE);
    }

    public void checkObjectHit(Rectangle attackBox) {
        for (GameContainer c : containers) {
            if (c.active && !c.doAnimation){
                if (c.getHitBox().intersects(attackBox.getX(), attackBox.getY(), attackBox.getWidth(), attackBox.getHeight())) {
                    c.setDoAnimation(true);
                    int type = 0;
                    if (c.getObjectType() == BARREL)
                        type = 1;
                    potions.add(new Potion((int) (c.getHitBox().getX() + c.getHitBox().getWidth() / 2), (int) (c.getHitBox().getY() - c.getHitBox().getHeight() / 4), HEALTH_POTION));
                    return;
                }
            }
        }
    }

    public void resetAllObjects() {
        loadObjects(playing.getLevelManager().getCurrentLevel());
        for (GameContainer c : containers) {
            c.reset();
        }
        for (Potion p : potions) {
            p.reset();
        }
        for (Star s : stars) {
            s.reset();
        }
    }
}
