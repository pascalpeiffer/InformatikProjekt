package de.missiontakedown.gameengine.dialog.lab;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.graphics.dialog.AbstractDialog;
import de.missiontakedown.gameengine.inventory.Item;
import de.missiontakedown.gameengine.scenes.lab.LabScene;
import de.missiontakedown.gameengine.state.GameState;
import de.missiontakedown.gameengine.state.QuestState;

/**
 * @author Jan
 */
public class EnoughMoneyForSulfuricAcidDialog extends AbstractDialog {

    private final GameEngine engine;

    private final LabScene labScene;

    public EnoughMoneyForSulfuricAcidDialog(GameEngine engine, LabScene labScene) {
        super(engine);
        this.engine = engine;
        this.labScene = labScene;
    }

    @Override
    public void onDialogShow() {
        labScene.getPlayer().setMoveable(false);
        if (!engine.getInventoryManager().findItemByType(Item.Type.SulfuricAcid).isPresent()) {
            continueDialog("Eagle:", "Ich hab ja jetzt auch genug Geld um die Schwefelsäure zu bestellen! Dann mache ich das direkt.", () -> {
                engine.getStateManager().setState(GameState.BUY_SULFURIC_ACID);
                engine.getStateManager().setQuest(QuestState.BUY_SULFURIC_ACID);
                labScene.getPlayer().setMoveable(true);
                dispose();
            });
        }else {
            //PLAYER ALREADY HAS SulfuricAcid
            continueDialog("Eagle:", "Puh. Geschafft. Endlich wieder zurück nach oben.", () -> {
                engine.getStateManager().setState(GameState.BUY_SULFURIC_ACID);
                engine.getStateManager().setQuest(QuestState.NO_QUEST);
                labScene.getPlayer().setMoveable(true);
                dispose();
            });
        }
    }
}
