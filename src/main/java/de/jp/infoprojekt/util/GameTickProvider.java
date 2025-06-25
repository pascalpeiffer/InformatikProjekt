package de.jp.infoprojekt.util;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GameTickProvider class
 *
 * @author Pascal
 * @version 25.06.2025
 */
public class GameTickProvider {

    private final int tickDelay;

    private final List<Runnable> runnables = new ArrayList<>();

    public GameTickProvider(int ticksPerSecond) {
        tickDelay = 1000 / ticksPerSecond;

        new Timer(tickDelay, e -> {
            runnables.forEach(Runnable::run);
        }).start();
    }

    public void onTick(Runnable r) {
        runnables.add(r);
    }
}
