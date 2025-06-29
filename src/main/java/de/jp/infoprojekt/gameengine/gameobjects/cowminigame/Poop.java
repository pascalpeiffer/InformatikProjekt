package de.jp.infoprojekt.gameengine.gameobjects.cowminigame;

import de.jp.infoprojekt.gameengine.gameobjects.AbstractGameObject;
import de.jp.infoprojekt.resources.GameResource;
import de.jp.infoprojekt.resources.scenes.CowMinigameSceneResource;

import java.awt.*;

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
