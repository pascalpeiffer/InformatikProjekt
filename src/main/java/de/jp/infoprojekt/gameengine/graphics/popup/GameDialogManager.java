package de.jp.infoprojekt.gameengine.graphics.popup;

import de.jp.infoprojekt.gameengine.GameEngine;

public class GameDialogManager {

    private final GameEngine engine;

    private AbstractDialog currentDialog;

    public GameDialogManager(GameEngine engine) {
        this.engine = engine;
    }

    public void setDialog(AbstractDialog dialog) {
        unsetDialog();
        this.currentDialog = dialog;
        engine.getGraphics().addDialogLayer(currentDialog);
    }

    public void unsetDialog() {
        if (currentDialog != null) {
            engine.getGraphics().removeLayer(currentDialog);
            currentDialog = null;
        }
    }

    public AbstractDialog getCurrentDialog() {
        return currentDialog;
    }
}
