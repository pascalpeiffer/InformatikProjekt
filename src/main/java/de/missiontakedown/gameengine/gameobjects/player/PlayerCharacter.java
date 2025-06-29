package de.missiontakedown.gameengine.gameobjects.player;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.gameobjects.AbstractGameObject;
import de.missiontakedown.gameengine.tick.GameTick;
import de.missiontakedown.resources.GameAudioResource;
import de.missiontakedown.resources.GameResource;
import de.missiontakedown.resources.ScalingEvent;
import de.missiontakedown.resources.gameobjects.PlayerResource;
import de.missiontakedown.util.FloatPoint;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Pascal
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
    private GameResource gameResource = PlayerResource.PLAYER;
    private boolean flipPlayerImage = false;
    private float playerScalingConstant = 1;

    private float scaling = 1;

    private boolean night;

    private GameAudioResource playerSteppingSound;

    public PlayerCharacter(GameEngine engine) {
        this.engine = engine;
        updateSize();
    }

    private int lastMoveTick = 0;
    private GameAudioResource.Instance steppingSound;
    public void tick(long currentTick) {
        if (isMoveable) {
            handleKeyInputs();
        }

        if (moving) {

            if (playerSteppingSound != null && steppingSound == null) {
                steppingSound = playerSteppingSound.create().loop(Clip.LOOP_CONTINUOUSLY).play();
            }

            lastMoveTick++;
            if (lastMoveTick >= 10) {
                lastMoveTick = 0;

                if (gameResource == PlayerResource.PLAYER || gameResource == PlayerResource.PLAYER_NIGHT) {
                    gameResource = night ? PlayerResource.PLAYER_MOVEMENT_NIGHT : PlayerResource.PLAYER_MOVEMENT;
                }else {
                    gameResource = night ? PlayerResource.PLAYER_NIGHT : PlayerResource.PLAYER;
                }
                repaint();
            }
        }else {

            if (steppingSound != null) {
                steppingSound.stop();
                steppingSound = null;
            }

            if (gameResource == PlayerResource.PLAYER_MOVEMENT) {
                gameResource = PlayerResource.PLAYER;
                repaint();
            }else if (gameResource == PlayerResource.PLAYER_MOVEMENT_NIGHT) {
                gameResource = PlayerResource.PLAYER_NIGHT;
                repaint();
            }

        }

        if (night) {
            if (gameResource == PlayerResource.PLAYER) {
                gameResource = PlayerResource.PLAYER_NIGHT;
                repaint();
            }else if (gameResource == PlayerResource.PLAYER_MOVEMENT) {
                gameResource = PlayerResource.PLAYER_MOVEMENT_NIGHT;
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

    private FloatPoint bindToAllowedArea(FloatPoint oldPoint, FloatPoint newPoint) {
        if (blockArea == null) {
            return newPoint;
        }

        BufferedImage resource = blockArea.getResource();
        int width = resource.getWidth();
        int height = resource.getHeight();

        int oldX = clamp((int) (width * oldPoint.getX()), 0, width - 1);
        int oldY = clamp((int) (height * oldPoint.getY()), 0, height - 1);

        int newX = clamp((int) (width * newPoint.getX()), 0, width - 1);
        int newY = clamp((int) (height * newPoint.getY()), 0, height - 1);

        if (isNonTransparent(resource.getRGB(newX, newY))) {
            return newPoint;
        } else if (isNonTransparent(resource.getRGB(oldX, newY))) {
            return new FloatPoint(oldPoint.getX(), newPoint.getY());
        } else if (isNonTransparent(resource.getRGB(newX, oldY))) {
            return new FloatPoint(newPoint.getX(), oldPoint.getY());
        } else {
            return oldPoint;
        }
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
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

        int x = 0;
        int y = 0;
        int width = (int) (flipPlayerImage ? -gameResource.getWidth() * scaling : gameResource.getWidth() * scaling);
        int height = (int) (gameResource.getHeight() * scaling);

        //Player Size Scaling
        float scale = getRelativeY();
        scale = playerScalingConstant + (scale - 0) * (1 - playerScalingConstant);
        width = (int) (width * scale);
        int newHeight = (int) (height * scale);
        y = height - newHeight;
        height = newHeight;

        x += (getWidth() + -width) / 2;

        g.drawImage(gameResource.getResource(), x, y, width, height, null);

    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        if (gameResource != null) {
            updateSize();
        }
        super.scale(widthMultiply, heightMultiply);
    }

    private void updateSize() {
        setSize((int) (gameResource.getWidth() * scaling), (int) (gameResource.getHeight() * scaling));
    }

    public void setPlayerSteppingSound(GameAudioResource playerSteppingSound) {
        this.playerSteppingSound = playerSteppingSound;
    }

    public void setPlayerScalingConstant(float playerScalingConstant) {
        this.playerScalingConstant = playerScalingConstant;
    }

    public void setScaling(float scaling) {
        this.scaling = scaling;
        updateSize();
        repaint();
    }

    public float getScaling() {
        return scaling;
    }

    public void setNight(boolean night) {
        this.night = night;
        repaint();
    }

    public boolean isNight() {
        return night;
    }
}
