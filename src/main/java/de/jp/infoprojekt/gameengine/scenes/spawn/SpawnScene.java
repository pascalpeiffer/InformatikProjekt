package de.jp.infoprojekt.gameengine.scenes.spawn;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.gameobjects.player.PlayerCharacter;
import de.jp.infoprojekt.gameengine.scenes.AbstractScene;
import de.jp.infoprojekt.resources.GameResource;
import de.jp.infoprojekt.resources.ResourceManager;
import de.jp.infoprojekt.resources.ScalingEvent;
import de.jp.infoprojekt.resources.scenes.SpawnSceneResource;

import javax.swing.*;
import java.awt.*;

/**
 * SpawnScene class
 *
 * @author Pascal
 * @version 24.06.2025
 */
public class SpawnScene extends AbstractScene implements ScalingEvent {

    private GameEngine engine;

    private GameResource spawnBackground = SpawnSceneResource.SPAWN_BACKGROUND;

    public SpawnScene(GameEngine engine) {
        this.engine = engine;
        setLayout(null);

        PlayerCharacter player = new PlayerCharacter(engine);
        player.setRelativeLocation(0.5f, 0.5f);
        player.setMoveable(true);
        player.setBlockingArea(getPlayerBlockingArea());
        add(player);

        //addBlockingArea();

        new Timer(500, e -> {
            if (spawnBackground == SpawnSceneResource.SPAWN_BACKGROUND) {
                spawnBackground = SpawnSceneResource.SPAWN_BACKGROUND_DOOR_HALF;
            }else if (spawnBackground == SpawnSceneResource.SPAWN_BACKGROUND_DOOR_HALF) {
                spawnBackground = SpawnSceneResource.SPAWN_BACKGROUND_AGENTS;
            }else if (spawnBackground == SpawnSceneResource.SPAWN_BACKGROUND_AGENTS) {
                spawnBackground = SpawnSceneResource.SPAWN_BACKGROUND;
            }
            repaint();
        }).start();

        ResourceManager.addScalingListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(spawnBackground.getResource(), 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void scale(float width, float height) {
        repaint();
    }
}
