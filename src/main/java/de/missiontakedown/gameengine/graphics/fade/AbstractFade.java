package de.missiontakedown.gameengine.graphics.fade;

import de.missiontakedown.gameengine.graphics.GameGraphics;
import de.missiontakedown.gameengine.scenes.AbstractScene;

import javax.swing.*;

public abstract class AbstractFade extends JPanel {

    public abstract void fade(AbstractScene scene, GameGraphics graphics);

}
