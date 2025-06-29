package de.jp.infoprojekt.gameengine.scenes.lab;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.dialog.lab.AllIngredientsDialog;
import de.jp.infoprojekt.gameengine.dialog.lab.EnoughMoneyForSulfuricAcidDialog;
import de.jp.infoprojekt.gameengine.dialog.lab.NitratorDialog;
import de.jp.infoprojekt.gameengine.gameobjects.interaction.InteractionHint;
import de.jp.infoprojekt.gameengine.gameobjects.overlay.InventoryOverlay;
import de.jp.infoprojekt.gameengine.gameobjects.overlay.MoneyOverlay;
import de.jp.infoprojekt.gameengine.gameobjects.overlay.QuestOverlay;
import de.jp.infoprojekt.gameengine.gameobjects.player.PlayerCharacter;
import de.jp.infoprojekt.gameengine.graphics.fade.BlackFade;
import de.jp.infoprojekt.gameengine.inventory.Item;
import de.jp.infoprojekt.gameengine.scenes.AbstractScene;
import de.jp.infoprojekt.gameengine.scenes.spawn.SpawnScene;
import de.jp.infoprojekt.gameengine.state.GameState;
import de.jp.infoprojekt.gameengine.state.QuestState;
import de.jp.infoprojekt.gameengine.tick.GameTick;
import de.jp.infoprojekt.resources.GameResource;
import de.jp.infoprojekt.resources.ResourceManager;
import de.jp.infoprojekt.resources.ScalingEvent;
import de.jp.infoprojekt.resources.scenes.LabSceneResource;

import java.awt.*;
import java.awt.event.KeyEvent;

public class LabScene extends AbstractScene implements ScalingEvent, GameTick {

    private final GameEngine engine;

    private final GameResource backgroundResource = LabSceneResource.BACKGROUND;

    private PlayerCharacter player;

    private InteractionHint doorInteractionHint;
    private InteractionHint workbenchInteractionHint;
    private InteractionHint shelfInteractionHint;

    public LabScene(GameEngine engine) {
        this.engine = engine;
        setLayout(null);

        add(new MoneyOverlay(engine));
        add(new QuestOverlay(engine));
        add(new InventoryOverlay(engine));

        addGameObjects();

        ResourceManager.addScalingListener(this);

        repaint();
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

    @Override
    public void tick(long currentTick) {
        doorTick(player.getRGBOnBlockArea() == Color.GREEN.getRGB());
        workbenchTick(player.getRGBOnBlockArea() == Color.RED.getRGB());
        shelfTick(player.getRGBOnBlockArea() == Color.BLUE.getRGB());

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
                    engine.getInventoryManager().findItemByType(Item.Type.Destillation).ifPresent(item -> {
                        engine.getInventoryManager().removeItem(item);
                    });
                    engine.getStateManager().setState(GameState.PLACED_DISTILLATION_DEVICE);
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
                    engine.getInventoryManager().addItem(new Item(Item.Type.Destillation));
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
