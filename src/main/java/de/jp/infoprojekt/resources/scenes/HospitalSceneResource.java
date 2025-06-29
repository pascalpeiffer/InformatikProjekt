package de.jp.infoprojekt.resources.scenes;

import de.jp.infoprojekt.resources.GameAudioResource;
import de.jp.infoprojekt.resources.GameResource;

public class HospitalSceneResource {

    public static final GameResource BACKGROUND = new GameResource("/scenes/hospital/Background.png");

    public static final GameAudioResource AMBIENT = new GameAudioResource("/scenes/hospital/HospitalAmbient.wav");

    public static final GameAudioResource GAME_OVER = new GameAudioResource("/scenes/hospital/GameOver.wav", 0.5f);

}
