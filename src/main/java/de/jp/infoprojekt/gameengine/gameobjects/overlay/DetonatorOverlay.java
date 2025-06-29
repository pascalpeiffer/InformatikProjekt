package de.jp.infoprojekt.gameengine.gameobjects.overlay;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.dialog.spawn.CallBossDialog;
import de.jp.infoprojekt.gameengine.gameobjects.AbstractGameObject;
import de.jp.infoprojekt.gameengine.scenes.headquarter.HeadquarterScene;
import de.jp.infoprojekt.gameengine.scenes.spawn.SpawnScene;
import de.jp.infoprojekt.gameengine.state.GameState;
import de.jp.infoprojekt.gameengine.state.QuestState;
import de.jp.infoprojekt.resources.GameResource;
import de.jp.infoprojekt.resources.ResourceManager;
import de.jp.infoprojekt.resources.gameobjects.PlayerResource;
import de.jp.infoprojekt.resources.scenes.HeadquarterSceneResource;
import de.jp.infoprojekt.resources.scenes.SpawnSceneResource;
import de.jp.infoprojekt.util.FloatPoint;
import de.jp.infoprojekt.util.FloatRectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
