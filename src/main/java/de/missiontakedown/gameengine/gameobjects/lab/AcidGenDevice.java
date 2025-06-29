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
public class AcidGenDevice extends AbstractGameObject implements GameTick {

    private final GameResource acidGen = ItemResource.AcidGen;

    private final int moveTicks;

    private boolean moving = true;

    public AcidGenDevice(GameEngine engine) {
        update();
        moveTicks = (int) (engine.getTickProvider().getTicksPerSecond() * 0.05);
    }

    private boolean move = false;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(acidGen.getResource(), move ? 1 : 0, 0, getWidth(), getHeight(), null);
    }

    private int moveTicksLeft = 0;
    @Override
    public void tick(long currentTick) {
        if (!moving) {
            return;
        }

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
        setSize(acidGen.getWidth() + 1, acidGen.getHeight());
        repaint();
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isMoving() {
        return moving;
    }
}
