package de.jp.infoprojekt.gameengine.gameobjects.overlay;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.gameobjects.AbstractGameObject;
import de.jp.infoprojekt.gameengine.graphics.render.TextRenderer;
import de.jp.infoprojekt.gameengine.tick.GameTick;
import de.jp.infoprojekt.resources.ResourceManager;
import de.jp.infoprojekt.resources.scenes.LabSceneResource;
import de.jp.infoprojekt.util.FloatPoint;
import de.jp.infoprojekt.util.FloatRectangle;
import de.jp.infoprojekt.util.FontManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NitratorOverlay extends AbstractGameObject implements GameTick {

    private final GameEngine engine;

    private final FloatRectangle arrowDown = new FloatRectangle(0.05f, 0.72f, 0.08f, 0.13f);
    private final FloatRectangle arrowUp = new FloatRectangle(0.26f, 0.72f, 0.08f, 0.13f);
    private final FloatRectangle cooling = new FloatRectangle(0.76f, 0.72f, 0.08f, 0.13f);

    private Font font = FontManager.JERSEY_10;

    private float dropRate;

    private int temp = 18;

    public NitratorOverlay(GameEngine engine) {
        this.engine = engine;
        setDisableLocationFix(true);
        setRelativeLocation(new FloatPoint(0f, 0f));
        updateSize();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                float relativeX = (float) e.getX() / ResourceManager.getGameScreenWidth();
                float relativeY = (float) e.getY() / ResourceManager.getGameScreenHeight();

                FloatPoint mouseHit = new FloatPoint(relativeX, relativeY);

                onMouseClick(mouseHit);
            }
        });
    }

    private void onMouseClick(FloatPoint mouseHit) {
        if (arrowDown.contains(mouseHit)) {
            dropRate -= 0.2f;
            if (dropRate < 0) {
                dropRate = 0;
            }
            repaint();
        }else if (arrowUp.contains(mouseHit)) {
            dropRate += 0.2f;
            repaint();
        }else if (cooling.contains(mouseHit)) {
            temp = Math.max(18, temp-1);
            repaint();
        }
    }

    private long lastDrop;
    @Override
    public void tick(long currentTick) {
        if (dropRate != 0 && lastDrop + (engine.getTickProvider().getTicksPerSecond() / dropRate) <= currentTick) {
            lastDrop = currentTick;

            temp += 2;
            repaint();
            //Drop
            LabSceneResource.DROP.create().play();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(LabSceneResource.TEMP_GAUGE.getResource(), getWidth() / 2 - LabSceneResource.TEMP_GAUGE.getWidth() / 2, (int) (ResourceManager.getGameScreenHeight() * 0.6f), LabSceneResource.TEMP_GAUGE.getWidth(), LabSceneResource.TEMP_GAUGE.getHeight(), null);

        g.drawImage(LabSceneResource.ARROW_DOWN.getResource(), getWidth() / 4 - LabSceneResource.ARROW_DOWN.getWidth() * 2, (int) (ResourceManager.getGameScreenHeight() * 0.7f), LabSceneResource.ARROW_DOWN.getWidth(), LabSceneResource.ARROW_DOWN.getHeight(), null);
        g.drawImage(LabSceneResource.EMPTY_TEMPLATE.getResource(), getWidth() / 4 - LabSceneResource.EMPTY_TEMPLATE.getWidth(), (int) (ResourceManager.getGameScreenHeight() * 0.7f), LabSceneResource.EMPTY_TEMPLATE.getWidth(), LabSceneResource.EMPTY_TEMPLATE.getHeight(), null);
        g.drawImage(LabSceneResource.ARROW_UP.getResource(), getWidth() / 4, (int) (ResourceManager.getGameScreenHeight() * 0.7f), LabSceneResource.ARROW_UP.getWidth(), LabSceneResource.ARROW_UP.getHeight(), null);

        g.drawImage(LabSceneResource.COOL_BUTTON.getResource(), getWidth() / 4 * 3, (int) (ResourceManager.getGameScreenHeight() * 0.7f), LabSceneResource.COOL_BUTTON.getWidth(), LabSceneResource.COOL_BUTTON.getHeight(), null);

        //TODO remove fix 10000
        String tempString = temp + "°";
        TextRenderer.drawFormattedString(g, tempString, getWidth() / 2 - getFontMatrics().stringWidth(tempString) / 2 - (LabSceneResource.TEMP_GAUGE.getWidth() / 9), (int) (ResourceManager.getGameScreenHeight() * 0.834f) - getFontMatrics().getDescent(), 10000, 10000, getFont(), Integer.MAX_VALUE);

        String dropRateString = Float.toString(dropRate);
        TextRenderer.drawFormattedString(g, dropRateString, getWidth() / 4 - (LabSceneResource.EMPTY_TEMPLATE.getWidth() / 2) - getFontMatrics().stringWidth(dropRateString) / 2, (int) (ResourceManager.getGameScreenHeight() * 0.834f) - getFontMatrics().getDescent(), 10000, 10000, getFont(), Integer.MAX_VALUE);

        String description = "Tropfen pro Sekunde";
        TextRenderer.drawFormattedString(g, description, getWidth() / 4 - (LabSceneResource.EMPTY_TEMPLATE.getWidth() / 2) - getFontMatrics().stringWidth(description) / 2, (int) (ResourceManager.getGameScreenHeight() * 0.7f) - getFontMatrics().getDescent(), 10000, 10000, getFont(), Integer.MAX_VALUE);
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        updateSize();
        super.scale(widthMultiply, heightMultiply);
    }

    private void updateSize() {
        setSize(ResourceManager.getGameScreenWidth(), ResourceManager.getGameScreenHeight());
        repaint();
    }

    private FontMetrics getFontMatrics() {
        return getFontMetrics(getFont());
    }

    @Override
    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    public Font getFont() {
        return font.deriveFont((float) 80 * ResourceManager.getScaling().getX());
    }
}
