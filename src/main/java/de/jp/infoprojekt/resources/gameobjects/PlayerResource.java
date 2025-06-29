package de.jp.infoprojekt.resources.gameobjects;

import de.jp.infoprojekt.resources.GameResource;

/**
 * Player resource class
 *
 * @author Pascal
 * @version 24.06.2025
 */
public class PlayerResource {

    public static GameResource PLAYER = new GameResource("/player/Player.png", 0.35f, 0.4f);
    public static GameResource PLAYER_MOVEMENT = new GameResource("/player/PlayerMovement.png", 0.35f, 0.4f);

    public static GameResource MONEY_ICON = new GameResource("/player/MoneyIcon.png", 0.075f);

    public static GameResource DETONATOR = new GameResource("/player/Detonator.png", 0.4f);

    public static GameResource INVENTORY_FRAME = new GameResource("/player/InventoryFrame.png", 0.08f);

}
