package de.jp.infoprojekt.gameengine.gameobjects.overlay;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.gameobjects.AbstractGameObject;
import de.jp.infoprojekt.gameengine.graphics.render.TextRenderer;
import de.jp.infoprojekt.resources.GameResource;
import de.jp.infoprojekt.resources.ResourceManager;
import de.jp.infoprojekt.resources.scenes.LabSceneResource;
import de.jp.infoprojekt.util.FloatPoint;
import de.jp.infoprojekt.util.FloatRectangle;
import de.jp.infoprojekt.util.FontManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DistillationOverlay extends AbstractGameObject {

    private final GameEngine engine;

    private Font font = FontManager.JERSEY_10;

    private FloatRectangle toggleSwitch = new FloatRectangle(0.04f, 0.72f,0.04f, 0.13f);
    private FloatRectangle downArrow = new FloatRectangle(0.3f, 0.72f, 0.08f, 0.13f);
    private FloatRectangle upArrow = new FloatRectangle(0.6f, 0.72f, 0.08f, 0.13f);

    private GameResource toggleSwitchResource = LabSceneResource.SWITCH_OFF;

    private final String[] temps = new String[] {"380°", "290°", "100°"};
    private int tempIndex = 0;

    private boolean changeable = true;

    private Runnable callback;

    public DistillationOverlay(GameEngine engine) {
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
        if (toggleSwitch.contains(mouseHit)) {
            if (toggleSwitchResource == LabSceneResource.SWITCH_OFF) {
                toggleSwitchResource = LabSceneResource.SWITCH_ON;
                changeable = false;
                callback.run();
            }
        }

        if (changeable) {
            if (downArrow.contains(mouseHit)) {
                tempIndex--;
                if (tempIndex < 0) {
                    tempIndex = temps.length - 1;
                }
            }else if (upArrow.contains(mouseHit)) {
                tempIndex++;
                if (tempIndex >= temps.length) {
                    tempIndex = 0;
                }
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        g.drawImage(LabSceneResource.ARROW_DOWN.getResource(), getWidth() / 2 - LabSceneResource.TEMP_GAUGE.getWidth() / 2 - LabSceneResource.ARROW_DOWN.getWidth(), (int) (ResourceManager.getGameScreenHeight() * 0.7f), LabSceneResource.ARROW_DOWN.getWidth(), LabSceneResource.ARROW_DOWN.getHeight(), null);
        g.drawImage(LabSceneResource.ARROW_UP.getResource(), getWidth() / 2 + LabSceneResource.TEMP_GAUGE.getWidth() / 2, (int) (ResourceManager.getGameScreenHeight() * 0.7f), LabSceneResource.ARROW_UP.getWidth(), LabSceneResource.ARROW_UP.getHeight(), null);
        g.drawImage(LabSceneResource.TEMP_GAUGE.getResource(), getWidth() / 2 - LabSceneResource.TEMP_GAUGE.getWidth() / 2, (int) (ResourceManager.getGameScreenHeight() * 0.6f), LabSceneResource.TEMP_GAUGE.getWidth(), LabSceneResource.TEMP_GAUGE.getHeight(), null);

        g.drawImage(toggleSwitchResource.getResource(), (int) (ResourceManager.getGameScreenWidth() * 0.01f), (int) (ResourceManager.getGameScreenHeight() * 0.7f), toggleSwitchResource.getWidth(), toggleSwitchResource.getHeight(), null);

        String text = temps[tempIndex];
        //TODO remove fix 10000
        TextRenderer.drawFormattedString(g, text, getWidth() / 2 - getFontMatrics().stringWidth(text) / 2 - (LabSceneResource.TEMP_GAUGE.getWidth() / 9), (int) (ResourceManager.getGameScreenHeight() * 0.834f) - getFontMatrics().getDescent(), 10000, 10000, getFont(), Integer.MAX_VALUE);
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

    public int getTempIndex() {
        return tempIndex;
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    public void setChangeable(boolean changeable) {
        this.changeable = changeable;
    }

    public void setToggleSwitch(boolean on) {
        toggleSwitchResource = on ? LabSceneResource.SWITCH_ON : LabSceneResource.SWITCH_OFF;
        repaint();
    }
}
