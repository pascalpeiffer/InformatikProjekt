package de.missiontakedown.gameengine.dialog.spawn;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.graphics.dialog.AbstractDialog;
import de.missiontakedown.gameengine.graphics.fade.BlackFade;
import de.missiontakedown.gameengine.scenes.ending.EndingScene;
import de.missiontakedown.gameengine.scenes.spawn.SpawnScene;

/**
 * @author Jan
 */
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
                continueDialog("Mr. G:", "Sehr gut Eagle, ich wusste, dass auf dich Verlass ist. Deine Aktion und das daraus entstehende Chaos wird es uns ermöglichen die Macht in die richtigen Hände zu geben, damit sich so etwas nicht wiederholt.", () -> {
                    continueDialog("Mr. G:", " Danke Eagle, das ist allein dein Verdienst, dass die Menschen wieder frei sind. " +
                                    "Ich melde mich wenn es einen neuen Auftrag für dich gibt.", () -> {
                        continueDialog("Mr. G:", "Mr. G out.", () -> {
                            dispose();
                            scene.getPlayer().setMoveable(true);

                            engine.getGraphics().switchToScene(new EndingScene(engine), new BlackFade(engine));
                        });
                    });
                });
            });
        });
    }
}
