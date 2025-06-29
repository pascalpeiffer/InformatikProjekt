package de.missiontakedown.gameengine.tick;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GameTickProvider {

    private final int tickDelay;

    private final List<GameTick> runnables = new ArrayList<>();

    private final List<GameTick> addingRunnables = new ArrayList<>();
    private final List<GameTick> removingRunnables = new ArrayList<>();

    private long tick = 0;

    public GameTickProvider(int ticksPerSecond) {
        tickDelay = 1000 / ticksPerSecond;

        new Timer(tickDelay, e -> {
            //TODO add dev overlay to show ticks per second and the time it took
            runnables.removeAll(removingRunnables);
            removingRunnables.clear();
            runnables.addAll(addingRunnables);
            addingRunnables.clear();
            runnables.forEach(gameTick -> gameTick.tick(tick));
            tick++;
        }).start();
    }

    public void registerTick(GameTick tick) {
        addingRunnables.add(tick);
    }

    public void unregisterTick(GameTick tick) {
        removingRunnables.add(tick);
    }

    public long getTick() {
        return tick;
    }

    public int getTickDelay() {
        return tickDelay;
    }

    public int getTicksPerSecond() {
        return 1000 / getTickDelay();
    }
}
