package de.jp.infoprojekt.gameengine.gameobjects.player;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.gameobjects.AbstractGameObject;
import de.jp.infoprojekt.gameengine.tick.GameTick;
import de.jp.infoprojekt.resources.GameAudioResource;
import de.jp.infoprojekt.resources.GameResource;
import de.jp.infoprojekt.resources.ScalingEvent;
import de.jp.infoprojekt.resources.gameobjects.Player;
import de.jp.infoprojekt.util.FloatPoint;

import javax.sound.sampled.Clip;
import java.awt.*;

/**
 * PlayerCharacter class
 *
 * @author Pascal
 * @version 24.06.2025
 */
public class PlayerCharacter extends AbstractGameObject implements ScalingEvent, GameTick {

    private final GameEngine engine;

    //Movement
    private boolean isMoveable = false; //Are we listening to key inputs?
    private float playerMovementSpeed = 0.004f;
    private boolean isSprinting = false;
    private float playerMovementSpeedSprintMultiplier = 1.5f;
    private GameResource blockArea;
    private boolean moving = false;

    //Render
    private GameResource gameResource = Player.PLAYER;
    private boolean flipPlayerImage = false;
    private float playerScalingConstant = 1;


    private GameAudioResource playerSteppingSound;

    public PlayerCharacter(GameEngine engine) {
        this.engine = engine;
        setSize(gameResource.getWidth(), gameResource.getHeight());
    }

    private int lastMoveTick = 0;
    private Clip steppingSound;
    public void tick(long currentTick) {
        if (isMoveable) {
            handleKeyInputs();
        }

        if (moving) {

            if (playerSteppingSound != null && steppingSound == null) {
                steppingSound = playerSteppingSound.play(0.3f);
                steppingSound.loop(Clip.LOOP_CONTINUOUSLY);
                steppingSound.start();
            }

            lastMoveTick++;
            if (lastMoveTick >= 10) {
                lastMoveTick = 0;

                if (gameResource == Player.PLAYER) {
                    gameResource = Player.PLAYER_MOVEMENT;
                }else {
                    gameResource = Player.PLAYER;
                }
                repaint();
            }
        }else {

            if (steppingSound != null) {
                steppingSound.stop();
                steppingSound = null;
            }

            if (gameResource == Player.PLAYER_MOVEMENT) {
                gameResource = Player.PLAYER;
                repaint();
            }
        }
    }

    public void stopSteppingSound() {
        if (steppingSound != null) {
            steppingSound.stop();
        }
    }

    public void setBlockingArea(GameResource blockArea) {
        this.blockArea = blockArea;
    }

    private void handleKeyInputs() {
        float finalPlayerSpeed = playerMovementSpeed * (isSprinting ? playerMovementSpeedSprintMultiplier : 1);

        FloatPoint newRelPos = new FloatPoint(getRelativeX(), getRelativeY());

        isSprinting = engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().SPRINT);

        boolean change = false;

        if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().LEFT_KEY)) {
            newRelPos.setX(getRelativeX() - finalPlayerSpeed);
            flipPlayerImage = true;
            change = true;
        }else if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().RIGHT_KEY)) {
            newRelPos.setX(getRelativeX() + finalPlayerSpeed);
            flipPlayerImage = false;
            change = true;
        }

        if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().FORWARD_KEY)) {
            newRelPos.setY(getRelativeY() - finalPlayerSpeed);
            change = true;
        }else if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().BACKWARD_KEY)) {
            newRelPos.setY(getRelativeY() + finalPlayerSpeed);
            change = true;
        }

        if (change) {
            moving = true;
            newRelPos = bindToAllowedArea(new FloatPoint(getRelativeX(), getRelativeY()), newRelPos);
            setRelativeLocation(newRelPos);
        }else {
            moving = false;
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

    public int getRGBOnBlockArea() {
        int x = (int) (blockArea.getResource().getWidth() * getRelativeX());
        int y = (int) (blockArea.getResource().getHeight() * getRelativeY());

        x = Math.max(0, Math.min(blockArea.getResource().getWidth() - 1, x));
        y = Math.max(0, Math.min(blockArea.getResource().getHeight() - 1, y));

        return blockArea.getResource().getRGB(x, y);
    }

    private boolean isNonTransparent(int rgb) {
        return (rgb >> 24) != 0x00;
    }

    public boolean isMoveable() {
        return isMoveable;
    }

    public void setMoveable(boolean moveable) {
        isMoveable = moveable;
        if (!moveable) {
            moving = false;
        }
    }

    public void setFlipPlayerImage(boolean flipPlayerImage) {
        this.flipPlayerImage = flipPlayerImage;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x = flipPlayerImage ? gameResource.getWidth() : 0;
        int y = 0;
        int width = flipPlayerImage ? -gameResource.getWidth() : gameResource.getWidth();
        int height = gameResource.getHeight();

        //Player Size Scaling
        float scale = getRelativeY();
        scale = playerScalingConstant + (scale - 0) * (1 - playerScalingConstant);
        width = (int) (width * scale);
        int newHeight = (int) (height * scale);
        y = height - newHeight;
        height = newHeight;

        g.drawImage(gameResource.getResource(), x, y, width, height, null);

        //TODO remove debug
        //g.setColor(Color.RED);
        //g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    @Override
    public void scale(float width, float height) {
        if (gameResource != null) {
            setSize(gameResource.getWidth(), gameResource.getHeight());
        }
        super.scale(width, height);
    }

    public void setPlayerSteppingSound(GameAudioResource playerSteppingSound) {
        this.playerSteppingSound = playerSteppingSound;
    }

    public void setPlayerScalingConstant(float playerScalingConstant) {
        this.playerScalingConstant = playerScalingConstant;
    }
}
