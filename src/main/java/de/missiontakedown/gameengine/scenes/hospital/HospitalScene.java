package de.missiontakedown.gameengine.scenes.hospital;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.graphics.fade.BlackFade;
import de.missiontakedown.gameengine.graphics.render.TextRenderer;
import de.missiontakedown.gameengine.scenes.AbstractScene;
import de.missiontakedown.gameengine.tick.GameTick;
import de.missiontakedown.resources.GameAudioResource;
import de.missiontakedown.resources.GameResource;
import de.missiontakedown.resources.ResourceManager;
import de.missiontakedown.resources.scenes.HospitalSceneResource;
import de.missiontakedown.util.FontManager;

import java.awt.*;

public class HospitalScene extends AbstractScene implements GameTick {

    private final GameResource background = HospitalSceneResource.BACKGROUND;

    private final GameEngine engine;

    private int ticksLeft;

    private Font font = FontManager.JERSEY_10;

    public HospitalScene(GameEngine engine) {
        this.engine = engine;
        setLayout(null);
        ticksLeft = engine.getTickProvider().getTicksPerSecond() * 60;
    }

    @Override
    public void tick(long currentTick) {
        if (ticksLeft > 0) {
            ticksLeft--;
            repaint();
            return;
        }

        engine.rollbackToNitratorScene(new BlackFade(engine));

        engine.getTickProvider().unregisterTick(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Font f = font.deriveFont(60 * ResourceManager.getScaling().getX());
        FontMetrics m = g.getFontMetrics(f);
        String timeString = toTime(ticksLeft / engine.getTickProvider().getTicksPerSecond());
        TextRenderer.drawFormattedString(g, timeString, getWidth()  / 2 - m.stringWidth(timeString) / 2, (int) ((double) getHeight() / 10 * 8.5 - (double) m.getHeight() / 2), getWidth(), getHeight(), f, Integer.MAX_VALUE);
    }

    private GameAudioResource.Instance hospitalAmbient;

    @Override
    public void sceneShown() {
        super.sceneShown();
        engine.getTickProvider().registerTick(this);
        HospitalSceneResource.GAME_OVER.create().play();
        hospitalAmbient = HospitalSceneResource.AMBIENT.create().play();
    }

    @Override
    public void sceneHidden() {
        super.sceneHidden();
        engine.getTickProvider().unregisterTick(this);
        if (hospitalAmbient.isActive()) {
            hospitalAmbient.stop();
        }
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background.getResource(), 0, 0, getWidth(), getHeight(), null);
    }
}
