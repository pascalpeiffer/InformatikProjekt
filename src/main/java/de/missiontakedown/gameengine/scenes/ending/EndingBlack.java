package de.missiontakedown.gameengine.scenes.ending;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.graphics.fade.BlackFade;
import de.missiontakedown.gameengine.scenes.AbstractScene;
import de.missiontakedown.gameengine.scenes.main.TitleScene;
import de.missiontakedown.resources.ResourceManager;
import de.missiontakedown.resources.ScalingEvent;
import de.missiontakedown.settings.GAME_SETTINGS;

import javax.swing.*;
import java.awt.*;

/**
 * @author Pascal
 */
public class EndingBlack extends AbstractScene implements ScalingEvent {

    private final GameEngine engine;

    public EndingBlack(GameEngine engine) {
        this.engine = engine;
        setLayout(null);
        setBackground(Color.BLACK);

        ResourceManager.addScalingListener(this);

    }

    @Override
    public void sceneShown() {
        super.sceneShown();

        if (GAME_SETTINGS.FALKE & GAME_SETTINGS.TASSE_KAFFEE != null && GAME_SETTINGS.TASSE_KAFFEE.isActive()) {
            GAME_SETTINGS.TASSE_KAFFEE.onEnd(() -> {
                SwingUtilities.invokeLater(() -> {
                    engine.getGraphics().switchToScene(new TitleScene(engine), new BlackFade(engine));
                });
            });
        }else {
            engine.getGraphics().switchToScene(new TitleScene(engine), new BlackFade(engine));
        }
    }

    @Override
    public void sceneHidden() {
        super.sceneHidden();
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        repaint();
    }

}
