package de.jp.infoprojekt.resources.scenes;

import de.jp.infoprojekt.resources.GameAudioResource;
import de.jp.infoprojekt.resources.GameResource;

public class HeadquarterSceneResource {

    public static final GameResource BACKGROUND = new GameResource("/scenes/headquarter/Headquarter.png");
    public static final GameResource BACKGROUND_EXPLOSION = new GameResource("/scenes/headquarter/HeadquarterExplosion.png");
    public static final GameResource PLAYER_MAP = new GameResource("/scenes/headquarter/HeadquarterPlayerMap.png");

    public static final GameAudioResource AMBIENT = new GameAudioResource("/scenes/headquarter/Ambient.wav", 0.4f);
    public static final GameAudioResource EXPLOSION = new GameAudioResource("/scenes/headquarter/HeadquarterExplosion.wav");

}
