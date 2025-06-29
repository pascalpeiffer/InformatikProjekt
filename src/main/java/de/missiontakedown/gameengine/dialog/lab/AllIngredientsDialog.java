package de.missiontakedown.gameengine.dialog.lab;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.graphics.dialog.AbstractDialog;
import de.missiontakedown.gameengine.scenes.lab.LabScene;
import de.missiontakedown.gameengine.state.QuestState;

/**
 * @author Jan
 */
public class AllIngredientsDialog extends AbstractDialog {

    private final GameEngine engine;
    private final LabScene labScene;

    public AllIngredientsDialog(GameEngine engine, LabScene labScene) {
        super(engine);
        this.engine = engine;
        this.labScene = labScene;
    }

    @Override
    public void onDialogShow() {
        labScene.getPlayer().setMoveable(false);
        continueDialog("Eagle:", "Jetzt habe ich alle Zutaten um das Nitroglycerin herzustellen. Ich fange am besten gleich an.", () -> {
            labScene.getPlayer().setMoveable(true);
            engine.getStateManager().setQuest(QuestState.GET_NITRATOR);
            dispose();
        });
    }

}
