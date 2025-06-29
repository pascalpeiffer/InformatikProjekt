package de.missiontakedown.gameengine.gameobjects.overlay;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.dialog.spawn.CallBossDialog;
import de.missiontakedown.gameengine.gameobjects.AbstractGameObject;
import de.missiontakedown.gameengine.graphics.fade.BlackFade;
import de.missiontakedown.gameengine.scenes.headquarter.HeadquarterBombExplodeScene;
import de.missiontakedown.gameengine.scenes.headquarter.HeadquarterScene;
import de.missiontakedown.gameengine.scenes.hospital.HospitalScene;
import de.missiontakedown.gameengine.scenes.spawn.SpawnScene;
import de.missiontakedown.gameengine.state.GameState;
import de.missiontakedown.gameengine.state.QuestState;
import de.missiontakedown.resources.GameResource;
import de.missiontakedown.resources.ResourceManager;
import de.missiontakedown.resources.gameobjects.PlayerResource;
import de.missiontakedown.resources.scenes.HeadquarterSceneResource;
import de.missiontakedown.resources.scenes.SpawnSceneResource;
import de.missiontakedown.util.FloatPoint;
import de.missiontakedown.util.FloatRectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Pascal
 */
public class DetonatorOverlay extends AbstractGameObject {

    private final GameEngine engine;

    private final GameResource detonator = PlayerResource.DETONATOR;

    private final FloatRectangle detonatorHitBox = new FloatRectangle(0.028f, 0.149f, 0.069f, 0.132f);

    public DetonatorOverlay(GameEngine engine) {
        this.engine = engine;
        setDisableLocationFix(true);
        setRelativeLocation(new FloatPoint(0.01f, 0.6f));
        updateSize();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                float relativeX = (float) e.getX() / ResourceManager.getGameScreenWidth();
                float relativeY = (float) e.getY() / ResourceManager.getGameScreenHeight();

                FloatPoint mouseHit = new FloatPoint(relativeX, relativeY);

                onMouseClick(mouseHit);
            }
        });
    }

    private void onMouseClick(FloatPoint mouseHit) {
        if (detonatorHitBox.contains(mouseHit)) {
            setVisible(false);
            engine.getStateManager().setQuest(QuestState.NO_QUEST);
            if (engine.getGraphics().getCurrentScene() instanceof SpawnScene) {
                SpawnScene scene = (SpawnScene) engine.getGraphics().getCurrentScene();
                SpawnSceneResource.BOMB_EXPLODE.create().onEnd(() -> {
                    SwingUtilities.invokeLater(() -> {
                        CallBossDialog dialog = new CallBossDialog(engine, scene);
                        engine.getDialogManager().setDialog(dialog);
                    });
                }).play();
            }else if (engine.getGraphics().getCurrentScene() instanceof HeadquarterScene) {
                HeadquarterScene scene = (HeadquarterScene) engine.getGraphics().getCurrentScene();
                scene.setBackgroundResource(HeadquarterSceneResource.BACKGROUND_EXPLOSION);
                scene.repaint();
                scene.getInventoryOverlay().setVisible(false);
                scene.getQuestOverlay().setVisible(false);
                scene.getMoneyOverlay().setVisible(false);
                scene.getPlayer().setVisible(false);
                scene.getExitOverlay().setVisible(false);
                scene.getColaBomb().setVisible(false);
                scene.getAmbientSound().stop();
                engine.getDialogManager().unsetDialog();
                engine.getStateManager().setState(GameState.PLAYER_EXPLODED);
                engine.getStateManager().setQuest(QuestState.NO_QUEST);
                HeadquarterSceneResource.EXPLOSION.create().play();
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    SwingUtilities.invokeLater(() -> {
                        engine.getGraphics().switchToScene(new HeadquarterBombExplodeScene(engine), new BlackFade(engine));
                    });
                }).start();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(detonator.getResource(),0,0, detonator.getWidth(), detonator.getHeight(), null);

    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        updateSize();
        super.scale(widthMultiply, heightMultiply);
    }

    private void updateSize() {
        setSize(detonator.getWidth(), detonator.getHeight());
        repaint();
    }

}
