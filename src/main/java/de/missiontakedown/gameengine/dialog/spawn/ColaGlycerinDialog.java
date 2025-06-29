package de.missiontakedown.gameengine.dialog.spawn;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.graphics.dialog.AbstractDialog;
import de.missiontakedown.gameengine.scenes.spawn.SpawnScene;
import de.missiontakedown.gameengine.state.GameState;
import de.missiontakedown.gameengine.state.QuestState;

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
