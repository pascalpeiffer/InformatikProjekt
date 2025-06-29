package de.jp.infoprojekt.gameengine.dialog.computer;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.graphics.dialog.AbstractDialog;

public class ColaInFridgeDialog extends AbstractDialog {

    public ColaInFridgeDialog(GameEngine engine) {
        super(engine);
    }

    @Override
    public void onDialogShow() {
        continueDialog("Eagle:", "Hmm. Ich glaube ich habe noch genug Cola im Kühlschrank.", this::dispose);
    }
}
