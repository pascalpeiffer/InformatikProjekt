package de.missiontakedown.gameengine.dialog.spawn;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.graphics.dialog.AbstractDialog;
import de.missiontakedown.gameengine.state.QuestState;

public class TooLessMoneyForSulfuricDialog extends AbstractDialog {

    private final GameEngine engine;

    public TooLessMoneyForSulfuricDialog(GameEngine engine) {
        super(engine);
        this.engine = engine;
    }

    @Override
    public void onDialogShow() {
        continueDialog("Eagle:", "Verdammt, ich habe zu wenig Geld. Ich denke dann stelle ich erst mal Salpetersäure her.", () -> {
            engine.getStateManager().setQuest(QuestState.CREATE_NITRIC_ACID);
            dispose();
        });
    }
}
