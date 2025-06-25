package de.jp.infoprojekt.gameengine.graphics.popup;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.resources.ResourceManager;
import de.jp.infoprojekt.resources.ScalingEvent;
import de.jp.infoprojekt.resources.dialog.Dialog;
import de.jp.infoprojekt.util.FontManager;
import de.jp.infoprojekt.util.MinecraftTextRenderer;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractDialog extends JComponent implements ScalingEvent {

    private final float widthDivider = 2;
    private final float heightDivider = 4;
    private final float bottomSeparateDivider = 25;

    private String title = "Mr. G:";
    private String mainText = "Ich verstehe. §4Eagle es war eine Ehre mit dir zu arbeiten. Trotzdem verfügst du leider über zu viele Informationen von vergangenen Missionen, dass wir dich einfach aufhören lassen können. Wie gesagt es war mir eine Ehre. Ich verstehe. Eagle es war eine Ehre mit dir zu arbeiten. Trotzdem verfügst du leider über zu viele Informationen von vergangenen Missionen, dass wir dich einfach aufhören lassen können. Wie gesagt es war mir eine Ehre.";

    private Font titleFont = FontManager.JERSEY_10;
    private Font mainFont = FontManager.JERSEY_20;

    private int index = 0;

    Timer timer;
    public AbstractDialog(GameEngine engine) {
        setFocusable(false);
        ResourceManager.addScalingListener(this);

        engine.getGraphics().addDialogLayer(this);

        setSizeAndLoc();

        timer = new Timer(50, e -> {
            if (index >= mainText.length()) {
                timer.stop();
            }
            index++;
            repaint();
        });
        timer.start();
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        setSizeAndLoc();
    }

    private void setSizeAndLoc() {
        setSize((int) (ResourceManager.getGameScreenWidth() / widthDivider), (int) (ResourceManager.getGameScreenHeight() / heightDivider));
        setLocation(ResourceManager.getGameScreenWidth() / 2 - getWidth() / 2, (int) ((int) (ResourceManager.getGameScreenHeight() / heightDivider * (heightDivider -  1)) - (ResourceManager.getGameScreenHeight() / bottomSeparateDivider)));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.GREEN);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        g.drawImage(Dialog.DIALOG.getResource(), 0, 0, getWidth(), getHeight(), null);

        Font f = titleFont.deriveFont(50 * ResourceManager.getScaling().getX());
        FontMetrics metrics = g.getFontMetrics(f);

        g.setFont(f);
        g.setColor(Color.WHITE);
        g.drawString(title, getWidth() / 2 - metrics.stringWidth(title) / 2, metrics.getHeight());

        MinecraftTextRenderer.drawFormattedString(g, mainText, getWidth() / 20, (int) (metrics.getHeight() * 1.6f), getWidth() - getWidth() / 6, getHeight() - (int) (metrics.getHeight() * 1.6f), mainFont.deriveFont((float) 30 * ResourceManager.getScaling().getX()), index);
    }

    //private void drawCenteredString(Graphics g, String text, )
}
