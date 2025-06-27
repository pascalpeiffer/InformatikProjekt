package de.jp.infoprojekt.gameengine.scenes.util;

import de.jp.infoprojekt.gameengine.scenes.AbstractScene;

import java.awt.*;

/**
 * BlackScene class
 *
 * @author Pascal
 * @version 19.06.2025
 */
public class ColorScene extends AbstractScene {

    public ColorScene(Color color) {
        setBackground(color);
        setLayout(null);
    }

}
