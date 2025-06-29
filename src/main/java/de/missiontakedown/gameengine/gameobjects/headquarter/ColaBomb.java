package de.missiontakedown.gameengine.gameobjects.headquarter;

import de.missiontakedown.gameengine.gameobjects.AbstractGameObject;
import de.missiontakedown.resources.GameResource;
import de.missiontakedown.resources.item.ItemResource;

import java.awt.*;

/**
 * @author Pascal
 */
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
