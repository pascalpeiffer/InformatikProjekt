package de.missiontakedown.gameengine.scenes.lab;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.dialog.lab.AllIngredientsDialog;
import de.missiontakedown.gameengine.dialog.lab.EnoughMoneyForSulfuricAcidDialog;
import de.missiontakedown.gameengine.dialog.lab.NitratorDialog;
import de.missiontakedown.gameengine.dialog.lab.NitroglycerinInColaDialog;
import de.missiontakedown.gameengine.gameobjects.interaction.InteractionHint;
import de.missiontakedown.gameengine.gameobjects.interaction.MissionTakeDownFile;
import de.missiontakedown.gameengine.gameobjects.overlay.InventoryOverlay;
import de.missiontakedown.gameengine.gameobjects.overlay.MoneyOverlay;
import de.missiontakedown.gameengine.gameobjects.overlay.QuestOverlay;
import de.missiontakedown.gameengine.gameobjects.player.PlayerCharacter;
import de.missiontakedown.gameengine.graphics.fade.BlackFade;
import de.missiontakedown.gameengine.inventory.Item;
import de.missiontakedown.gameengine.scenes.AbstractScene;
import de.missiontakedown.gameengine.scenes.hospital.HospitalScene;
import de.missiontakedown.gameengine.scenes.spawn.SpawnScene;
import de.missiontakedown.gameengine.state.GameState;
import de.missiontakedown.gameengine.state.QuestState;
import de.missiontakedown.gameengine.tick.GameTick;
import de.missiontakedown.resources.GameResource;
import de.missiontakedown.resources.ResourceManager;
import de.missiontakedown.resources.ScalingEvent;
import de.missiontakedown.resources.interaction.InteractionResource;
import de.missiontakedown.resources.scenes.LabSceneResource;
import de.missiontakedown.util.FloatPoint;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @author Pascal
 */
public class LabScene extends AbstractScene implements ScalingEvent, GameTick {

    private final GameEngine engine;

    private GameResource backgroundResource = LabSceneResource.BACKGROUND;

    private PlayerCharacter player;

    private InteractionHint doorInteractionHint;
    private InteractionHint workbenchInteractionHint;
    private InteractionHint shelfInteractionHint;

    private MoneyOverlay moneyOverlay;
    private QuestOverlay questOverlay;
    private InventoryOverlay inventoryOverlay;

    private MissionTakeDownFile missionTakedownFile;

    public LabScene(GameEngine engine) {
        this.engine = engine;
        setLayout(null);

        moneyOverlay = new MoneyOverlay(engine);
        add(moneyOverlay);

        questOverlay = new QuestOverlay(engine);
        add(questOverlay);

        inventoryOverlay = new InventoryOverlay(engine);
        add(inventoryOverlay);

        missionTakedownFile = new MissionTakeDownFile();
        missionTakedownFile.setRelativeLocation(new FloatPoint(0.5f, 0.95f));
        missionTakedownFile.setVisible(false);
        add(missionTakedownFile);


        addGameObjects();

        ResourceManager.addScalingListener(this);

        repaint();

        nitratorExplodedDelayTick = engine.getTickProvider().getTicksPerSecond() * 2;
    }

    private void addGameObjects() {
        initPlayer();
        addInteractionHints();
    }

    private void addInteractionHints() {
        doorInteractionHint = new InteractionHint("Tür" +" (" + KeyEvent.getKeyText(engine.getKeyMappingSettings().INTERACT) + ")");
        doorInteractionHint.setVisible(false);
        add(doorInteractionHint);

        workbenchInteractionHint = new InteractionHint("Werkbank" +" (" + KeyEvent.getKeyText(engine.getKeyMappingSettings().INTERACT) + ")");
        workbenchInteractionHint.setVisible(false);
        add(workbenchInteractionHint);

        shelfInteractionHint = new InteractionHint("Regal" +" (" + KeyEvent.getKeyText(engine.getKeyMappingSettings().INTERACT) + ")");
        shelfInteractionHint.setVisible(false);
        add(shelfInteractionHint);
    }

    private void initPlayer() {
        player = new PlayerCharacter(engine);
        player.setRelativeLocation(0.5f, 0.8f);
        player.setMoveable(true);
        player.setBlockingArea(LabSceneResource.PLAYER_SPACE);
        player.setPlayerSteppingSound(LabSceneResource.PLAYER_STEPPING);
        player.setPlayerScalingConstant(0.2f);
        add(player);
    }

    private int nitratorExplodedDelayTick;
    @Override
    public void tick(long currentTick) {
        doorTick(player.getRGBOnBlockArea() == Color.GREEN.getRGB());
        workbenchTick(player.getRGBOnBlockArea() == Color.RED.getRGB());
        shelfTick(player.getRGBOnBlockArea() == Color.BLUE.getRGB());
        missionFileTick();

        if (engine.getStateManager().getState() == GameState.CREATE_OXYGEN) {
            engine.getStateManager().setQuest(QuestState.GET_ELECTROLYSIS);
            engine.getStateManager().setState(GameState.GET_ELECTROLYSIS);
        }else if (engine.getStateManager().getState() == GameState.GOT_NITRIC_ACID) {
            EnoughMoneyForSulfuricAcidDialog dialog = new EnoughMoneyForSulfuricAcidDialog(engine, this);
            engine.getDialogManager().setDialog(dialog);
            engine.getStateManager().setState(GameState.GOT_NITRIC_ACID_DIALOG);
        }else if (engine.getStateManager().getState() == GameState.CREATED_GLYCERIN) {
            AllIngredientsDialog dialog = new AllIngredientsDialog(engine, this);
            engine.getDialogManager().setDialog(dialog);
            engine.getStateManager().setState(GameState.GET_NITRATOR);
        }else if (engine.getStateManager().getState() == GameState.CREATED_NITRO_GLYCERIN) {
            NitroglycerinInColaDialog dialog = new NitroglycerinInColaDialog(engine, this);
            engine.getDialogManager().setDialog(dialog);
            engine.getStateManager().setState(GameState.CREATED_NITRO_GLYCERIN_DIALOG);
        }

        if (engine.getStateManager().getState() == GameState.NITRATOR_EXPLODED) {
            if (nitratorExplodedDelayTick > 0) {
                nitratorExplodedDelayTick--;
            }else {
                engine.getTickProvider().unregisterTick(this);
                engine.getGraphics().switchToScene(new HospitalScene(engine), new BlackFade(engine));
            }
        }
    }

    private void missionFileTick() {
        if (engine.getStateManager().getState() == GameState.NITRATOR_EXPLODED) {
            return;
        }

        if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().OPEN_FILE)) {
            if (!missionTakedownFile.isShowing()) {
                InteractionResource.FILE_INTERACTION.create().play();
                missionTakedownFile.setVisible(true);
            }
        }else {
            if (missionTakedownFile.isShowing()) {
                missionTakedownFile.setVisible(false);
            }
        }
    }

    private void doorTick(boolean playerNearby) {
        boolean intHint = false;

        if (playerNearby) {
            if (engine.getStateManager().getState() == GameState.BUY_SULFURIC_ACID) {
                intHint = true;
                if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                    //GOTO SPAWN
                    engine.getGraphics().switchToScene(new SpawnScene(engine), new BlackFade(engine));
                }
            }else if (engine.getStateManager().getState() == GameState.DRINK_COLA) {
                intHint = true;
                if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                    //GOTO SPAWN
                    engine.getGraphics().switchToScene(new SpawnScene(engine), new BlackFade(engine));
                }
            }
        }

        doorInteractionHint.setVisible(intHint);
    }

    private void workbenchTick(boolean playerNearby) {
        boolean intHint = false;

        if (playerNearby) {

            if (engine.getStateManager().getState() == GameState.PLACE_ELECTROLYSIS) {
                intHint = true;
                if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                    LabSceneResource.PLACING_OBJECT_TABLE.create().play();
                    engine.getInventoryManager().findItemByType(Item.Type.Electrolysis).ifPresent(item -> {
                        engine.getInventoryManager().removeItem(item);
                    });
                    engine.getStateManager().setState(GameState.PLACED_ELECTROLYSIS);
                    engine.getGraphics().switchToScene(new WorkbenchScene(engine), new BlackFade(engine));
                }
            }else if (engine.getStateManager().getState() == GameState.PLACE_SALPTERGEN) {
                intHint = true;
                if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                    LabSceneResource.PLACING_OBJECT_TABLE.create().play();
                    engine.getInventoryManager().findItemByType(Item.Type.AcidGen).ifPresent(item -> {
                        engine.getInventoryManager().removeItem(item);
                    });
                    engine.getStateManager().setState(GameState.PLACED_SALPTERGEN);
                    engine.getGraphics().switchToScene(new WorkbenchScene(engine), new BlackFade(engine));
                }
            }else if (engine.getStateManager().getState() == GameState.PLACE_DISTILLATION_DEVICE) {
                intHint = true;
                if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                    LabSceneResource.PLACING_OBJECT_TABLE.create().play();
                    engine.getInventoryManager().findItemByType(Item.Type.Distillation).ifPresent(item -> {
                        engine.getInventoryManager().removeItem(item);
                    });
                    engine.getStateManager().setState(GameState.PLACED_DISTILLATION_DEVICE);
                    engine.getStateManager().setQuest(QuestState.NO_QUEST);
                    engine.getGraphics().switchToScene(new WorkbenchScene(engine), new BlackFade(engine));
                }
            }else if (engine.getStateManager().getState() == GameState.PLACE_NITRATOR) {
                intHint = true;
                if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                    LabSceneResource.PLACING_OBJECT_TABLE.create().play();
                    engine.getInventoryManager().findItemByType(Item.Type.Nitrator).ifPresent(item -> {
                        engine.getInventoryManager().removeItem(item);
                    });
                    engine.getStateManager().setState(GameState.PLACED_NITRATOR);
                    engine.getStateManager().setQuest(QuestState.NO_QUEST);
                    NitratorDialog dialog = new NitratorDialog(engine);
                    engine.getDialogManager().setDialog(dialog);
                    player.setMoveable(false);
                }
            }

        }

        workbenchInteractionHint.setVisible(intHint);
    }

    private void shelfTick(boolean playerNearby) {
        boolean intHint = false;

        if (playerNearby) {

            if (engine.getStateManager().getState() == GameState.GET_ELECTROLYSIS) {
                intHint = true;
                if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                    engine.getInventoryManager().addItem(new Item(Item.Type.Electrolysis));
                    LabSceneResource.PICKING_UP_OBJECT.create().play();
                    engine.getStateManager().setState(GameState.PLACE_ELECTROLYSIS);
                    engine.getStateManager().setQuest(QuestState.PLACE_ELECTROLYSIS);
                }
            }else if (engine.getStateManager().getState() == GameState.GET_SALPTERGEN) {
                intHint = true;
                if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                    engine.getInventoryManager().addItem(new Item(Item.Type.AcidGen));
                    LabSceneResource.PICKING_UP_OBJECT.create().play();
                    engine.getStateManager().setState(GameState.PLACE_SALPTERGEN);
                    engine.getStateManager().setQuest(QuestState.PLACE_SALPTERGEN);
                }
            }else if (engine.getStateManager().getState() == GameState.GET_DISTILLATION_DEVICE) {
                intHint = true;
                if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                    engine.getInventoryManager().addItem(new Item(Item.Type.Distillation));
                    LabSceneResource.PICKING_UP_OBJECT.create().play();
                    engine.getStateManager().setState(GameState.PLACE_DISTILLATION_DEVICE);
                    engine.getStateManager().setQuest(QuestState.PLACE_DESTILLATION_DEVICE);
                }
            }else if (engine.getStateManager().getState() == GameState.GET_NITRATOR) {
                intHint = true;
                if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                    engine.getInventoryManager().addItem(new Item(Item.Type.Nitrator));
                    LabSceneResource.PICKING_UP_OBJECT.create().play();
                    engine.getStateManager().setState(GameState.PLACE_NITRATOR);
                    engine.getStateManager().setQuest(QuestState.PLACE_NITRATOR);
                }
            }

        }

        shelfInteractionHint.setVisible(intHint);
    }

    @Override
    public void sceneShown() {
        super.sceneShown();
        engine.getTickProvider().registerTick(this);
        engine.getTickProvider().registerTick(player);
        repaint();

        if (engine.getStateManager().getState() == GameState.BOUGHT_FOG_FLUID) {
            engine.getStateManager().setQuest(QuestState.GET_DESTILLATION_DEVICE);
            engine.getStateManager().setState(GameState.GET_DISTILLATION_DEVICE);
        }

        if (engine.getStateManager().getState() == GameState.NITRATOR_EXPLODED) {
            remove(player);
            engine.getTickProvider().unregisterTick(player);
            remove(doorInteractionHint);
            remove(shelfInteractionHint);
            remove(workbenchInteractionHint);

            remove(moneyOverlay);
            remove(inventoryOverlay);
            remove(questOverlay);
            backgroundResource = LabSceneResource.BACKGROUND_EXPLODED;
            LabSceneResource.LAP_EXPLOSION.create().play();
            repaint();
        }
    }

    @Override
    public void sceneHidden() {
        super.sceneHidden();
        engine.getTickProvider().unregisterTick(this);
        engine.getTickProvider().unregisterTick(player);
        player.stopSteppingSound();
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundResource.getResource(), 0, 0, getWidth(), getHeight(), null);
    }

    public PlayerCharacter getPlayer() {
        return player;
    }
}
