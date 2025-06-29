package de.missiontakedown.gameengine.dialog.computer;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.graphics.dialog.AbstractDialog;

/**
 * @author Jan
 */
public class ColaInFridgeDialog extends AbstractDialog {

    public ColaInFridgeDialog(GameEngine engine) {
        super(engine);
    }

    @Override
    public void onDialogShow() {
        continueDialog("Eagle:", "Hmm. Ich glaube ich habe noch genug Cola im Kühlschrank.", this::dispose);
    }
}
