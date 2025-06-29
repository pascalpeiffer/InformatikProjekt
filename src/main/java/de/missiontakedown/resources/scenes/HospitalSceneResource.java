package de.missiontakedown.resources.scenes;

import de.missiontakedown.resources.GameAudioResource;
import de.missiontakedown.resources.GameResource;

/**
 * @author Jan
 */
public class HospitalSceneResource {

    public static final GameResource BACKGROUND = new GameResource("/scenes/hospital/Background.png");

    public static final GameAudioResource AMBIENT = new GameAudioResource("/scenes/hospital/HospitalAmbient.wav");

    public static final GameAudioResource GAME_OVER = new GameAudioResource("/scenes/hospital/GameOver.wav", 0.5f);

}
