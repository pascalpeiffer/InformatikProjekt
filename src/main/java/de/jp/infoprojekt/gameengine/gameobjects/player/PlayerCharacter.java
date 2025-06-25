package de.jp.infoprojekt.gameengine.gameobjects.player;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.gameobjects.AbstractGameObject;
import de.jp.infoprojekt.resources.GameResource;
import de.jp.infoprojekt.resources.ScalingEvent;
import de.jp.infoprojekt.resources.gameobjects.Player;
import de.jp.infoprojekt.util.FloatPoint;

import java.awt.*;
import java.awt.event.KeyEvent;

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
    private GameResource blockArea;

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

    public void setBlockingArea(GameResource blockArea) {
        this.blockArea = blockArea;
    }

    private void handleKeyInputs() {
        float finalPlayerSpeed = playerMovementSpeed * (isSprinting ? playerMovementSpeedSprintMultiplier : 1);

        FloatPoint newRelPos = new FloatPoint(getRelativeX(), getRelativeY());

        isSprinting = engine.getGameKeyHandler().isKeyDown(KeyEvent.VK_SHIFT);

        boolean change = false;

        if (engine.getGameKeyHandler().isKeyDown(KeyEvent.VK_A)) {
            newRelPos.setX(getRelativeX() - finalPlayerSpeed);
            flipPlayerImage = true;
            change = true;
        }else if (engine.getGameKeyHandler().isKeyDown(KeyEvent.VK_D)) {
            newRelPos.setX(getRelativeX() + finalPlayerSpeed);
            flipPlayerImage = false;
            change = true;
        }

        if (engine.getGameKeyHandler().isKeyDown(KeyEvent.VK_W)) {
            newRelPos.setY(getRelativeY() - finalPlayerSpeed);
            change = true;
        }else if (engine.getGameKeyHandler().isKeyDown(KeyEvent.VK_S)) {
            newRelPos.setY(getRelativeY() + finalPlayerSpeed);
            change = true;
        }

        if (change) {
            newRelPos = bindToAllowedArea(new FloatPoint(getRelativeX(), getRelativeY()), newRelPos);
            setRelativeLocation(newRelPos);
        }
    }

    //TODO clean up this mess if time
    private FloatPoint bindToAllowedArea(FloatPoint oldPoint, FloatPoint newPoint) {
        if (blockArea == null) {
            return newPoint;
        }

        int x = (int) (blockArea.getResource().getWidth() * oldPoint.getX());
        int y = (int) (blockArea.getResource().getHeight() * oldPoint.getY());

        int newX = (int) (blockArea.getResource().getWidth() * newPoint.getX());
        int newY = (int) (blockArea.getResource().getHeight() * newPoint.getY());

        x = Math.max(0, Math.min(blockArea.getResource().getWidth() - 1, x));
        y = Math.max(0, Math.min(blockArea.getResource().getHeight() - 1, y));

        newX = Math.max(0, Math.min(blockArea.getResource().getWidth() - 1, newX));
        newY = Math.max(0, Math.min(blockArea.getResource().getHeight() - 1, newY));

        if (isNonTransparent(blockArea.getResource().getRGB(newX, newY))) {
            return newPoint;
        }else if (isNonTransparent(blockArea.getResource().getRGB(x, newY))) {
            return new FloatPoint(oldPoint.getX(), newPoint.getY());
        }else if (isNonTransparent(blockArea.getResource().getRGB(newX, y))) {
            return new FloatPoint(newPoint.getX(), oldPoint.getY());
        }else {
            return oldPoint;
        }
    }

    private boolean isNonTransparent(int rgb) {
        return (rgb >> 24) != 0x00;
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

        int x = flipPlayerImage ? player.getWidth() : 0;
        int y = 0;
        int width = flipPlayerImage ? -player.getWidth() : player.getWidth();
        int height = player.getHeight();

        //Temp Scaling //TODO
        /*float scale = (float) (getRelativeY() + 0.5 / 2); //0-1
        System.out.println(scale);
        scale = Math.max(0, Math.min(1, scale));

        int newHeight = (int) (height * scale);
        y = height - newHeight;
        height = newHeight;*/

        g.drawImage(player.getResource(), x, y, width, height, null);

        //TODO remove debug
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
