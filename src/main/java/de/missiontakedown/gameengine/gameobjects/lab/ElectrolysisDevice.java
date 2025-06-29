package de.missiontakedown.gameengine.gameobjects.lab;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.gameobjects.AbstractGameObject;
import de.missiontakedown.gameengine.tick.GameTick;
import de.missiontakedown.resources.GameResource;
import de.missiontakedown.resources.item.ItemResource;

import java.awt.*;

/**
 * @author Pascal
 */
public class ElectrolysisDevice extends AbstractGameObject implements GameTick {

    private final GameResource electrolysis = ItemResource.Electrolysis;

    final int moveTicks;

    public ElectrolysisDevice(GameEngine engine) {
        update();
        moveTicks = (int) (engine.getTickProvider().getTicksPerSecond() * 0.05);
    }

    boolean move = false;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(electrolysis.getResource(), move ? 1 : 0, 0, getWidth(), getHeight(), null);
    }

    int moveTicksLeft = 0;
    @Override
    public void tick(long currentTick) {
        if (moveTicksLeft > 0) {
            moveTicksLeft--;
        }else {
            moveTicksLeft = moveTicks;
            move = !move;
            repaint();
        }
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        update();
        super.scale(widthMultiply, heightMultiply);
    }

    public void update() {
        setSize(electrolysis.getWidth() + 1, electrolysis.getHeight());
        repaint();
    }

}
