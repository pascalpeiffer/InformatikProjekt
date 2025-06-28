package de.jp.infoprojekt.gameengine.gameobjects.travel;

import de.jp.infoprojekt.gameengine.gameobjects.AbstractGameObject;
import de.jp.infoprojekt.resources.GameResource;
import de.jp.infoprojekt.resources.scenes.TravelSceneResource;

import java.awt.*;

public class TravelCar extends AbstractGameObject {

    private final GameResource carResource = TravelSceneResource.CAR;

    private boolean flipped;

    public TravelCar() {
        setDisableLocationFix(true);
        setDisableBoundRelative(true);
        update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(carResource.getResource(), flipped ? getWidth() : 0, 0, flipped ? -getWidth() : getWidth(), getHeight(), null);
        g.setColor(Color.RED);
        g.drawRect(0, 0, getWidth() -1,getHeight()-1);
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        update();
        super.scale(widthMultiply, heightMultiply);
    }

    private void update() {
        setSize(carResource.getWidth(), carResource.getHeight());
        repaint();
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }
}
