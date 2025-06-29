package de.missiontakedown.gameengine.dialog.spawn;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.graphics.dialog.AbstractDialog;
import de.missiontakedown.gameengine.scenes.spawn.SpawnScene;
import de.missiontakedown.gameengine.state.GameState;
import de.missiontakedown.gameengine.state.QuestState;

public class CallBossDialog extends AbstractDialog {

    private final GameEngine engine;
    private final SpawnScene scene;

    public CallBossDialog(GameEngine engine, SpawnScene scene) {
        super(engine);
        this.engine = engine;
        this.scene = scene;
    }

    @Override
    public void onDialogShow() {
        scene.getPlayer().setMoveable(false);
        continueDialog("Eagle:", "So, jetzt schnell den Boss anrufen und den Auftrag bestätigen.", () -> {
            engine.getStateManager().setState(GameState.CALL_G);
            engine.getStateManager().setQuest(QuestState.CALL_G);
            scene.getPlayer().setMoveable(true);
            dispose();
        });
    }
}
