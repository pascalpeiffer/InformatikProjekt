package de.missiontakedown.gameengine.scenes.ending;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.graphics.fade.BlackFade;
import de.missiontakedown.gameengine.graphics.render.TextRenderer;
import de.missiontakedown.gameengine.scenes.AbstractScene;
import de.missiontakedown.gameengine.scenes.main.TitleScene;
import de.missiontakedown.gameengine.tick.GameTick;
import de.missiontakedown.resources.ResourceManager;
import de.missiontakedown.resources.ScalingEvent;
import de.missiontakedown.settings.GAME_SETTINGS;
import de.missiontakedown.util.FontManager;

import java.awt.*;

/**
 * @author Pascal
 */
public class EndingSceneAuthors extends AbstractScene implements ScalingEvent, GameTick {

    private final Font titleFont = FontManager.JERSEY_20;

    private final GameEngine engine;

    public EndingSceneAuthors(GameEngine engine) {
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

        if (GAME_SETTINGS.FALKE) {
            engine.getGraphics().switchToScene(new EndingBlack(engine), new BlackFade(engine));
        }else {
            engine.getGraphics().switchToScene(new TitleScene(engine), new BlackFade(engine));
        }

        engine.getTickProvider().unregisterTick(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        FontMetrics metrics = g.getFontMetrics(getTitleFont());

        String authors = "by Jan & Pascal";
        //TODO fix max 10000
        TextRenderer.drawFormattedString(g, authors, getWidth() / 2 - metrics.stringWidth(authors) / 2, getHeight() / 2 + metrics.getDescent(), 10000, 10000, getTitleFont(), Integer.MAX_VALUE);
    }

    @Override
    public void sceneShown() {
        super.sceneShown();
        engine.getTickProvider().registerTick(this);
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

    public Font getTitleFont() {
        return titleFont.deriveFont(180 * ResourceManager.getScaling().getX());
    }
}
