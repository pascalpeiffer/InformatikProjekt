package de.missiontakedown.gameengine.dialog.lab;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.graphics.dialog.AbstractDialog;
import de.missiontakedown.gameengine.graphics.fade.BlackFade;
import de.missiontakedown.gameengine.scenes.lab.WorkbenchScene;

public class NitratorDialog extends AbstractDialog {

    private final GameEngine engine;

    public NitratorDialog(GameEngine engine) {
        super(engine);
        this.engine = engine;
    }

    @Override
    public void onDialogShow() {
        continueDialog("Eagle:", "§o§lSchaut auf das Rezept...§r Die Reaktion im Nitrator ist Exothermisch.", () -> {
            continueDialog("Eagle:", "Sie erfordert somit ständige Kühlung und langsame Zugabe der Inhalte.", () -> {
                continueDialog("Eagle:", "Wenn die Mischung die Temperatur von 30° übersteigt, explodiert sie.", () -> {
                    continueDialog("Eagle:", "Naja gut, dann wollen wir mal.", () -> {
                        engine.getGraphics().switchToScene(new WorkbenchScene(engine), new BlackFade(engine));
                        dispose();
                    });
                });
            });
        });
    }
}
