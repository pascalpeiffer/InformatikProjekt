package de.jp.infoprojekt.gameengine.scenes.travel;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.gameobjects.travel.TravelCar;
import de.jp.infoprojekt.gameengine.scenes.AbstractScene;
import de.jp.infoprojekt.gameengine.tick.GameTick;
import de.jp.infoprojekt.resources.GameAudioResource;
import de.jp.infoprojekt.resources.scenes.TravelSceneResource;

import javax.sound.sampled.Clip;
import java.awt.*;

//TODO fix and add audio?
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
        car.setRelativeY(0.88f);
        add(car);
        repaint();
    }

    @Override
    public void tick(long currentTick) {
        travelTicksLeft--;

        float travel = (float) travelTicksLeft / travelTicks;

        travel = map(travel);

        car.setRelativeX(leftToRight ? 1 - travel : travel);

        if (travel <= -0.2f) {
            travelEnd.run();
            engine.getTickProvider().unregisterTick(this);
        }

    }

    private float map(float x) {
        float fromMin = 0.0f;
        float fromMax = 1.0f;

        float toMin = -0.2f;
        float toMax = 1.2f;

        return toMin + ((x - fromMin) / (fromMax - fromMin)) * (toMax - toMin);
    }

    private float mapAudio(float x) {
        float fromMin = -0.2f;
        float fromMax = 0.5f;

        float toMin = 0f;
        float toMax = 1f;

        return toMin + ((x - fromMin) / (fromMax - fromMin)) * (toMax - toMin);
    }


    private GameAudioResource.Instance audioInstance;
    @Override
    public void sceneShown() {
        super.sceneShown();
        car.update();
        engine.getTickProvider().registerTick(this);
        audioInstance = TravelSceneResource.BACKGROUND_AUDIO.create().loop(Clip.LOOP_CONTINUOUSLY).play();
    }

    @Override
    public void sceneHidden() {
        super.sceneHidden();
        engine.getTickProvider().unregisterTick(this);
        if (audioInstance != null && audioInstance.isActive()) {
            audioInstance.stop();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(TravelSceneResource.BACKGROUND.getResource(), 0, 0,getWidth(), getHeight(), null);
    }
}
