package de.jp.infoprojekt.gameengine.scenes.gameovershotdead;

import de.jp.infoprojekt.gameengine.scenes.AbstractScene;
import de.jp.infoprojekt.resources.GameResource;
import de.jp.infoprojekt.resources.scenes.GameOverShotDeadSceneResource;

import java.awt.*;

public class GameOverShotDeadScene extends AbstractScene {

    private GameResource background = GameOverShotDeadSceneResource.BACKGROUND;

    public GameOverShotDeadScene() {
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background.getResource(), 0, 0, getWidth(), getHeight(), null);
    }
}
