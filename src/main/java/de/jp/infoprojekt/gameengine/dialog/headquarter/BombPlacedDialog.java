package de.jp.infoprojekt.gameengine.dialog.headquarter;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.graphics.dialog.AbstractDialog;
import de.jp.infoprojekt.gameengine.scenes.headquarter.HeadquarterScene;
import de.jp.infoprojekt.gameengine.state.QuestState;

public class BombPlacedDialog extends AbstractDialog {

    private final GameEngine engine;
    private final HeadquarterScene scene;

    public BombPlacedDialog(GameEngine engine, HeadquarterScene scene) {
        super(engine);
        this.engine = engine;
        this.scene = scene;
    }

    @Override
    public void onDialogShow() {
        scene.getPlayer().setMoveable(false);
        continueDialog("Eagle:", "So jetzt schnell weg hier und dann Zündung. Am besten ich mache das von Zuhause, dann bin ich außerhalb der Reichweite der Explosion.", () -> {
            scene.getExitOverlay().setVisible(true);
            scene.getPlayer().setMoveable(true);
            engine.getStateManager().setQuest(QuestState.DETONATE_BOMB);
            dispose();
        });
    }

}
