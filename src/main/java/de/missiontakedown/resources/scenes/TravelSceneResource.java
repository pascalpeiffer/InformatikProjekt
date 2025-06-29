package de.missiontakedown.resources.scenes;

import de.missiontakedown.resources.GameAudioResource;
import de.missiontakedown.resources.GameResource;

/**
 * @author Jan
 */
public class TravelSceneResource {

    public static GameResource BACKGROUND = new GameResource("/scenes/travel/TravellingBackground.png");
    public static GameResource BACKGROUND_NIGHT = new GameResource("/scenes/travel/TravellingBackgroundNight.png");
    public static GameResource CAR = new GameResource("/scenes/travel/Car.png", 0.5f);
    public static GameResource CAR_NIGHT = new GameResource("/scenes/travel/CarNight.png", 0.5f);
    public static GameAudioResource BACKGROUND_AUDIO = new GameAudioResource("/scenes/travel/TravellingSoundBackground.wav");

}
