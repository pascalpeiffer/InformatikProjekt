package de.missiontakedown.resources;

import de.missiontakedown.util.FloatPair;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.List;

public class ResourceManager {

    public static GameResource MAIN_LOGO = new GameResource("/GameLogo.png");

    private static ObjectProperty<FloatPair> scaling = new SimpleObjectProperty<>(new FloatPair(1.0f, 1.0f)); // Width / Height -> default 1920x1080//Scaling from 1080px

    private static List<ScalingEvent> eventListener = new ArrayList<>();

    private static Line audioLine;

    static {
        ChangeListener<FloatPair> changeListener = (observable, oldValue, newValue) ->
            eventListener.forEach(scalingEvent -> scalingEvent.scale(newValue.getY(), newValue.getY()));

        scaling.addListener(changeListener);

        //Get default audio device
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                AudioSystem.NOT_SPECIFIED,
                16, 2, 4,
                AudioSystem.NOT_SPECIFIED, true);
        DataLine.Info info = new DataLine.Info(Clip.class, format);

        try {
            audioLine = AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            System.out.println("Could not find audio device");
            throw new RuntimeException(e);
        }
    }

    public static void addScalingListener(ScalingEvent event) {
        eventListener.add(event);
    }

    public static void removeScalingListener(ScalingEvent event) {
        eventListener.remove(event);
    }

    public static ObjectProperty<FloatPair> scalingProperty() {
        return scaling;
    }

    public static FloatPair getScaling() {
        return scaling.get();
    }

    public static int getGameScreenWidth() {
        return (int) (getScaling().getX() * 1920);
    }

    public static int getGameScreenHeight() {
        return (int) (getScaling().getY() * 1080);
    }

    public static Line getAudioLine() {
        return audioLine;
    }
}
