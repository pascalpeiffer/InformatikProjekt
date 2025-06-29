package de.jp.infoprojekt.gameengine.gameobjects.lab;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.gameobjects.AbstractGameObject;
import de.jp.infoprojekt.gameengine.tick.GameTick;
import de.jp.infoprojekt.resources.GameResource;
import de.jp.infoprojekt.resources.item.ItemResource;

import java.awt.*;

public class Nitrator extends AbstractGameObject implements GameTick {

    private final GameResource nitrator = ItemResource.Nitrator;

    final int moveTicks;

    public Nitrator(GameEngine engine) {
        update();
        moveTicks = (int) (engine.getTickProvider().getTicksPerSecond() * 0.05);
    }

    boolean move = false;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(nitrator.getResource(), move ? 1 : 0, 0, getWidth(), getHeight(), null);
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
        setSize(nitrator.getWidth() + 1, nitrator.getHeight());
        repaint();
    }

}
