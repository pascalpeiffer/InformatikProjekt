package de.missiontakedown.gameengine.gameobjects.travel;

import de.missiontakedown.gameengine.gameobjects.AbstractGameObject;
import de.missiontakedown.resources.GameResource;
import de.missiontakedown.resources.scenes.TravelSceneResource;

import java.awt.*;

/**
 * @author Pascal
 */
public class TravelCar extends AbstractGameObject {

    private final GameResource dayCarResource = TravelSceneResource.CAR;
    private final GameResource nightCarResource = TravelSceneResource.CAR_NIGHT;

    private boolean flipped;

    private boolean night = false;

    public TravelCar() {
        setDisableBoundRelative(true);
        update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(night ? nightCarResource.getResource() : dayCarResource.getResource(), flipped ? getWidth() : 0, 0, flipped ? -getWidth() : getWidth(), getHeight(), null);
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        update();
        super.scale(widthMultiply, heightMultiply);
    }

    public void update() {
        setSize(night ? nightCarResource.getWidth() : dayCarResource.getWidth(), night ? nightCarResource.getHeight() : dayCarResource.getHeight());
        repaint();
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    public void setNight(boolean night) {
        this.night = night;
    }

    public boolean isNight() {
        return night;
    }
}
