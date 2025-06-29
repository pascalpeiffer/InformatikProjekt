package de.missiontakedown.gameengine.gameobjects.lab;

import de.missiontakedown.gameengine.gameobjects.AbstractGameObject;
import de.missiontakedown.resources.GameResource;
import de.missiontakedown.resources.item.ItemResource;

import java.awt.*;

/**
 * @author Pascal
 */
public class DistillationDevice extends AbstractGameObject {

    private final GameResource distillation = ItemResource.Distillation;

    public DistillationDevice() {
        update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(distillation.getResource(), 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        update();
        super.scale(widthMultiply, heightMultiply);
    }

    public void update() {
        setSize(distillation.getWidth(), distillation.getHeight());
        repaint();
    }

}
