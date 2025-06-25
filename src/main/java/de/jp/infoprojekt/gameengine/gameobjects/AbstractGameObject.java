package de.jp.infoprojekt.gameengine.gameobjects;

import de.jp.infoprojekt.resources.ResourceManager;
import de.jp.infoprojekt.resources.ScalingEvent;

import javax.swing.*;

/**
 * AbstractGameObject class
 *
 * @author Pascal
 * @version 12.06.2025
 */
public abstract class AbstractGameObject extends JComponent implements ScalingEvent {

    private float relativeX;
    private float relativeY;

    public AbstractGameObject() {
        ResourceManager.addScalingListener(this);
    }

    public void setRelativeLocation(float relativeX, float relativeY) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        applyLocation();
    }

    public float getRelativeX() {
        return relativeX;
    }

    public float getRelativeY() {
        return relativeY;
    }

    public void setRelativeX(float relativeX) {
        this.relativeX = relativeX;
        applyLocation();
    }

    public void setRelativeY(float relativeY) {
        this.relativeY = relativeY;
        applyLocation();
    }

    private void boundRelative() {
        relativeX = Math.max(0, Math.min(1, relativeX));
        relativeY = Math.max(0, Math.min(1, relativeY));
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        applyLocation();
        repaint();
    }


    private void applyLocation() {
        boundRelative();
        int x = (int) (relativeX * ResourceManager.getGameScreenWidth());
        int y = (int) (relativeY * ResourceManager.getGameScreenHeight());
        x = x - getWidth() / 2;
        y = y - getHeight();
        setLocation(x, y);
    }
}
