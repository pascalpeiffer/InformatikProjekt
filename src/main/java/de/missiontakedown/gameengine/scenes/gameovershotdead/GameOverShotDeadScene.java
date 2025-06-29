package de.missiontakedown.gameengine.scenes.gameovershotdead;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.graphics.fade.BlackFade;
import de.missiontakedown.gameengine.scenes.AbstractScene;
import de.missiontakedown.gameengine.scenes.ending.EndingScene;
import de.missiontakedown.gameengine.tick.GameTick;
import de.missiontakedown.resources.GameResource;
import de.missiontakedown.resources.ScalingEvent;
import de.missiontakedown.resources.scenes.GameOverShotDeadSceneResource;

import java.awt.*;

public class GameOverShotDeadScene extends AbstractScene implements ScalingEvent, GameTick {

    private final GameResource background = GameOverShotDeadSceneResource.BACKGROUND;

    private final GameEngine engine;

    public GameOverShotDeadScene(GameEngine engine) {
        setLayout(null);
        this.engine = engine;

        displayTime = engine.getTickProvider().getTicksPerSecond() * 4;
    }

    private int displayTime;
    @Override
    public void tick(long currentTick) {
        if (displayTime > 0) {
            displayTime--;
            return;
        }

        engine.getGraphics().switchToScene(new EndingScene(engine), new BlackFade(engine));
        engine.getTickProvider().unregisterTick(this);
    }

    @Override
    public void sceneShown() {
        super.sceneShown();
        GameOverShotDeadSceneResource.GAME_OVER.create().play();
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

        g.drawImage(background.getResource(), 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        repaint();
    }
}
