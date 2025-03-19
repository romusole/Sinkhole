package cz.cvut.fel.pjv.gui;

import cz.cvut.fel.pjv.main.Game;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

import static cz.cvut.fel.pjv.utils.Constants.UI.PauseButtons.SOUND_SIZE;
import static cz.cvut.fel.pjv.utils.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static cz.cvut.fel.pjv.utils.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

/**
 * Manages audio options in the game, including sound and volume controls.
 */
public class AudioOptions {
    private Game game;
    private SoundButton musicButton, sfxButton;
    private VolumeButtons volumeButton;

    /**
     * Constructs an AudioOptions object with the specified game instance.
     *
     * @param game the game instance associated with these audio options
     */
    public AudioOptions(Game game) {
        this.game = game;
        createSoundButtons();
        createVolumeButton();
    }

    /**
     * Creates the volume button for adjusting game volume.
     */
    private void createVolumeButton() {
        int volumeX = (int) (348 * Game.SCALE);
        int volumeY = (int) (310 * Game.SCALE);
        volumeButton = new VolumeButtons(volumeX, volumeY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    /**
     * Creates the sound buttons for toggling music and sound effects.
     */
    private void createSoundButtons() {
        int soundX = (int) (480 * Game.SCALE);
        int musicY = (int) (170 * Game.SCALE);
        int sfxY = (int) (216 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    /**
     * Updates the state of the sound and volume buttons.
     */
    public void update() {
        musicButton.update();
        sfxButton.update();
        volumeButton.update();
    }

    /**
     * Draws the sound and volume buttons on the provided GraphicsContext.
     *
     * @param gc the GraphicsContext to draw on
     */
    public void draw(GraphicsContext gc) {
        musicButton.draw(gc);
        sfxButton.draw(gc);
        volumeButton.draw(gc);
    }

    /**
     * Handles mouse dragged events to adjust the volume slider.
     *
     * @param e the mouse event
     */
    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMouseOver()) {
            double volumeBefore = volumeButton.getValue();
            volumeButton.changeX((int) e.getX());
            double volumeAfter = volumeButton.getValue();
            if (volumeBefore != volumeAfter) {
                game.getAudioPlayer().setVolume(volumeAfter);
            }
        }
    }

    /**
     * Handles mouse pressed events for sound and volume buttons.
     *
     * @param e the mouse event
     */
    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton))
            musicButton.setMousePressed(true);
        else if (isIn(e, sfxButton)) {
            sfxButton.setMousePressed(true);
        } else if (isIn(e, volumeButton)) {
            volumeButton.setMousePressed(true);
        }
    }

    /**
     * Handles mouse released events for sound and volume buttons.
     *
     * @param e the mouse event
     */
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, musicButton)) {
            if (musicButton.isMousePressed()) {
                musicButton.update();
                musicButton.setMuted(!musicButton.isMuted());
                game.getAudioPlayer().toggleSongsMute();
            }
        } else if (isIn(e, sfxButton)) {
            if (sfxButton.isMousePressed()) {
                sfxButton.update();
                sfxButton.setMuted(!sfxButton.isMuted());
                game.getAudioPlayer().toggleEffectMute();
            }
        }
        musicButton.resetBools();
        sfxButton.resetBools();
        volumeButton.resetBools();
    }

    /**
     * Handles mouse moved events to update button hover states.
     *
     * @param e the mouse event
     */
    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if (isIn(e, musicButton)) {
            musicButton.setMouseOver(true);
        } else if (isIn(e, sfxButton)) {
            sfxButton.setMouseOver(true);
        } else if (isIn(e, volumeButton)) {
            volumeButton.setMouseOver(true);
        }
    }

    /**
     * Checks if the mouse event is within the bounds of the specified button.
     *
     * @param e the mouse event
     * @param b the button to check
     * @return true if the mouse event is within the bounds of the button, false otherwise
     */
    private boolean isIn(MouseEvent e, PauseButton b) {
        return (b.getBounds().contains(e.getX(), e.getY()));
    }
}
