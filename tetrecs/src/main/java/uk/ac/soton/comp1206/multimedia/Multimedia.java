package uk.ac.soton.comp1206.multimedia;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Multimedia class creates a base model of how to play music/audio.
 * The 2 methods: playAudioFile & playBackgroundMusic deal with most of the logic.
 */
public class Multimedia {
    private static final Logger logger = LogManager.getLogger(Multimedia.class);

    private static BooleanProperty audioEnabledProperty = new SimpleBooleanProperty(true);
    private static MediaPlayer mediaPlayer;
    private static MediaPlayer mediaPlayerTwo;

    private static MediaPlayer audioPlayer;
    private static MediaPlayer musicPlayer;


    public static void playAudioFile(String file) {
        if (!getAudioEnabled()) return;

        //the file to play the audio file
        String toPlay = Multimedia.class.getResource("/" + file).toExternalForm();

        try {
            Media play = new Media(toPlay);
            mediaPlayer = new MediaPlayer(play);
            mediaPlayer.play();

        } catch (Exception e) {
            setAudioEnabled(false);
            e.printStackTrace(); //gives a more accurate/appropriate error
            logger.error("Unable to play audio from file (audio file)");
        }

    }

    public static void playBackgroundMusic(String file) {
        if (!getAudioEnabled()) return;

        //the file to play the music file
        String toPlay = Multimedia.class.getResource("/" + file).toExternalForm();

        try {
            Media play = new Media(toPlay);
            mediaPlayerTwo = new MediaPlayer(play);
            mediaPlayerTwo.play();

            //Loops the music continuously
            mediaPlayerTwo.setAutoPlay(true);
            mediaPlayerTwo.setCycleCount(MediaPlayer.INDEFINITE);

        } catch (Exception e) {
            setAudioEnabled(false);
            e.printStackTrace(); //gives a more accurate/appropriate error
            logger.error("Unable to play audio from file (background music");
        }
    }

    public static BooleanProperty audioEnabledProperty() {
        return audioEnabledProperty;
    }

    public static void setAudioEnabled(boolean enabled) {
        logger.info("Audio enabled set to: " + enabled);
        audioEnabledProperty().set(enabled);
    }

    public static boolean getAudioEnabled() {
        return audioEnabledProperty().get();
    }

}