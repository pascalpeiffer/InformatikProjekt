package de.jp.infoprojekt.gameengine.dialog.spawn;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.graphics.dialog.AbstractDialog;
import de.jp.infoprojekt.gameengine.scenes.spawn.SpawnScene;
import de.jp.infoprojekt.gameengine.state.GameState;
import de.jp.infoprojekt.gameengine.state.QuestState;

public class ColaGlycerinDialog extends AbstractDialog {

    private final GameEngine engine;
    private final SpawnScene spawnScene;

    public ColaGlycerinDialog(GameEngine engine, SpawnScene spawnScene) {
        super(engine);
        this.spawnScene = spawnScene;
        this.engine = engine;
    }

    @Override
    public void onDialogShow() {
        spawnScene.getPlayer().setMoveable(false);

        continueDialog("Eagle:", "Jetzt fehlt mir nur noch das Glycerin... Aber wo kriege ich das her?", () -> {
            optionsDialog("Aber wo kriege ich das her?", "Extrem intensiv nachdenken und zu nichts kommen", "Erstmal eine Cola gönnen und effektiv nachdenken", optionA -> {
                if (optionA) {
                    continueDialog("Eagle:", "Ich brauche eine Cola!", this::needToDrinkCola);
                } else {
                    needToDrinkCola();
                }
            });
        });
    }

    private void needToDrinkCola() {
        engine.getStateManager().setState(GameState.NEED_COLA);
        engine.getStateManager().setQuest(QuestState.NEED_COLA);
        spawnScene.getPlayer().setMoveable(true);
        dispose();
    }

}
