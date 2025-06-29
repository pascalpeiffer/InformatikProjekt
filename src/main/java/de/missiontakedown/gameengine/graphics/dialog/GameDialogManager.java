package de.missiontakedown.gameengine.graphics.dialog;

import de.missiontakedown.gameengine.GameEngine;

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
        engine.getTickProvider().registerTick(currentDialog);
        currentDialog.onDialogShow();
    }

    public void unsetDialog() {
        if (currentDialog != null) {
            currentDialog.onDialogHide();
            engine.getTickProvider().unregisterTick(currentDialog);
            engine.getGraphics().removeLayer(currentDialog);
            currentDialog = null;
        }
    }

    public AbstractDialog getCurrentDialog() {
        return currentDialog;
    }

    public boolean hasDialog() {
        return currentDialog != null;
    }
}
