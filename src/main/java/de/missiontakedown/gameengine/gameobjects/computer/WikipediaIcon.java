package de.missiontakedown.gameengine.gameobjects.computer;

import de.missiontakedown.gameengine.gameobjects.AbstractGameObject;
import de.missiontakedown.resources.GameResource;
import de.missiontakedown.resources.scenes.ComputerSceneResource;

import java.awt.*;

/**
 * @author Pascal
 */
public class WikipediaIcon extends AbstractGameObject {

    private final GameResource background = ComputerSceneResource.WIKIPEDIA;

    public WikipediaIcon() {
        update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background.getResource(), 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        update();
        super.scale(widthMultiply, heightMultiply);
    }

    private void update() {
        setSize(background.getWidth(), background.getHeight());
        repaint();
    }
}
