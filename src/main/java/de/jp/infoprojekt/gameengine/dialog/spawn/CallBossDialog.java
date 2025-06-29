package de.jp.infoprojekt.gameengine.dialog.spawn;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.graphics.dialog.AbstractDialog;
import de.jp.infoprojekt.gameengine.scenes.spawn.SpawnScene;
import de.jp.infoprojekt.gameengine.state.GameState;
import de.jp.infoprojekt.gameengine.state.QuestState;

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
