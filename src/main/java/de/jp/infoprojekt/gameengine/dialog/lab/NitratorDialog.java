package de.jp.infoprojekt.gameengine.dialog.lab;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.graphics.dialog.AbstractDialog;
import de.jp.infoprojekt.gameengine.graphics.fade.BlackFade;
import de.jp.infoprojekt.gameengine.scenes.lab.WorkbenchScene;

public class NitratorDialog extends AbstractDialog {

    private final GameEngine engine;

    public NitratorDialog(GameEngine engine) {
        super(engine);
        this.engine = engine;
    }

    @Override
    public void onDialogShow() {
        continueDialog("Eagle:", "§o§lSchaut auf das Rezept...§r Die Reaktion im Nitrator ist Exothermisch.", () -> {
            continueDialog("Eagle:", "Sie erfordert somit ständige Kühlung und langsame zugabe der Inhalte.", () -> {
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
