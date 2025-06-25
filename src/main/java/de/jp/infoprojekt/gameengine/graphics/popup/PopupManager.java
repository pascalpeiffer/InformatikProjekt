package de.jp.infoprojekt.gameengine.graphics.popup;

import de.jp.infoprojekt.gameengine.GameEngine;

import javax.swing.*;

/**
 * PopupManager class
 *
 * @author Pascal
 * @version 24.06.2025
 */
public class PopupManager {

    private final GameEngine engine;

    private AbstractPopup currentPopup;

    public PopupManager(GameEngine engine) {
        this.engine = engine;
    }

    public void openPopup(AbstractPopup popup) {
        closePopup();
        currentPopup = popup;
        engine.getGraphics().getFrame().getLayeredPane().add(popup.getComponent(), 1);
        getCurrentPopup().showPopup();
    }

    public void closePopup() {
        if (getCurrentPopup() != null) {
            getCurrentPopup().hidePopup();
            currentPopup = null;
        }
    }

    public AbstractPopup getCurrentPopup() {
        return currentPopup;
    }
}
