package de.jp.infoprojekt.gameengine.scenes.spawn;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.dialog.spawn.IntroductionCallDialog;
import de.jp.infoprojekt.gameengine.gameobjects.interaction.MissionTakeDownFile;
import de.jp.infoprojekt.gameengine.gameobjects.money.MoneyOverlay;
import de.jp.infoprojekt.gameengine.gameobjects.player.PlayerCharacter;
import de.jp.infoprojekt.gameengine.gameobjects.interaction.InteractionHint;
import de.jp.infoprojekt.gameengine.scenes.AbstractScene;
import de.jp.infoprojekt.gameengine.scenes.gameovershotdead.GameOverShotDeadScene;
import de.jp.infoprojekt.gameengine.scenes.util.ColorScene;
import de.jp.infoprojekt.gameengine.state.GameState;
import de.jp.infoprojekt.gameengine.tick.GameTick;
import de.jp.infoprojekt.resources.GameAudioResource;
import de.jp.infoprojekt.resources.GameResource;
import de.jp.infoprojekt.resources.ResourceManager;
import de.jp.infoprojekt.resources.ScalingEvent;
import de.jp.infoprojekt.resources.interaction.InteractionResource;
import de.jp.infoprojekt.resources.scenes.SpawnSceneResource;
import de.jp.infoprojekt.util.FloatPoint;

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

    private MoneyOverlay moneyOverlay;
    private MissionTakeDownFile missionTakedownFile;
    private PlayerCharacter player;
    private InteractionHint phoneInteractionHint;

    public SpawnScene(GameEngine engine) {
        this.engine = engine;
        setLayout(null);

        initGameObjects();

        ResourceManager.addScalingListener(this);
    }

    // -- INITIALIZE --

    private void initGameObjects() { //Order matters! -> the top on is on top (huh!?)
        initMissionTakedownFile();
        initMoneyOverlay();
        initPlayer();
        initPhoneInteractionHint();
    }

    private void initPhoneInteractionHint() {
        phoneInteractionHint = new InteractionHint(KeyEvent.getKeyText(engine.getKeyMappingSettings().INTERACT));
        phoneInteractionHint.setRelativeLocation(new FloatPoint(0.9f, 0.5f));
        engine.getTickProvider().registerTick(phoneInteractionHint);
        phoneInteractionHint.setVisible(false);
        add(phoneInteractionHint);
    }

    private void initMoneyOverlay() {
        moneyOverlay = new MoneyOverlay(engine);
        if (engine.getStateManager().getState().getId() < 10) {
            moneyOverlay.setVisible(false);
        }
        add(moneyOverlay);
    }

    private void initMissionTakedownFile() {
        missionTakedownFile = new MissionTakeDownFile();
        missionTakedownFile.setRelativeLocation(new FloatPoint(0.5f, 0.95f));
        missionTakedownFile.setVisible(false);
        add(missionTakedownFile);
    }

    private void initPlayer() {
        player = new PlayerCharacter(engine);
        player.setRelativeLocation(0.5f, 0.9f);
        player.setMoveable(true);
        player.setBlockingArea(SpawnSceneResource.PLAYER_SPACE);
        player.setPlayerSteppingSound(SpawnSceneResource.PLAYER_STEPPING);
        player.setPlayerScalingConstant(0.6f);
        add(player);
    }

    // -- GAME LOOP (TICK) --

    @Override
    public void tick(long tick) {
        phoneTick(player.getRGBOnBlockArea() == Color.RED.getRGB());
        doorTick(player.getRGBOnBlockArea() == Color.GREEN.getRGB());
        missionFileTick();
        missionRefuseTick();
    }

    private void missionFileTick() {
        if (engine.getStateManager().getState().getId() < GameState.GAME_INTRODUCED.getId()) {
            return;
        }

        if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().OPEN_FILE)) {
            if (!missionTakedownFile.isShowing()) {
                InteractionResource.FILE_INTERACTION.create().play();
                missionTakedownFile.setVisible(true);
                player.setMoveable(false);
            }
        }else {
            if (missionTakedownFile.isShowing()) {
                missionTakedownFile.setVisible(false);
                player.setMoveable(true);
            }
        }
    }

    private void doorTick(boolean isPlayerNearby) {
        if (engine.getStateManager().getState() == GameState.MISSION_REFUSED_DOOR_KNOCKED && isPlayerNearby) {
            if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                engine.getStateManager().setState(GameState.MISSION_REFUSED_DOOR_ANSWERED);
            }
        }
    }

    private int introductionPhoneCallCooldown = 0;
    private GameAudioResource.Instance currentCallAudio;
    private void phoneTick(boolean isPlayerNearby) {
        if (engine.getStateManager().getState() == GameState.GAME_ENTRY && introductionPhoneCallCooldown <= 0) {
            engine.getStateManager().setState(GameState.INTRODUCTION_CALLING);
            phoneInteractionHint.setVisible(true);

            currentCallAudio = SpawnSceneResource.PHONE.create().setPan(0.5f).onEnd(() -> {
                if (engine.getStateManager().getState() == GameState.INTRODUCTION_CALLING) {
                    introductionPhoneCallCooldown = engine.getTickProvider().getTicksPerSecond() * 15;
                    engine.getStateManager().setState(GameState.GAME_ENTRY);
                    phoneInteractionHint.setVisible(false);
                }
            }).play();

        }else if (engine.getStateManager().getState() == GameState.GAME_ENTRY) {
            introductionPhoneCallCooldown--;
        }


        if (isPlayerNearby && engine.getStateManager().getState() == GameState.INTRODUCTION_CALLING && engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
            engine.getStateManager().setState(GameState.INTRODUCTION_CALL);
            phoneInteractionHint.setVisible(false);
            if (currentCallAudio != null && currentCallAudio.isActive()) {
                currentCallAudio.stop();
            }
            player.setMoveable(false);
            player.setFlipPlayerImage(false);
            SwingUtilities.invokeLater(() -> {
                IntroductionCallDialog callDialog = new IntroductionCallDialog(engine, this);
                engine.getDialogManager().setDialog(callDialog);
            });
        }
    }

    private int missionRefuseDoorKnockCooldown;
    private void missionRefuseTick() {
        if (engine.getStateManager().getState() == GameState.MISSION_REFUSED_DOOR_ANSWERED) {
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
                    SpawnSceneResource.DRAW_AND_SHOOT.create().onEnd(() -> {
                        SwingUtilities.invokeLater(() -> {
                            engine.getGraphics().switchToScene(new GameOverShotDeadScene());
                        });
                    }).play();
                });
            }).start();
        }


        if (engine.getStateManager().getState() == GameState.MISSION_REFUSED) {
            if (missionRefuseDoorKnockCooldown <= 0) {
                SpawnSceneResource.DOOR_KNOCKING.create().play();
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
                SpawnSceneResource.DOOR_KICKDOWN.create().onEnd(() -> {
                    SpawnSceneResource.FLASH_BANG_AND_BEEP.create().play();
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
                }).play();
            }
            missionRefuseDoorKnockCooldown--;
        }
    }

    // -- HANDLE EVENTS --

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
    }

    @Override
    public void scale(float width, float height) {
        repaint();
    }

    // -- GETTER / SETTER --

    public PlayerCharacter getPlayer() {
        return player;
    }

    public MissionTakeDownFile getMissionTakedownFile() {
        return missionTakedownFile;
    }

    public MoneyOverlay getMoneyOverlay() {
        return moneyOverlay;
    }
}
