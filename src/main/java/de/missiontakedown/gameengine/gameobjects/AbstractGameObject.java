package de.missiontakedown.gameengine.gameobjects;

import de.missiontakedown.resources.ResourceManager;
import de.missiontakedown.resources.ScalingEvent;
import de.missiontakedown.util.FloatPoint;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pascal
 */
public abstract class AbstractGameObject extends JComponent implements ScalingEvent {

    private float relativeX;
    private float relativeY;

    private boolean disableLocationFix = false;
    private boolean disableBoundRelative = false;

    private List<Runnable> clickRunnables = new ArrayList<>();

    public AbstractGameObject() {
        ResourceManager.addScalingListener(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clickRunnables.forEach(Runnable::run);
            }
        });
    }

    public void onClick(Runnable runnable) {
        clickRunnables.add(runnable);
    }

    public void setRelativeLocation(float relativeX, float relativeY) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        applyLocation();
    }

    public void setRelativeLocation(FloatPoint point) {
        setRelativeLocation(point.getX(), point.getY());
    }

    public float getRelativeX() {
        return relativeX;
    }

    public float getRelativeY() {
        return relativeY;
    }

    public FloatPoint getRelativeLocation() {
        return new FloatPoint(getRelativeX(), getRelativeY());
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
        if (disableBoundRelative) {
            return;
        }
        relativeX = Math.max(0, Math.min(1, relativeX));
        relativeY = Math.max(0, Math.min(1, relativeY));
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        applyLocation();
    }


    private void applyLocation() {
        boundRelative();
        int x = (int) (relativeX * ResourceManager.getGameScreenWidth());
        int y = (int) (relativeY * ResourceManager.getGameScreenHeight());

        if (!disableLocationFix) {
            x = x - getWidth() / 2;
            y = y - getHeight();
        }

        setLocation(x, y);
        repaint();
    }

    public void setDisableLocationFix(boolean disableLocationFix) {
        this.disableLocationFix = disableLocationFix;
    }

    public boolean isDisableLocationFix() {
        return disableLocationFix;
    }

    public boolean isDisableBoundRelative() {
        return disableBoundRelative;
    }

    public void setDisableBoundRelative(boolean disableBoundRelative) {
        this.disableBoundRelative = disableBoundRelative;
    }
}
