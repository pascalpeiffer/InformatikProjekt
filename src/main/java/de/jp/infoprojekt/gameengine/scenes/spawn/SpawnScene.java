package de.jp.infoprojekt.gameengine.scenes.spawn;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.gameobjects.player.PlayerCharacter;
import de.jp.infoprojekt.gameengine.graphics.popup.dialog.spawn.PreGameDialog;
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

    private final GameEngine engine;

    private GameResource spawnBackground = SpawnSceneResource.BACKGROUND;

    public SpawnScene(GameEngine engine) {
        this.engine = engine;
        setLayout(null);

        PlayerCharacter player = new PlayerCharacter(engine);
        player.setRelativeLocation(0.5f, 0.9f);
        player.setMoveable(true);
        player.setBlockingArea(SpawnSceneResource.PLAYER_SPACE);
        add(player);

        new PreGameDialog(engine);



        new Timer(500, e -> {
            if (spawnBackground == SpawnSceneResource.BACKGROUND) {
                spawnBackground = SpawnSceneResource.BACKGROUND_DOOR_HALF;
            }else if (spawnBackground == SpawnSceneResource.BACKGROUND_DOOR_HALF) {
                spawnBackground = SpawnSceneResource.BACKGROUND_AGENTS;
            }else if (spawnBackground == SpawnSceneResource.BACKGROUND_AGENTS) {
                spawnBackground = SpawnSceneResource.BACKGROUND;
            }
            repaint();
        });

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
