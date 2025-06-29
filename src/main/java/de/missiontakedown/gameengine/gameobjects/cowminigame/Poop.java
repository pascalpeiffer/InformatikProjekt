package de.missiontakedown.gameengine.gameobjects.cowminigame;

import de.missiontakedown.gameengine.gameobjects.AbstractGameObject;
import de.missiontakedown.resources.GameResource;
import de.missiontakedown.resources.scenes.CowMinigameSceneResource;

import java.awt.*;

/**
 * @author Pascal
 */
public class Poop extends AbstractGameObject {

    private final GameResource poopResource = CowMinigameSceneResource.POOP;

    private float scale = 1;

    public Poop() {
        update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(poopResource.getResource(), 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        update();
        super.scale(widthMultiply, heightMultiply);
    }

    public void update() {
        setSize((int) (poopResource.getWidth() * scale), (int) (poopResource.getHeight() * scale));
        repaint();
    }

    public void setScale(float scale) {
        this.scale = scale;
        update();
    }
}
