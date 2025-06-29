package de.missiontakedown.resources.scenes;

import de.missiontakedown.resources.GameAudioResource;
import de.missiontakedown.resources.GameResource;

public class CowMinigameSceneResource {

    public static GameResource BACKGROUND = new GameResource("/scenes/cowminigame/Background.png");
    public static GameResource POOP = new GameResource("/scenes/cowminigame/Poop.png", 0.1f);

    public static GameAudioResource COW_AMBIENT = new GameAudioResource("/scenes/cowminigame/CowAmbient.wav");
    public static GameAudioResource POOP_PICK_UP = new GameAudioResource("/scenes/cowminigame/PoopPickUp.wav");
    public static GameAudioResource COW_POOP = new GameAudioResource("/scenes/cowminigame/CowPoop.wav", 0.75f);


}
