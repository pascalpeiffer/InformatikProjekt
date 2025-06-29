package de.jp.infoprojekt.gameengine.gameobjects.overlay;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.gameobjects.AbstractGameObject;
import de.jp.infoprojekt.gameengine.graphics.render.TextRenderer;
import de.jp.infoprojekt.resources.ResourceManager;
import de.jp.infoprojekt.resources.ScalingEvent;
import de.jp.infoprojekt.resources.quest.QuestResource;
import de.jp.infoprojekt.util.FontManager;

import javax.swing.*;
import java.awt.*;

public class TextOverlay extends AbstractGameObject implements ScalingEvent {

    private String text = "";

    private Font font = FontManager.JERSEY_10;

    public TextOverlay(String text) {
        this.text = text;
        setDisableLocationFix(true);
        updateSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int frameWidth = (int) (((float) getHeight() / QuestResource.QUEST_FRAME1.getResource().getHeight()) * QuestResource.QUEST_FRAME1.getResource().getWidth());

        float amount = (float) getWidth() / frameWidth;

        int count = 0;
        for (int i = 0; i < amount - 1; i++) {
            g.drawImage(QuestResource.QUEST_FRAME2.getResource(), 4 + i * frameWidth, 0, frameWidth, getHeight(), null);
            count++;
        }

        //Rest
        amount = amount - count;
        g.drawImage(QuestResource.QUEST_FRAME2.getResource(), 4 + count * frameWidth, 0, (int) (frameWidth * amount), getHeight(), null);

        //Start and End
        g.drawImage(QuestResource.QUEST_FRAME1.getResource(),0, 0, frameWidth, getHeight(), null);
        g.drawImage(QuestResource.QUEST_FRAME3.getResource(),getWidth() - frameWidth, 0, frameWidth, getHeight(), null);

        //g.drawImage(QuestResource.QUEST_FRAME.getResource(), 0, 0,getWidth(), getHeight(), null);
        TextRenderer.drawFormattedString(g, text, 20,getHeight() / 2 + getFontMatrics().getDescent(), getWidth(),getHeight(), getFont(), Integer.MAX_VALUE);
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        updateSize();
        super.scale(widthMultiply, heightMultiply);
    }

    private void updateSize() {
        FontMetrics m = getFontMatrics();
        setSize(m.stringWidth(text) + 40, m.getHeight());
        repaint();
    }

    private FontMetrics getFontMatrics() {
        return getFontMetrics(getFont());
    }

    @Override
    public Font getFont() {
        return font.deriveFont((float) 40 * ResourceManager.getScaling().getX());
    }

    public void setText(String text) {
        this.text = text;
        repaint();
    }

    public String getText() {
        return text;
    }
}
