package de.jp.infoprojekt.gameengine.scenes.spawn;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.dialog.spawn.ColaGlycerinDialog;
import de.jp.infoprojekt.gameengine.dialog.spawn.FinalDialog;
import de.jp.infoprojekt.gameengine.dialog.spawn.RememberFogFluidDialog;
import de.jp.infoprojekt.gameengine.dialog.spawn.IntroductionCallDialog;
import de.jp.infoprojekt.gameengine.gameobjects.interaction.MissionTakeDownFile;
import de.jp.infoprojekt.gameengine.gameobjects.overlay.DetonatorOverlay;
import de.jp.infoprojekt.gameengine.gameobjects.overlay.InventoryOverlay;
import de.jp.infoprojekt.gameengine.gameobjects.overlay.MoneyOverlay;
import de.jp.infoprojekt.gameengine.gameobjects.overlay.QuestOverlay;
import de.jp.infoprojekt.gameengine.gameobjects.player.PlayerCharacter;
import de.jp.infoprojekt.gameengine.gameobjects.interaction.InteractionHint;
import de.jp.infoprojekt.gameengine.graphics.fade.BlackFade;
import de.jp.infoprojekt.gameengine.inventory.Item;
import de.jp.infoprojekt.gameengine.scenes.AbstractScene;
import de.jp.infoprojekt.gameengine.scenes.farmer.FarmerScene;
import de.jp.infoprojekt.gameengine.scenes.gameovershotdead.GameOverShotDeadScene;
import de.jp.infoprojekt.gameengine.scenes.headquarter.HeadquarterScene;
import de.jp.infoprojekt.gameengine.scenes.lab.LabScene;
import de.jp.infoprojekt.gameengine.scenes.travel.TravelScene;
import de.jp.infoprojekt.gameengine.scenes.util.ColorScene;
import de.jp.infoprojekt.gameengine.state.GameState;
import de.jp.infoprojekt.gameengine.state.QuestState;
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
    private DetonatorOverlay detonatorOverlay;

    private PlayerCharacter player;
    private InteractionHint phoneInteractionHint;
    private InteractionHint computerInteractionHint;
    private InteractionHint doorInteractionHint;
    private InteractionHint fridgeInteractionHint;
    private InteractionHint bedInteractionHint;

    public SpawnScene(GameEngine engine) {
        this.engine = engine;
        setLayout(null);

        initGameObjects();

        ResourceManager.addScalingListener(this);


        introductionPhoneCallCooldown = engine.getTickProvider().getTicksPerSecond() * 5;
    }

    // -- INITIALIZE --

    private void initGameObjects() { //Order matters! -> the top on is on top (huh!?)
        initMissionTakedownFile();
        initDetonatorOverlay();
        initQuestOverlay();
        initMoneyOverlay();
        initInventoryOverlay();
        initBedInteractionHint();
        initFridgeInteractionHint();
        initDoorInteractionHint();
        initComputerInteractionHint();
        initPhoneInteractionHint();
        initPlayer();
    }

    private void initDetonatorOverlay() {
        detonatorOverlay = new DetonatorOverlay(engine);
        detonatorOverlay.setVisible(engine.getStateManager().getState().getId() >= GameState.PLACED_BOMB.getId());
        add(detonatorOverlay);
    }

    private void initBedInteractionHint() {
        bedInteractionHint = new InteractionHint("Bett" +" (" + KeyEvent.getKeyText(engine.getKeyMappingSettings().INTERACT) + ")");
        bedInteractionHint.setVisible(false);
        add(bedInteractionHint);
    }

    private void initFridgeInteractionHint() {
        fridgeInteractionHint = new InteractionHint("Kühlschrank" +" (" + KeyEvent.getKeyText(engine.getKeyMappingSettings().INTERACT) + ")");
        fridgeInteractionHint.setVisible(false);
        add(fridgeInteractionHint);
    }

    private void initInventoryOverlay() {
        add(new InventoryOverlay(engine));
    }

    private void initQuestOverlay() {
        QuestOverlay questOverlay = new QuestOverlay(engine);
        add(questOverlay);
    }

    private void initDoorInteractionHint() {
        doorInteractionHint = new InteractionHint("Tür" +" (" + KeyEvent.getKeyText(engine.getKeyMappingSettings().INTERACT) + ")");
        doorInteractionHint.setVisible(false);
        add(doorInteractionHint);
    }

    private void initComputerInteractionHint() {
        computerInteractionHint = new InteractionHint("Computer" +" (" + KeyEvent.getKeyText(engine.getKeyMappingSettings().INTERACT) + ")");
        computerInteractionHint.setVisible(false);
        add(computerInteractionHint);
    }

    private void initPhoneInteractionHint() {
        phoneInteractionHint = new InteractionHint("Telfon" +" (" + KeyEvent.getKeyText(engine.getKeyMappingSettings().INTERACT) + ")");
        phoneInteractionHint.setVisible(false);
        add(phoneInteractionHint);
    }

    private void initMoneyOverlay() {
        moneyOverlay = new MoneyOverlay(engine);
        if (engine.getStateManager().getState().getId() < GameState.GAME_INTRODUCED.getId()) {
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
        backFromFarmerTick();
        phoneTick(player.getRGBOnBlockArea() == Color.RED.getRGB());
        doorTick(player.getRGBOnBlockArea() == Color.GREEN.getRGB());
        missionFileTick();
        missionRefuseTick();
        computerTick(player.getRGBOnBlockArea() == Color.BLUE.getRGB());
        fridgeTick(player.getRGBOnBlockArea() == new Color(255,0,255).getRGB());
        bedTick(player.getRGBOnBlockArea() == new Color(0,255,255).getRGB());
        boughtSulfuricAcidTick();
    }

    private void bedTick(boolean isPlayerNearby) {
        boolean intHint = false;

        if (engine.getStateManager().getState() == GameState.GO_TO_REST) {
            if (isPlayerNearby) {
                intHint = true;
                if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                    engine.getStateManager().setState(GameState.RESTED);
                    engine.getStateManager().setQuest(QuestState.NO_QUEST);
                    engine.getGraphics().switchToScene(new SpawnScene(engine), new BlackFade(engine, 4000, () -> {
                        engine.getStateManager().setQuest(QuestState.PLACE_BOMB);
                    }));
                }
            }
        }

        bedInteractionHint.setVisible(intHint);
    }

    private int fridgeColaCooldown;
    private void fridgeTick(boolean isPlayerNearby) {
        boolean intHint = false;

        if (engine.getStateManager().getState() == GameState.NEED_COLA && isPlayerNearby) {
            intHint = true;
            if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                if (spawnBackground != SpawnSceneResource.BACKGROUND_FRIDGE) {
                    spawnBackground = SpawnSceneResource.BACKGROUND_FRIDGE;
                    repaint();
                    fridgeColaCooldown = (int) (engine.getTickProvider().getTicksPerSecond() * 0.5f);
                    SpawnSceneResource.FRIDGE.create().play();
                }
            }

            if (spawnBackground == SpawnSceneResource.BACKGROUND_FRIDGE) {
                if (fridgeColaCooldown > 0) {
                    intHint = false;
                    fridgeColaCooldown--;
                }else {
                    if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                        //Drink Cola
                        spawnBackground = SpawnSceneResource.BACKGROUND;
                        repaint();
                        engine.getInventoryManager().addItem(new Item(Item.Type.ColaEmpty));
                        engine.getStateManager().setState(GameState.DRANK_COLA);
                        engine.getStateManager().setQuest(QuestState.NO_QUEST);
                        SpawnSceneResource.COLA_DRINK.create().onEnd(() -> {
                            //After sound
                            RememberFogFluidDialog dialog = new RememberFogFluidDialog(engine, this);
                            engine.getDialogManager().setDialog(dialog);
                            engine.getStateManager().setState(GameState.REMEMBER_FOG_FLUID);
                        }).play();
                    }
                }
            }
        }else if (engine.getStateManager().getState() == GameState.DRINK_COLA && isPlayerNearby) {
            intHint = true;
            if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                if (spawnBackground != SpawnSceneResource.BACKGROUND_FRIDGE) {
                    spawnBackground = SpawnSceneResource.BACKGROUND_FRIDGE;
                    repaint();
                    fridgeColaCooldown = (int) (engine.getTickProvider().getTicksPerSecond() * 0.5f);
                    SpawnSceneResource.FRIDGE.create().play();
                }
            }

            if (spawnBackground == SpawnSceneResource.BACKGROUND_FRIDGE) {
                if (fridgeColaCooldown > 0) {
                    intHint = false;
                    fridgeColaCooldown--;
                }else {
                    if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                        //Drink Cola
                        spawnBackground = SpawnSceneResource.BACKGROUND;
                        repaint();
                        engine.getInventoryManager().addItem(new Item(Item.Type.ColaEmpty));
                        engine.getStateManager().setState(GameState.GO_TO_REST);
                        engine.getStateManager().setQuest(QuestState.GO_TO_REST);
                        SpawnSceneResource.COLA_DRINK.create().play();
                    }
                }
            }
        }

        fridgeInteractionHint.setVisible(intHint);
    }

    private void boughtSulfuricAcidTick() {
        if (engine.getStateManager().getState() == GameState.BUY_SULFURIC_ACID) {
            if (engine.getInventoryManager().findItemByType(Item.Type.SulfuricAcid).isPresent()) {
                ColaGlycerinDialog dialog = new ColaGlycerinDialog(engine, this);
                engine.getDialogManager().setDialog(dialog);
                engine.getStateManager().setState(GameState.BOUGHT_SULFURIC_ACID);
            }
        }


    }

    private void backFromFarmerTick() {
        if (engine.getStateManager().getState() == GameState.EARNING_MONEY) {
            engine.getStateManager().setState(GameState.CREATE_OXYGEN);
            engine.getStateManager().setQuest(QuestState.CREATE_OXYGEN);
        }
    }

    private void computerTick(boolean isPlayerNearby) {
        if (engine.getDialogManager().hasDialog()) {
            return;
        }

        if (isPlayerNearby && !engine.getDialogManager().hasDialog()) {

            if (engine.getStateManager().getState().getId() >= GameState.GAME_INTRODUCED.getId()) {
                computerInteractionHint.setVisible(true);

                if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                    engine.getGraphics().switchToScene(new ComputerScene(engine, player.getRelativeLocation()), new BlackFade(engine));
                }

            }else {
                computerInteractionHint.setVisible(false);
            }

        }else {
            computerInteractionHint.setVisible(false);
        }
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
        boolean intHint = false;

        if (engine.getStateManager().getState() == GameState.MISSION_REFUSED_DOOR_KNOCKED && isPlayerNearby) {
            intHint = true;
            if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                engine.getStateManager().setState(GameState.MISSION_REFUSED_DOOR_ANSWERED);
            }
        }

        if (engine.getStateManager().getState() == GameState.GO_TO_FARMER && isPlayerNearby) {
            intHint = true;
            if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                intHint = false;
                //Goto Farmer
                engine.getGraphics().switchToScene(new TravelScene(engine, true, 5, () -> {
                    engine.getGraphics().switchToScene(new FarmerScene(engine), new BlackFade(engine));
                }), new BlackFade(engine));
            }
        }

        if (engine.getStateManager().getState() == GameState.CREATE_OXYGEN && isPlayerNearby) {
            intHint = true;
            if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                //GOTO Lab
                engine.getGraphics().switchToScene(new LabScene(engine), new BlackFade(engine));
            }
        }

        if (engine.getStateManager().getState() == GameState.BOUGHT_FOG_FLUID && isPlayerNearby) {
            intHint = true;
            if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                //GOTO Lab
                engine.getGraphics().switchToScene(new LabScene(engine), new BlackFade(engine));
            }
        }

        if (engine.getStateManager().getState() == GameState.RESTED && isPlayerNearby) {
            intHint = true;
            if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                //GOTO Headquarter
                engine.getGraphics().switchToScene(new TravelScene(engine, true, 5, () -> {
                    engine.getGraphics().switchToScene(new HeadquarterScene(engine), new BlackFade(engine));
                }).setNight(true), new BlackFade(engine));
            }
        }

        doorInteractionHint.setVisible(isPlayerNearby && intHint);
    }

    private int introductionPhoneCallCooldown = 0;
    private GameAudioResource.Instance currentCallAudio;
    private void phoneTick(boolean isPlayerNearby) {
        boolean intHint = false;

        if (isPlayerNearby && currentCallAudio != null && currentCallAudio.isActive()) {
            intHint = true;
        }else {
            intHint = false;

            if (engine.getStateManager().getState() == GameState.CALL_G && isPlayerNearby) {
                intHint = true;
            }
        }

        if (engine.getStateManager().getState() == GameState.GAME_ENTRY && introductionPhoneCallCooldown <= 0) {
            engine.getStateManager().setState(GameState.INTRODUCTION_CALLING);

            currentCallAudio = SpawnSceneResource.PHONE.create().setPan(0.5f).onEnd(() -> {
                if (engine.getStateManager().getState() == GameState.INTRODUCTION_CALLING) {
                    introductionPhoneCallCooldown = engine.getTickProvider().getTicksPerSecond() * 15;
                    engine.getStateManager().setState(GameState.GAME_ENTRY);
                }
            }).play();

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
                IntroductionCallDialog callDialog = new IntroductionCallDialog(engine, this);
                engine.getDialogManager().setDialog(callDialog);
            });
        }

        if (isPlayerNearby && engine.getStateManager().getState() == GameState.CALL_G && engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
            engine.getStateManager().setState(GameState.CALLING_G);
            engine.getStateManager().setQuest(QuestState.NO_QUEST);
            player.setMoveable(false);
            player.setFlipPlayerImage(false);
            SpawnSceneResource.DIAL_UP.create().setPan(0.5f).onEnd(() -> {
                SwingUtilities.invokeLater(() -> {
                    FinalDialog finalDialog = new FinalDialog(engine, this);
                    engine.getDialogManager().setDialog(finalDialog);
                });
            }).play();
        }

        phoneInteractionHint.setVisible(intHint);
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

        if (engine.getStateManager().getState() == GameState.RESTED || engine.getStateManager().getState() == GameState.PLACED_BOMB) {
            player.setNight(true);
            spawnBackground = SpawnSceneResource.BACKGROUND_NIGHT;
            repaint();
        }
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
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(spawnBackground.getResource(), 0, 0, getWidth(), getHeight(), null);

        //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        //g2d.drawImage(SpawnSceneResource.PLAYER_SPACE.getResource(), 0, 0, getWidth(), getHeight(), null);

        g2d.dispose();
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

    public DetonatorOverlay getDetonatorOverlay() {
        return detonatorOverlay;
    }
}
