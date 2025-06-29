package de.missiontakedown.gameengine.scenes.lab;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.dialog.lab.WrongDistillationDialog;
import de.missiontakedown.gameengine.gameobjects.lab.AcidGenDevice;
import de.missiontakedown.gameengine.gameobjects.lab.DistillationDevice;
import de.missiontakedown.gameengine.gameobjects.lab.ElectrolysisDevice;
import de.missiontakedown.gameengine.gameobjects.lab.Nitrator;
import de.missiontakedown.gameengine.gameobjects.overlay.DistillationOverlay;
import de.missiontakedown.gameengine.gameobjects.overlay.NitratorOverlay;
import de.missiontakedown.gameengine.graphics.fade.BlackFade;
import de.missiontakedown.gameengine.inventory.Item;
import de.missiontakedown.gameengine.scenes.AbstractScene;
import de.missiontakedown.gameengine.state.GameState;
import de.missiontakedown.gameengine.state.QuestState;
import de.missiontakedown.gameengine.tick.GameTick;
import de.missiontakedown.resources.GameResource;
import de.missiontakedown.resources.ResourceManager;
import de.missiontakedown.resources.ScalingEvent;
import de.missiontakedown.resources.scenes.LabSceneResource;

import javax.swing.*;
import java.awt.*;

public class WorkbenchScene extends AbstractScene implements ScalingEvent, GameTick {

    private final GameEngine engine;

    private final GameResource backgroundResource = LabSceneResource.BACKGROUND_ZOOMED;

    private ElectrolysisDevice electrolysisDevice;
    private AcidGenDevice acidGenDevice;
    private DistillationDevice distillationDevice;
    private DistillationOverlay distillationOverlay;
    private Nitrator nitrator;
    private NitratorOverlay nitratorOverlay;

    public WorkbenchScene(GameEngine engine) {
        this.engine = engine;
        setLayout(null);

        initGameObjects();

        ResourceManager.addScalingListener(this);

        repaint();
    }

    private void initGameObjects() {
        initDistillationOverlay();
        initNitratorOverlay();
        initElectrolysisDevice();
        initAcidGenDevice();
        initDistillationDevice();
        initNitrator();
    }

    private void initNitratorOverlay() {
        nitratorOverlay = new NitratorOverlay(engine, this);
        nitratorOverlay.setVisible(false);
        add(nitratorOverlay);
    }

    private void initNitrator() {
        nitrator = new Nitrator(engine);
        nitrator.setRelativeLocation(0.5f, 0.7f);
        nitrator.setVisible(false);
        add(nitrator);
    }

    private void initDistillationOverlay() {
        distillationOverlay = new DistillationOverlay(engine);
        distillationOverlay.setVisible(false);
        add(distillationOverlay);
    }

    private void initDistillationDevice() {
        distillationDevice = new DistillationDevice();
        distillationDevice.setRelativeLocation(0.5f, 0.65f);
        distillationDevice.setVisible(false);
        add(distillationDevice);
    }

    private void initElectrolysisDevice() {
        electrolysisDevice = new ElectrolysisDevice(engine);
        electrolysisDevice.setRelativeLocation(0.5f, 0.65f);
        electrolysisDevice.setVisible(false);
        add(electrolysisDevice);
    }

    private void initAcidGenDevice() {
        acidGenDevice = new AcidGenDevice(engine);
        acidGenDevice.setRelativeLocation(0.5f, 0.65f);
        acidGenDevice.setVisible(false);
        add(acidGenDevice);
    }

    private void initState() {
        if (engine.getStateManager().getState() == GameState.PLACED_ELECTROLYSIS) {
            LabSceneResource.ELECTROLYSIS.create().onEnd(() -> {
                SwingUtilities.invokeLater(() -> {
                    LabSceneResource.DONE_DING.create().play();
                    engine.getInventoryManager().addItem(new Item(Item.Type.Hydrogen));
                    engine.getInventoryManager().addItem(new Item(Item.Type.Oxygen));
                    engine.getGraphics().switchToScene(new LabScene(engine), new BlackFade(engine));
                    engine.getStateManager().setQuest(QuestState.GET_SALPTERGEN);
                    engine.getStateManager().setState(GameState.GET_SALPTERGEN);
                });
            }).play();
            electrolysisDevice.setVisible(true);
        }else if (engine.getStateManager().getState() == GameState.PLACED_SALPTERGEN) {
            acidGenDevice.setMoving(false);
            engine.getInventoryManager().findItemByType(Item.Type.Amoniak).ifPresent(item -> {
                engine.getInventoryManager().removeItem(item);
            });
            engine.getInventoryManager().findItemByType(Item.Type.Oxygen).ifPresent(item -> {
                engine.getInventoryManager().removeItem(item);
            });
            LabSceneResource.NITRIC_ACID_GEN_FILL.create().onEnd(() -> {
                acidGenDevice.setMoving(true);
                LabSceneResource.NITRIC_ACID_RUNNING.create().onEnd(() -> {
                    acidGenDevice.setMoving(false);
                    SwingUtilities.invokeLater(() -> {
                        LabSceneResource.DONE_DING.create().play();
                        engine.getInventoryManager().addItem(new Item(Item.Type.NitricAcid));
                        engine.getGraphics().switchToScene(new LabScene(engine), new BlackFade(engine));
                        engine.getStateManager().setState(GameState.GOT_NITRIC_ACID);
                    });
                }).play();
            }).play();
            acidGenDevice.setVisible(true);
        }else if (engine.getStateManager().getState() == GameState.PLACED_DISTILLATION_DEVICE) {
            engine.getInventoryManager().findItemByType(Item.Type.FogFluid).ifPresent(item -> {
                engine.getInventoryManager().removeItem(item);
            });
            distillationDevice.setVisible(true);
            distillationOverlay.setVisible(true);
            distillationOverlay.setCallback(() -> {
                LabSceneResource.DISTILLATION.create().onEnd(() -> {
                    if (distillationOverlay.getTempIndex() == 1) {
                        //Correct Temp -> Go to Lab
                        engine.getGraphics().switchToScene(new LabScene(engine), new BlackFade(engine));
                        engine.getInventoryManager().addItem(new Item(Item.Type.Glycerin));
                        engine.getStateManager().setState(GameState.CREATED_GLYCERIN);
                    }else {
                        WrongDistillationDialog wrongDistillationDialog = new WrongDistillationDialog(engine, distillationOverlay);
                        engine.getDialogManager().setDialog(wrongDistillationDialog);
                    }
                }).play();
            });
        }else if (engine.getStateManager().getState() == GameState.PLACED_NITRATOR) {
            engine.getInventoryManager().findItemByType(Item.Type.Glycerin).ifPresent(item -> {
                engine.getInventoryManager().removeItem(item);
            });
            engine.getInventoryManager().findItemByType(Item.Type.NitricAcid).ifPresent(item -> {
                engine.getInventoryManager().removeItem(item);
            });
            engine.getInventoryManager().findItemByType(Item.Type.SulfuricAcid).ifPresent(item -> {
                engine.getInventoryManager().removeItem(item);
            });
            nitrator.setVisible(true);
            nitratorOverlay.setVisible(true);
            engine.getTickProvider().registerTick(nitratorOverlay);
            engine.getTickProvider().registerTick(nitrator);
        }
        repaint();
    }

    @Override
    public void tick(long currentTick) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundResource.getResource(), 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void sceneShown() {
        super.sceneShown();
        engine.getTickProvider().registerTick(this);
        initState();
        repaint();

        engine.getTickProvider().registerTick(acidGenDevice);
        engine.getTickProvider().registerTick(electrolysisDevice);
    }

    @Override
    public void sceneHidden() {
        super.sceneHidden();
        engine.getTickProvider().unregisterTick(this);

        engine.getTickProvider().unregisterTick(acidGenDevice);
        engine.getTickProvider().unregisterTick(electrolysisDevice);
        engine.getTickProvider().unregisterTick(nitratorOverlay);
        engine.getTickProvider().unregisterTick(nitrator);
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        repaint();
    }

    public Nitrator getNitrator() {
        return nitrator;
    }
}
