package de.missiontakedown.gameengine.gameobjects.overlay;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.gameobjects.AbstractGameObject;
import de.missiontakedown.gameengine.graphics.render.TextRenderer;
import de.missiontakedown.resources.ResourceManager;
import de.missiontakedown.resources.ScalingEvent;
import de.missiontakedown.resources.quest.QuestResource;
import de.missiontakedown.util.FloatPoint;
import de.missiontakedown.util.FontManager;

import javax.swing.*;
import java.awt.*;

/**
 * @author Pascal
 */
public class QuestOverlay extends AbstractGameObject implements ScalingEvent {

    private final GameEngine engine;

    private String questText = "";

    private Font font = FontManager.JERSEY_10;

    public QuestOverlay(GameEngine engine) {
        this.engine = engine;
        setDisableLocationFix(true);

        engine.getStateManager().currentQuestProperty().addListener((observable, oldValue, newValue) -> {
            SwingUtilities.invokeLater(this::updateQuestText);
        });

        updateQuestText();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (questText.isEmpty()) {
            return;
        }

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
        TextRenderer.drawFormattedString(g, questText, 20,getHeight() / 2 + getFontMatrics().getDescent(), getWidth(),getHeight(), getFont(), Integer.MAX_VALUE);
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        updateSize();
        super.scale(widthMultiply, heightMultiply);
    }

    public void updateQuestText() {
        questText = engine.getStateManager().getQuest().getQuestText();
        updateSize();
        repaint();
    }

    private void updateSize() {
        FontMetrics m = getFontMatrics();
        setSize(m.stringWidth(questText) + 40, m.getHeight());
        setRelativeLocation(new FloatPoint(0.99f - (float) (getFontMatrics().stringWidth(questText) + 40) / ResourceManager.getGameScreenWidth(), 0.01f));
        repaint();
    }

    private FontMetrics getFontMatrics() {
        return getFontMetrics(getFont());
    }

    @Override
    public Font getFont() {
        return font.deriveFont((float) 40 * ResourceManager.getScaling().getX());
    }

}
