package de.missiontakedown.gameengine.gameobjects.computer;

import de.missiontakedown.gameengine.gameobjects.AbstractGameObject;
import de.missiontakedown.resources.GameResource;
import de.missiontakedown.resources.ScalingEvent;
import de.missiontakedown.resources.scenes.ComputerSceneResource;

import java.awt.*;

public class AmazonIcon extends AbstractGameObject implements ScalingEvent {

    private final GameResource background = ComputerSceneResource.AMAZON;

    public AmazonIcon() {
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
