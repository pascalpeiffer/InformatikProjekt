package de.missiontakedown.resources.scenes;

import de.missiontakedown.resources.GameAudioResource;
import de.missiontakedown.resources.GameResource;

public class HeadquarterSceneResource {

    public static final GameResource BACKGROUND = new GameResource("/scenes/headquarter/Headquarter.png");
    public static final GameResource BACKGROUND_EXPLOSION = new GameResource("/scenes/headquarter/HeadquarterExplosion.png");
    public static final GameResource PLAYER_MAP = new GameResource("/scenes/headquarter/HeadquarterPlayerMap.png");

    public static final GameAudioResource AMBIENT = new GameAudioResource("/scenes/headquarter/Ambient.wav", 0.4f);
    public static final GameAudioResource EXPLOSION = new GameAudioResource("/scenes/headquarter/HeadquarterExplosion.wav");

}
