package de.jp.infoprojekt.resources.scenes;

import de.jp.infoprojekt.resources.GameAudioResource;
import de.jp.infoprojekt.resources.GameResource;

/**
 * SceneResources class
 *
 * @author Pascal
 * @version 24.06.2025
 */
public class SpawnSceneResource {

    public static GameResource BACKGROUND = new GameResource("/scenes/spawn/SpawnSceneBackground.png");
    public static GameResource BACKGROUND_DOOR_HALF = new GameResource("/scenes/spawn/SpawnSceneBackground-DoorHalf.png");
    public static GameResource BACKGROUND_AGENTS = new GameResource("/scenes/spawn/SpawnSceneBackground-Agents.png");
    public static GameResource BACKGROUND_FRIDGE = new GameResource("/scenes/spawn/SpawnWithFridgeOpen.png");

    public static GameResource PLAYER_SPACE = new GameResource("/scenes/spawn/SpawnSceneUsablePlayerSpace.png");

    public static GameAudioResource PHONE = new GameAudioResource("/scenes/spawn/PhoneRing.wav", 1.4f);
    public static GameAudioResource PHONE_HANG_UP = new GameAudioResource("/scenes/spawn/PhoneHangUp.wav", 0.5f);

    public static GameAudioResource PLAYER_STEPPING = new GameAudioResource("/scenes/spawn/PlayerStepping.wav", 0.3f);

    public static GameAudioResource DOOR_KNOCKING = new GameAudioResource("/scenes/spawn/DoorKnocking.wav");
    public static GameAudioResource DRAW_AND_SHOOT = new GameAudioResource("/scenes/spawn/DrawAndShoot.wav", 2f);

    public static GameAudioResource DOOR_KICKDOWN = new GameAudioResource("/scenes/spawn/DoorKickdown.wav", 0.6f);
    public static GameAudioResource FLASH_BANG_AND_BEEP = new GameAudioResource("/scenes/spawn/FlashBangAndBeep.wav");

    public static GameAudioResource COLA_DRINK = new GameAudioResource("/scenes/spawn/ColaDrinking.wav");

    public static GameAudioResource FRIDGE = new GameAudioResource("/scenes/spawn/FridgeOpening.wav", 2f);


}
