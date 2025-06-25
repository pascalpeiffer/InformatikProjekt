package de.jp.infoprojekt.gameengine.gameobjects.player;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.gameobjects.AbstractGameObject;
import de.jp.infoprojekt.resources.GameResource;
import de.jp.infoprojekt.resources.ScalingEvent;
import de.jp.infoprojekt.resources.gameobjects.Player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * PlayerCharacter class
 *
 * @author Pascal
 * @version 24.06.2025
 */
public class PlayerCharacter extends AbstractGameObject implements ScalingEvent {

    private final GameEngine engine;

    //Movement
    private boolean isMoveable = false; //Are we listening to key inputs?
    private float playerMovementSpeed = 0.004f;
    private boolean isSprinting = false;
    private float playerMovementSpeedSprintMultiplier = 1.5f;
    private List<Rectangle2D> blockArea = new ArrayList<>();

    //Render
    private GameResource player = Player.PLAYER;
    private boolean flipPlayerImage = false;


    public PlayerCharacter(GameEngine engine) {
        this.engine = engine;
        setSize(player.getWidth(), player.getHeight());
        engine.getTickProvider().onTick(this::tick);
    }

    public void tick() {
        if (isMoveable) {
            handleKeyInputs();
        }
    }

    public void setBlockingArea(List<Rectangle2D> playerBlockingArea) {
        this.blockArea = playerBlockingArea;
    }

    private void handleKeyInputs() {
        float finalPlayerSpeed = playerMovementSpeed * (isSprinting ? playerMovementSpeedSprintMultiplier : 1);

        if (engine.getGameKeyHandler().isKeyDown(KeyEvent.VK_W)) {
            setRelativeY(getRelativeY() - finalPlayerSpeed);
        }else if (engine.getGameKeyHandler().isKeyDown(KeyEvent.VK_S)) {
            setRelativeY(getRelativeY() + finalPlayerSpeed);
        }

        if (engine.getGameKeyHandler().isKeyDown(KeyEvent.VK_A)) {
            setRelativeX(getRelativeX() - finalPlayerSpeed);
            flipPlayerImage = true;
        }else if (engine.getGameKeyHandler().isKeyDown(KeyEvent.VK_D)) {
            flipPlayerImage = false;
            setRelativeX(getRelativeX() + finalPlayerSpeed);
        }

        if (engine.getGameKeyHandler().isKeyDown(KeyEvent.VK_SHIFT)) {
            isSprinting = true;
        }else {
            isSprinting = false;
        }
    }

    public boolean isMoveable() {
        return isMoveable;
    }

    public void setMoveable(boolean moveable) {
        isMoveable = moveable;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(player.getResource(), flipPlayerImage ? player.getWidth() : 0, 0, flipPlayerImage ? -player.getWidth() : player.getWidth(), player.getHeight(), null);

        g.setColor(Color.RED);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    @Override
    public void scale(float width, float height) {
        if (player != null) {
            setSize(player.getWidth(), player.getHeight());
        }
        super.scale(width, height);
    }
}
