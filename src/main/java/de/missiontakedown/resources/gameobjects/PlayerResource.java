package de.missiontakedown.resources.gameobjects;

import de.missiontakedown.resources.GameResource;

/**
 * @author Jan
 */
public class PlayerResource {

    public static GameResource PLAYER = new GameResource("/player/Player.png", 0.35f, 0.4f);
    public static GameResource PLAYER_NIGHT = new GameResource("/player/PlayerNight.png", 0.35f, 0.4f);
    public static GameResource PLAYER_MOVEMENT = new GameResource("/player/PlayerMovement.png", 0.35f, 0.4f);
    public static GameResource PLAYER_MOVEMENT_NIGHT = new GameResource("/player/PlayerMovementNight.png", 0.35f, 0.4f);

    public static GameResource MONEY_ICON = new GameResource("/player/MoneyIcon.png", 0.075f);

    public static GameResource DETONATOR = new GameResource("/player/Detonator.png", 0.4f);

    public static GameResource INVENTORY_FRAME = new GameResource("/player/InventoryFrame.png", 0.08f);

}
