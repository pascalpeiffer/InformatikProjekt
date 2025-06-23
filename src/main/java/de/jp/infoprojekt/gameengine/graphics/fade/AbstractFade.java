package de.jp.infoprojekt.gameengine.graphics.fade;

import de.jp.infoprojekt.gameengine.graphics.GameGraphics;
import de.jp.infoprojekt.gameengine.scenes.AbstractScene;

import javax.swing.*;

/**
 * AbstractFade class
 *
 * @author Pascal
 * @version 19.06.2025
 */
public abstract class AbstractFade extends JPanel {

    public abstract void fade(AbstractScene scene, GameGraphics graphics);

}
