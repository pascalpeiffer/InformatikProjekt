package de.missiontakedown.gameengine.scenes.arrest;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.graphics.fade.AbstractFade;
import de.missiontakedown.gameengine.graphics.render.TextRenderer;
import de.missiontakedown.gameengine.scenes.AbstractScene;
import de.missiontakedown.gameengine.state.QuestState;
import de.missiontakedown.gameengine.tick.GameTick;
import de.missiontakedown.resources.ResourceManager;
import de.missiontakedown.resources.scenes.ArrestSceneResource;
import de.missiontakedown.util.FontManager;

import java.awt.*;

public class ArrestScene extends AbstractScene implements GameTick {

    private final GameEngine engine;

    private int ticksLeft;
    private final AbstractScene goBackToScene;
    private final AbstractFade fade;

    private Font font = FontManager.JERSEY_10;

    public ArrestScene(GameEngine engine, int arrestTimeInSec, AbstractScene goBackToScene, AbstractFade fade) {
        this.engine = engine;
        this.ticksLeft = arrestTimeInSec * engine.getTickProvider().getTicksPerSecond();
        this.goBackToScene = goBackToScene;
        this.fade = fade;
    }

    @Override
    public void tick(long currentTick) {
        if (ticksLeft > 0) {
            ticksLeft--;
            repaint();
            return;
        }

        engine.getStateManager().setQuest(QuestState.GO_TO_FARMER);

        if (fade != null) {
            engine.getGraphics().switchToScene(goBackToScene, fade);
        }else {
            engine.getGraphics().switchToScene(goBackToScene);
        }
        engine.getTickProvider().unregisterTick(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Font f = font.deriveFont(60 * ResourceManager.getScaling().getX());
        FontMetrics m = g.getFontMetrics(f);
        String timeString = toTime(ticksLeft / engine.getTickProvider().getTicksPerSecond());
        TextRenderer.drawFormattedString(g, timeString, getWidth()  / 2 - m.stringWidth(timeString) / 2, (int) ((double) getHeight() / 10 * 9.5 - (double) m.getHeight() / 2), getWidth(), getHeight(), f, Integer.MAX_VALUE);
    }

    private String toTime(int time) {
        int hours = time / 3600;
        int minutes = (time / 60) % 60;
        int seconds = time % 60;

        if (time < 60) {
            return String.valueOf(seconds);
        } else if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%d:%02d", minutes, seconds);
        }
    }

    @Override
    public void sceneShown() {
        super.sceneShown();
        engine.getTickProvider().registerTick(this);
        ArrestSceneResource.LOCKED_UP.create().play();
    }

    @Override
    public void sceneHidden() {
        super.sceneHidden();
        engine.getTickProvider().unregisterTick(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(ArrestSceneResource.BACKGROUND.getResource(), 0, 0, getWidth(), getHeight(), null);
    }
}
