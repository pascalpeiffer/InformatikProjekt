package de.jp.infoprojekt.resources.scenes;

import de.jp.infoprojekt.resources.GameAudioResource;
import de.jp.infoprojekt.resources.GameResource;

public class TravelSceneResource {

    public static GameResource BACKGROUND = new GameResource("/scenes/travel/TravellingBackground.png");
    public static GameResource BACKGROUND_NIGHT = new GameResource("/scenes/travel/TravellingBackgroundNight.png");
    public static GameResource CAR = new GameResource("/scenes/travel/Car.png", 0.5f);
    public static GameResource CAR_NIGHT = new GameResource("/scenes/travel/CarNight.png", 0.5f);
    public static GameAudioResource BACKGROUND_AUDIO = new GameAudioResource("/scenes/travel/TravellingSoundBackground.wav");

}
