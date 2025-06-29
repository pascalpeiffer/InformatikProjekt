package de.jp.infoprojekt.gameengine.gameobjects.headquarter;

import de.jp.infoprojekt.gameengine.gameobjects.AbstractGameObject;
import de.jp.infoprojekt.resources.GameResource;
import de.jp.infoprojekt.resources.item.ItemResource;

import java.awt.*;

public class ColaBomb extends AbstractGameObject {

    private final GameResource colaBomb = ItemResource.ColaBomb_NIGHT;

    public ColaBomb() {
        update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(colaBomb.getResource(), 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        update();
        super.scale(widthMultiply, heightMultiply);
    }

    public void update() {
        setSize(colaBomb.getWidth(), colaBomb.getHeight());
        repaint();
    }

}
