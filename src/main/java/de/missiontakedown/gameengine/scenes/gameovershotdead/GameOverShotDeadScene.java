package de.missiontakedown.gameengine.scenes.gameovershotdead;

import de.missiontakedown.gameengine.scenes.AbstractScene;
import de.missiontakedown.resources.GameResource;
import de.missiontakedown.resources.scenes.GameOverShotDeadSceneResource;

import java.awt.*;

public class GameOverShotDeadScene extends AbstractScene {

    private final GameResource background = GameOverShotDeadSceneResource.BACKGROUND;

    public GameOverShotDeadScene() {
        setLayout(null);
    }

    @Override
    public void sceneShown() {
        super.sceneShown();
        GameOverShotDeadSceneResource.GAME_OVER.create().play();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background.getResource(), 0, 0, getWidth(), getHeight(), null);
    }
}
