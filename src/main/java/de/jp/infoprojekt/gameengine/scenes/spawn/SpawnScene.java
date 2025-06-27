package de.jp.infoprojekt.gameengine.scenes.spawn;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.gameobjects.interaction.MissionTakeDownFile;
import de.jp.infoprojekt.gameengine.gameobjects.player.PlayerCharacter;
import de.jp.infoprojekt.gameengine.graphics.popup.dialog.spawn.IntroductionCallDialog;
import de.jp.infoprojekt.gameengine.gameobjects.interaction.InteractionHint;
import de.jp.infoprojekt.gameengine.scenes.AbstractScene;
import de.jp.infoprojekt.gameengine.scenes.gameovershotdead.GameOverShotDeadScene;
import de.jp.infoprojekt.gameengine.scenes.util.ColorScene;
import de.jp.infoprojekt.gameengine.state.GameState;
import de.jp.infoprojekt.gameengine.tick.GameTick;
import de.jp.infoprojekt.resources.GameResource;
import de.jp.infoprojekt.resources.ResourceManager;
import de.jp.infoprojekt.resources.ScalingEvent;
import de.jp.infoprojekt.resources.scenes.SpawnSceneResource;
import de.jp.infoprojekt.util.FloatPoint;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * SpawnScene class
 *
 * @author Pascal
 * @version 24.06.2025
 */
public class SpawnScene extends AbstractScene implements ScalingEvent, GameTick {

    private final GameEngine engine;

    private GameResource spawnBackground = SpawnSceneResource.BACKGROUND;

    private final PlayerCharacter player;
    private final MissionTakeDownFile missionTakeDownFile;

    public SpawnScene(GameEngine engine) {
        this.engine = engine;
        setLayout(null);

        InteractionHint hint = new InteractionHint("E");
        hint.setRelativeLocation(new FloatPoint(0.92f, 0.55f));
        engine.getTickProvider().registerTick(hint);
        //add(hint);


        missionTakeDownFile = new MissionTakeDownFile();
        missionTakeDownFile.setRelativeLocation(new FloatPoint(0.5f, 0.95f));
        missionTakeDownFile.setVisible(false);
        add(missionTakeDownFile);

        player = new PlayerCharacter(engine);
        player.setRelativeLocation(0.5f, 0.9f);
        player.setMoveable(true);
        player.setBlockingArea(SpawnSceneResource.PLAYER_SPACE);
        player.setPlayerSteppingSound(SpawnSceneResource.PLAYER_STEPPING);
        player.setPlayerScalingConstant(0.6f);
        add(player);

        ResourceManager.addScalingListener(this);
    }

    @Override
    public void tick(long tick) {
        handlePhoneTick(player.getRGBOnBlockArea() == Color.RED.getRGB());
        handleMissionRefuse();
        handleDoor(player.getRGBOnBlockArea() == Color.GREEN.getRGB());
        handleMissionFile();
    }

    private void handleMissionFile() {
        if (engine.getStateManager().getState().getId() < 10) {
            //return;
        }

        if (engine.getGameKeyHandler().isKeyDown(KeyEvent.VK_TAB)) {
            missionTakeDownFile.setVisible(true);
        }else {
            missionTakeDownFile.setVisible(false);
        }
    }

    private void handleDoor(boolean isPlayerNearby) {
        if (engine.getStateManager().getState() == GameState.MISSION_REFUSED_DOOR_KNOCKED && isPlayerNearby) {
            if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                engine.getStateManager().setState(GameState.GAME_OVER);
                spawnBackground = SpawnSceneResource.BACKGROUND_DOOR_HALF;
                repaint();
                new Thread(() -> {
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    SwingUtilities.invokeLater(() -> {
                        spawnBackground = SpawnSceneResource.BACKGROUND_AGENTS;
                        repaint();
                        SpawnSceneResource.DRAW_AND_SHOOT.play(2f, () -> {
                            SwingUtilities.invokeLater(() -> {
                                engine.getGraphics().switchToScene(new GameOverShotDeadScene());
                            });
                        });
                    });
                }).start();
            }
        }
    }

    private int missionRefuseDoorKnockCooldown;
    private void handleMissionRefuse() {
        if (engine.getStateManager().getState() == GameState.MISSION_REFUSED) {
            if (missionRefuseDoorKnockCooldown <= 0) {
                SpawnSceneResource.DOOR_KNOCKING.play();
                engine.getStateManager().setState(GameState.MISSION_REFUSED_DOOR_KNOCKED);

                //Time until door is busted
                missionRefuseDoorKnockCooldown = engine.getTickProvider().getTicksPerSecond() * 10;
            }
            missionRefuseDoorKnockCooldown--;
        }else if (engine.getStateManager().getState() == GameState.MISSION_REFUSED_DOOR_KNOCKED) {
            if (missionRefuseDoorKnockCooldown <= 0) {
                engine.getStateManager().setState(GameState.GAME_OVER);
                spawnBackground = SpawnSceneResource.BACKGROUND_DOOR_HALF;
                repaint();
                SpawnSceneResource.DOOR_KICKDOWN.play(0.6f, () -> {
                    SpawnSceneResource.FLASH_BANG_AND_BEEP.play();
                    SwingUtilities.invokeLater(() -> {
                        spawnBackground = SpawnSceneResource.BACKGROUND_AGENTS;
                        repaint();
                    });
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    remove(player);
                    ColorScene cs = new ColorScene(Color.WHITE);
                    cs.setSize(ResourceManager.getGameScreenWidth(), ResourceManager.getGameScreenHeight());
                    add(cs);
                    repaint();

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    SwingUtilities.invokeLater(() -> {
                        engine.getGraphics().switchToScene(new GameOverShotDeadScene());
                    });
                });
            }
            missionRefuseDoorKnockCooldown--;
        }
    }

    private int introductionPhoneCallCooldown;
    private Clip currentCallAudio;
    private void handlePhoneTick(boolean isPlayerNearby) {
        if (engine.getStateManager().getState() == GameState.GAME_ENTRY && introductionPhoneCallCooldown <= 0) {
            engine.getStateManager().setState(GameState.INTRODUCTION_CALLING);
            currentCallAudio = SpawnSceneResource.PHONE.play(1.4f, () -> {
                if (engine.getStateManager().getState() == GameState.INTRODUCTION_CALLING) {
                    introductionPhoneCallCooldown = engine.getTickProvider().getTicksPerSecond() * 15;
                    engine.getStateManager().setState(GameState.GAME_ENTRY);
                }
            });

            FloatControl pan = (FloatControl) currentCallAudio.getControl(FloatControl.Type.PAN);
            if (pan != null) {
                pan.setValue(0.5f);
            }

        }else if (engine.getStateManager().getState() == GameState.GAME_ENTRY) {
            introductionPhoneCallCooldown--;
        }


        if (isPlayerNearby && engine.getStateManager().getState() == GameState.INTRODUCTION_CALLING && engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
            engine.getStateManager().setState(GameState.INTRODUCTION_CALL);
            if (currentCallAudio != null && currentCallAudio.isActive()) {
                currentCallAudio.stop();
            }
            player.setMoveable(false);
            player.setFlipPlayerImage(false);
            SwingUtilities.invokeLater(() -> {
                IntroductionCallDialog callDialog = new IntroductionCallDialog(engine);
                engine.getDialogManager().setDialog(callDialog);
                callDialog.playMessage(0);
            });
        }
    }

    @Override
    public void sceneShown() {
        engine.getTickProvider().registerTick(this);
        engine.getTickProvider().registerTick(player);

        missionRefuseDoorKnockCooldown = engine.getTickProvider().getTicksPerSecond() * 10;
    }

    @Override
    public void sceneHidden() {
        engine.getTickProvider().unregisterTick(this);
        engine.getTickProvider().unregisterTick(player);
        player.stopSteppingSound();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(spawnBackground.getResource(), 0, 0, getWidth(), getHeight(), null);
        //g.setColor(Color.RED);
        //g.drawLine(getWidth() / 2,0, getWidth() / 2, getHeight());
    }

    @Override
    public void scale(float width, float height) {
        repaint();
    }

    public PlayerCharacter getPlayer() {
        return player;
    }

    public MissionTakeDownFile getMissionTakeDownFile() {
        return missionTakeDownFile;
    }
}
