package de.jp.infoprojekt.gameengine.dialog.spawn;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.graphics.dialog.AbstractDialog;
import de.jp.infoprojekt.gameengine.state.GameState;
import de.jp.infoprojekt.gameengine.state.QuestState;

public class ChooseRecipeDialog extends AbstractDialog {

    private final GameEngine engine;

    public ChooseRecipeDialog(GameEngine engine) {
        super(engine);
        this.engine = engine;
    }

    @Override
    public void onDialogShow() {
        continueDialog("Eagle:","Hmm, ich habe jetzt das Rezept und weiß, was ich brauche. Welche Zutat besorge ich zuerst?", () -> {
            optionsDialog("Welche Zutat besorge ich zu erst?", "Salpetersäure", "Schwefelsäure", optionA -> {
                if (optionA) {
                    engine.getStateManager().setState(GameState.FIRST_NITRIC_ACID);
                    engine.getStateManager().setQuest(QuestState.CREATE_NITRIC_ACID);
                    dispose();
                }else {
                    engine.getStateManager().setState(GameState.FIRST_SULFURIC_ACID);
                    engine.getStateManager().setQuest(QuestState.BUY_SULFURIC_ACID);
                    dispose();
                }
            });
        });
    }
}
