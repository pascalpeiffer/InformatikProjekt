package de.jp.infoprojekt.gameengine.dialog.spawn;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.graphics.dialog.AbstractDialog;
import de.jp.infoprojekt.gameengine.scenes.spawn.SpawnScene;

public class FinalDialog extends AbstractDialog {

    private final GameEngine engine;
    private final SpawnScene scene;

    public FinalDialog(GameEngine engine, SpawnScene scene) {
        super(engine);
        this.engine = engine;
        this.scene = scene;
    }

    @Override
    public void onDialogShow() {
        continueDialog("Mr. G:", "Mr. G hier.", () -> {
            continueDialog("Eagle:", "Mr. G es ist erledigt, die Führungsstelle ist gesprengt, Mission erfüllt.", () -> {
                continueDialog("Mr. G:", "Sehr gut Eagle, ich wusste dass auf dich verlass ist. Deine Aktion und das daraus entstehende Chaos wird es uns ermöglichen die Macht in die richtigen Hände zu geben, damit sich so etwas nicht wiederholt.", () -> {
                    continueDialog("Mr. G:", " Danke Eagle, das ist allein dein Verdienst, dass die Menschen wieder frei sind. " +
                                    "Ich melde mich wenn es einen neuen Auftrag für dich gibt.", () -> {
                        continueDialog("Mr. G:", "Mr. G out.", () -> {
                            dispose();
                            scene.getPlayer().setMoveable(true);
                            //TODO end scene
                        });
                    });
                });
            });
        });
    }
}
