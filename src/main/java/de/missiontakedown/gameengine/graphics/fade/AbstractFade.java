package de.missiontakedown.gameengine.graphics.fade;

import de.missiontakedown.gameengine.graphics.GameGraphics;
import de.missiontakedown.gameengine.scenes.AbstractScene;

import javax.swing.*;

/**
 * @author Pascal
 */
public abstract class AbstractFade extends JPanel {

    public abstract void fade(AbstractScene scene, GameGraphics graphics);

}
