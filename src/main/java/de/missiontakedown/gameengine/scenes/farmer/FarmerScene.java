package de.missiontakedown.gameengine.scenes.farmer;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.dialog.farmer.FirstFarmerDialog;
import de.missiontakedown.gameengine.dialog.farmer.SecondFarmerDialog;
import de.missiontakedown.gameengine.gameobjects.overlay.InventoryOverlay;
import de.missiontakedown.gameengine.gameobjects.overlay.MoneyOverlay;
import de.missiontakedown.gameengine.gameobjects.overlay.QuestOverlay;
import de.missiontakedown.gameengine.scenes.AbstractScene;
import de.missiontakedown.gameengine.state.GameState;
import de.missiontakedown.gameengine.state.QuestState;
import de.missiontakedown.resources.GameResource;
import de.missiontakedown.resources.scenes.FarmerSceneResource;

import java.awt.*;

public class FarmerScene extends AbstractScene {

    private final GameEngine engine;

    private GameResource backgroundResource = FarmerSceneResource.BACKGROUND;

    public FarmerScene(GameEngine engine) {
        this.engine = engine;
        setLayout(null);

        add(new MoneyOverlay(engine));
        add(new QuestOverlay(engine));
        add(new InventoryOverlay(engine));
    }

    @Override
    public void sceneShown() {
        super.sceneShown();
        if (engine.getStateManager().getState() == GameState.GO_TO_FARMER) {
            FirstFarmerDialog dialog = new FirstFarmerDialog(engine);
            engine.getDialogManager().setDialog(dialog);
            engine.getStateManager().setQuest(QuestState.NO_QUEST);
        }else if (engine.getStateManager().getState() == GameState.EARNING_MONEY) {
            SecondFarmerDialog dialog = new SecondFarmerDialog(engine);
            engine.getDialogManager().setDialog(dialog);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundResource.getResource(), 0, 0, getWidth(), getHeight(), null);
    }
}
