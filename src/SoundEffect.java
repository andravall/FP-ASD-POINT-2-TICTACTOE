/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231159 - Mohammad Ferdinand Valliandra
 * 2 - 5026231135 - Fachreza Aptadhi Kurniawan
 * 3 - 5026231149 - Ananda Donelly Reksana
 */

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public enum SoundEffect {
    EAT_FOOD("Audio/eatfood.wav"),
    EXPLODE("Audio/explode.wav"),
    DIE("Audio/die.wav"),
    WIN("Audio/win.wav");

    /** Nested enumeration for specifying volume */
    public static enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }

    public static Volume volume = Volume.LOW;

    /** Each sound effect has its own clip, loaded with its own sound file. */
    private Clip clip;

    /** Private Constructor to construct each element of the enum with its own sound file. */
    private SoundEffect(String soundFileName) {
        try {
            // Use URL (instead of File) to read from disk and JAR.
            URL url = this.getClass().getClassLoader().getResource(soundFileName);
            if (url == null) {
                throw new IOException("Sound file not found: " + soundFileName);
            }
            // Set up an audio input stream piped from the sound file.
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            // Get a clip resource.
            clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /** Play or Re-play the sound effect from the beginning, by rewinding. */
    public void play() {
        if (volume != Volume.MUTE) {
            if (clip.isRunning()) {
                clip.stop(); // Stop the player if it is still running
            }
            clip.setFramePosition(0); // Rewind to the beginning
            clip.start(); // Start playing
        }
    }

    /** Optional static method to preload all the sound files. */
    static void initGame() {
        values(); // Calls the constructor for all the elements
    }
}
