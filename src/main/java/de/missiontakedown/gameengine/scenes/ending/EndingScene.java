package de.missiontakedown.gameengine.scenes.ending;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.graphics.fade.BlackFade;
import de.missiontakedown.gameengine.scenes.AbstractScene;
import de.missiontakedown.gameengine.tick.GameTick;
import de.missiontakedown.resources.ResourceManager;
import de.missiontakedown.resources.ScalingEvent;
import de.missiontakedown.resources.scenes.EndSceneResource;
import de.missiontakedown.resources.scenes.TitleSceneResource;
import de.missiontakedown.settings.GAME_SETTINGS;

import java.awt.*;

public class EndingScene extends AbstractScene implements ScalingEvent, GameTick {

    private final GameEngine engine;

    public EndingScene(GameEngine engine) {
        this.engine = engine;
        setLayout(null);
        setBackground(Color.BLACK);

        ResourceManager.addScalingListener(this);

        displayTime = engine.getTickProvider().getTicksPerSecond() * 4;
    }

    private int displayTime;
    @Override
    public void tick(long currentTick) {
        if (displayTime > 0) {
            displayTime--;
            return;
        }

        engine.getGraphics().switchToScene(new EndingSceneAuthors(engine), new BlackFade(engine));
        engine.getTickProvider().unregisterTick(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        float scaling = 2f;
        int width = (int) (TitleSceneResource.TAKE_DOWN_TITLE.getWidth() * scaling);
        int height = (int) (TitleSceneResource.TAKE_DOWN_TITLE.getHeight() * scaling);
        g.drawImage(TitleSceneResource.TAKE_DOWN_TITLE.getResource(), getWidth() / 2 - width / 2, getHeight() / 2 - height / 2, width, height, null);
    }

    @Override
    public void sceneShown() {
        super.sceneShown();
        engine.getTickProvider().registerTick(this);
        if (GAME_SETTINGS.FALKE) {
            GAME_SETTINGS.TASSE_KAFFEE = EndSceneResource.ENDING_TASSE_KAFFEE.create().play();
        }
    }

    @Override
    public void sceneHidden() {
        super.sceneHidden();
        engine.getTickProvider().unregisterTick(this);
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        repaint();
    }
}
