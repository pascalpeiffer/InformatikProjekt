package de.jp.infoprojekt.gameengine.dialog.spawn;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.graphics.dialog.AbstractDialog;
import de.jp.infoprojekt.gameengine.scenes.spawn.SpawnScene;
import de.jp.infoprojekt.gameengine.state.GameState;
import de.jp.infoprojekt.gameengine.state.QuestState;

public class RememberFogFluidDialog extends AbstractDialog {

    private final GameEngine engine;

    private final SpawnScene spawnScene;

    public RememberFogFluidDialog(GameEngine engine, SpawnScene spawnScene) {
        super(engine);
        this.engine = engine;
        this.spawnScene = spawnScene;
    }

    @Override
    public void onDialogShow() {
        spawnScene.getPlayer().setMoveable(false);

        continueDialog("Eagle:", "Ahh, jetzt hab ich es! Auf Amazon habe ich doch Nebelfluid gesehen. Das enthält ja Glycerin und mit Destillation kann ich es extrahieren.", () -> {
            engine.getStateManager().setState(GameState.BUY_FOG_FLUID);
            engine.getStateManager().setQuest(QuestState.BUY_FOG_FLUID);
            spawnScene.getPlayer().setMoveable(true);
            dispose();
        });
    }

}
