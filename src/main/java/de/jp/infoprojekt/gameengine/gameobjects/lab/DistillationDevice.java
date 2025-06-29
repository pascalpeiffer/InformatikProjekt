package de.jp.infoprojekt.gameengine.gameobjects.lab;

import de.jp.infoprojekt.gameengine.gameobjects.AbstractGameObject;
import de.jp.infoprojekt.resources.GameResource;
import de.jp.infoprojekt.resources.item.ItemResource;

import java.awt.*;

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
