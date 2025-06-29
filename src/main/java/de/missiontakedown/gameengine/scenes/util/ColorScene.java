package de.missiontakedown.gameengine.scenes.util;

import de.missiontakedown.gameengine.scenes.AbstractScene;

import java.awt.*;

/**
 * @author Pascal
 */
public class ColorScene extends AbstractScene {

    public ColorScene(Color color) {
        setBackground(color);
        setLayout(null);
    }

}
