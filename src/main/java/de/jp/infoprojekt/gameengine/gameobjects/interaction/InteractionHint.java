package de.jp.infoprojekt.gameengine.gameobjects.interaction;

import de.jp.infoprojekt.gameengine.gameobjects.AbstractGameObject;
import de.jp.infoprojekt.gameengine.tick.GameTick;
import de.jp.infoprojekt.resources.ResourceManager;
import de.jp.infoprojekt.resources.ScalingEvent;
import de.jp.infoprojekt.resources.interaction.InteractionResource;
import de.jp.infoprojekt.util.FontManager;

import java.awt.*;

public class InteractionHint extends AbstractGameObject implements ScalingEvent, GameTick {

    private String hint;

    private Font font = FontManager.JERSEY_20;

    private int yOffset;
    private int offsetMax;

    public InteractionHint(String hint) {
        this.hint = hint;
        ResourceManager.addScalingListener(this);

        offsetMax = (int) (20 * ResourceManager.getScaling().getY());
        setSize(InteractionResource.INTERACTION_HINT.getWidth(), InteractionResource.INTERACTION_HINT.getHeight() + offsetMax);
        repaint();
    }

    @Override
    public void tick(long currentTick) {
        yOffset = (int) (offsetMax * (Math.sin((double) currentTick / 40) + 1) / 2);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        System.out.println((float) yOffset / offsetMax);

        g.setColor(Color.GREEN);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        g.setColor(Color.RED);
        g.drawRect(0, yOffset, getWidth() - 1, getHeight() - 1 - offsetMax);

        g.drawImage(InteractionResource.INTERACTION_HINT.getResource(), 0, yOffset, getWidth(), getHeight() - offsetMax, null);

        g.setFont(font.deriveFont((float) 30 * ResourceManager.getScaling().getX()));
        FontMetrics metrics = g.getFontMetrics(g.getFont());

        g.setColor(Color.BLACK);
        g.drawString(hint, getWidth() / 2 - metrics.stringWidth(hint) / 2, getHeight() / 2 + metrics.getDescent() / 2 + yOffset);
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        offsetMax = (int) (20 * ResourceManager.getScaling().getY());

        setSize(InteractionResource.INTERACTION_HINT.getWidth(), InteractionResource.INTERACTION_HINT.getHeight() + offsetMax);
        repaint();
        super.scale(widthMultiply, heightMultiply);
    }
}
