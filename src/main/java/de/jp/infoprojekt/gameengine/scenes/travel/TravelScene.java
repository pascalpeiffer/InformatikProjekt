package de.jp.infoprojekt.gameengine.scenes.travel;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.gameobjects.travel.TravelCar;
import de.jp.infoprojekt.gameengine.scenes.AbstractScene;
import de.jp.infoprojekt.gameengine.tick.GameTick;
import de.jp.infoprojekt.resources.scenes.TravelSceneResource;

import java.awt.*;

public class TravelScene extends AbstractScene implements GameTick {

    private final GameEngine engine;

    private int travelTicksLeft;
    private final int travelTicks;
    private final boolean leftToRight;
    private final TravelCar car;
    private final Runnable travelEnd;

    public TravelScene(GameEngine engine, boolean leftToRight, int travelTimeInSec, Runnable travelEnd) {
        this.engine = engine;
        this.leftToRight = leftToRight;
        this.travelTicks = engine.getTickProvider().getTicksPerSecond() * travelTimeInSec;
        this.travelTicksLeft = travelTicks;
        this.travelEnd = travelEnd;

        car = new TravelCar();
        car.setFlipped(leftToRight);
        car.setRelativeY(0.62f);
        add(car);
    }

    @Override
    public void tick(long currentTick) {
        travelTicksLeft--;

        float carWidth = ((float) car.getWidth() / 1920);
        float travel = (float) travelTicksLeft / travelTicks;

        car.setRelativeX(leftToRight ? 1-travel - (carWidth * 2): travel);
        car.repaint();

        if (travel <= - carWidth * 2) {
            travelEnd.run();
            engine.getTickProvider().unregisterTick(this);
        }

    }

    @Override
    public void sceneShown() {
        super.sceneShown();
        engine.getTickProvider().registerTick(this);
    }

    @Override
    public void sceneHidden() {
        super.sceneHidden();
        engine.getTickProvider().unregisterTick(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(TravelSceneResource.BACKGROUND.getResource(), 0, 0,getWidth(), getHeight(), null);
    }
}
