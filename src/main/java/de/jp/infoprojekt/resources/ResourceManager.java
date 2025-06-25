package de.jp.infoprojekt.resources;

import de.jp.infoprojekt.util.FloatPair;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableFloatValue;
import javafx.beans.value.ObservableValue;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResourceManager {

    public static BufferedImage TITLE_SCENE_BACKGROUND;

    //Scenes

    public static void loadImageResources() throws IOException {
        TITLE_SCENE_BACKGROUND = ImageIO.read(Objects.requireNonNull(ResourceManager.class.getResource("/scenes/title/titleSceneBackground.png")));

    }

    private static ObjectProperty<FloatPair> scaling = new SimpleObjectProperty<>(new FloatPair(1.0f, 1.0f)); // Width / Height -> default 1920x1080//Scaling from 1080px

    private static List<ScalingEvent> eventListener = new ArrayList<>();

    static {
        ChangeListener<FloatPair> changeListener = (observable, oldValue, newValue) ->
            eventListener.forEach(scalingEvent -> scalingEvent.scale(newValue.getY(), newValue.getY()));

        scaling.addListener(changeListener);
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
}
