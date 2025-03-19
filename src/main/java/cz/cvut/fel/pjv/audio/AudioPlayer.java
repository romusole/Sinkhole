package cz.cvut.fel.pjv.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;

/**
 * The AudioPlayer class handles playing background music and sound effects for the game.
 * It supports functionalities such as playing songs, sound effects, muting, and volume control.
 */
public class AudioPlayer {

    // Song and effect IDs
    public static final int MENU = 0;
    public static final int LEVEL = 1;
    public static final int DIE = 0;
    public static final int JUMP = 1;
    public static final int GAMEOVER = 2;
    public static final int LEVELCOMPLETED = 3;
    public static final int ATTACK_1 = 4;

    private MediaPlayer[] songs, effects;
    private int currentSongID;
    private double volume = 0.5d;
    private boolean songMute, effectMute;

    /**
     * Constructs an AudioPlayer instance, loads songs and effects, and starts playing the menu song.
     */
    public AudioPlayer() {
        loadSongs();
        loadEffects();
        playSong(MENU);
    }

    /**
     * Loads background songs into the MediaPlayer array.
     */
    private void loadSongs() {
        String[] names = {"menu", "level1"};
        songs = new MediaPlayer[names.length];
        for (int i = 0; i < songs.length; i++) {
            songs[i] = getMediaPlayer(names[i]);
        }
    }

    /**
     * Loads sound effects into the MediaPlayer array.
     */
    private void loadEffects() {
        String[] names = {"die", "jump", "gameover", "lvlcompleted", "attack1"};
        effects = new MediaPlayer[names.length];
        for (int i = 0; i < effects.length; i++) {
            effects[i] = getMediaPlayer(names[i]);
        }
        updateEffectsVolume();
    }

    /**
     * Creates and returns a MediaPlayer for a given audio file name.
     *
     * @param name the name of the audio file (without extension)
     * @return the MediaPlayer for the given audio file
     */
    public MediaPlayer getMediaPlayer(String name) {
        URL url = getClass().getResource("/sounds/" + name + ".mp3");
        if (url == null) {
            System.err.println("Audio file not found: " + name);
            return null;
        }
        Media media = new Media(url.toString());
        return new MediaPlayer(media);
    }

    /**
     * Updates the volume for the currently playing song.
     */
    private void updateSongsVolume() {
        if (songs[currentSongID] != null) {
            songs[currentSongID].setVolume(volume);
        }
    }

    /**
     * Updates the volume for all sound effects.
     */
    private void updateEffectsVolume() {
        for (MediaPlayer e : effects) {
            if (e != null) {
                e.setVolume(volume);
            }
        }
    }

    /**
     * Toggles the mute state for background songs.
     */
    public void toggleSongsMute() {
        this.songMute = !this.songMute;
        for (MediaPlayer s : songs) {
            if (s != null) {
                s.setMute(songMute);
            }
        }
    }

    /**
     * Toggles the mute state for sound effects.
     */
    public void toggleEffectMute() {
        effectMute = !effectMute;
        for (MediaPlayer e : effects) {
            if (e != null) {
                e.setMute(effectMute);
            }
        }
        if (!effectMute) {
            playEffect(JUMP);
        }
    }

    /**
     * Sets the volume for both songs and sound effects.
     *
     * @param volume the volume level (0.0 to 1.0)
     */
    public void setVolume(double volume) {
        this.volume = volume;
        updateSongsVolume();
        updateEffectsVolume();
    }

    /**
     * Stops the currently playing song.
     */
    public void stopSong() {
        if (songs[currentSongID] != null && songs[currentSongID].getStatus() == MediaPlayer.Status.PLAYING) {
            songs[currentSongID].stop();
        }
    }

    /**
     * Stops the current song and plays the level completed sound effect.
     */
    public void levelCompleted() {
        stopSong();
        playEffect(LEVELCOMPLETED);
    }

    /**
     * Plays the attack sound effect.
     */
    public void playAttack() {
        playEffect(ATTACK_1);
    }

    /**
     * Plays a specified song.
     *
     * @param songID the ID of the song to play
     */
    public void playSong(int songID) {
        stopSong();
        currentSongID = songID;
        updateSongsVolume();
        if (songs[currentSongID] != null) {
            songs[currentSongID].seek(Duration.ZERO);
            songs[currentSongID].setCycleCount(MediaPlayer.INDEFINITE);
            songs[currentSongID].play();
        }
    }

    /**
     * Plays a specified sound effect.
     *
     * @param effectID the ID of the sound effect to play
     */
    public void playEffect(int effectID) {
        if (effects[effectID] != null) {
            effects[effectID].seek(Duration.ZERO);
            effects[effectID].play();
        }
    }
}
