package de.missiontakedown.gameengine.gameobjects.interaction;

import de.missiontakedown.gameengine.gameobjects.AbstractGameObject;
import de.missiontakedown.gameengine.graphics.render.TextRenderer;
import de.missiontakedown.resources.ResourceManager;
import de.missiontakedown.resources.ScalingEvent;
import de.missiontakedown.resources.dialog.DialogResource;
import de.missiontakedown.util.FontManager;

import java.awt.*;

/**
 * @author Pascal
 */
public class InteractionHint extends AbstractGameObject implements ScalingEvent {

    private String hint;

    private Font font = FontManager.JERSEY_10;

    public InteractionHint(String hint) {
        this.hint = hint;
        setDisableLocationFix(true);
        setRelativeLocation(0, 0);

        update();
    }

    @Override
    public boolean contains(int x, int y) {
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Font f = font.deriveFont(40 * ResourceManager.getScaling().getX());
        FontMetrics metrics = g.getFontMetrics(f);

        int interactionHintWidth = getWidth() / 6;
        int interactionHintHeight = getHeight() / 15;

        if (metrics.stringWidth(hint) > interactionHintWidth * 0.8f) {
            interactionHintWidth = getWidth() / 4;
        }

        int interactionHintBottomDistance = interactionHintHeight / 2;

        g.drawImage(DialogResource.DIALOG.getResource(), getWidth() / 2 - interactionHintWidth / 2, getHeight() - interactionHintHeight - interactionHintBottomDistance, interactionHintWidth, interactionHintHeight, null);

        TextRenderer.drawFormattedString(g, hint, getWidth() / 2 - metrics.stringWidth(hint) / 2, getHeight() - interactionHintHeight + metrics.getDescent(), 1000, 1000, f, Integer.MAX_VALUE);
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        update();
        super.scale(widthMultiply, heightMultiply);
    }

    private void update() {
        setSize(ResourceManager.getGameScreenWidth(), ResourceManager.getGameScreenHeight());
        repaint();
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public void setFont(Font font) {
        this.font = font;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
