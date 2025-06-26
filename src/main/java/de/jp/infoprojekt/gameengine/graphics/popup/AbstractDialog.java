package de.jp.infoprojekt.gameengine.graphics.popup;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.resources.ResourceManager;
import de.jp.infoprojekt.resources.ScalingEvent;
import de.jp.infoprojekt.resources.dialog.Dialog;
import de.jp.infoprojekt.util.FontManager;
import de.jp.infoprojekt.util.TextRenderer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDialog extends JComponent implements ScalingEvent {

    private final GameEngine engine;

    private final float widthDivider = 2;
    private final float heightDivider = 4;
    private final float bottomSeparateDivider = 25;

    private boolean continueHintShown = false;
    private final String continueHintText = "Leer für weiter!";

    private String title;
    private String dialog;

    private Font titleFont = FontManager.JERSEY_10;
    private Font mainFont = FontManager.JERSEY_20;

    public AbstractDialog(GameEngine engine) {
        this.engine = engine;
        setFocusable(false);
        ResourceManager.addScalingListener(this);

        setSizeAndLoc();
    }

    public void show(int delay) {
        show(delay, () -> {});
    }

    private int renderIndex = 0;
    private Timer timer;
    public void show(int delay, Runnable callback) {
        resetShow();
        timer = new Timer(delay, e -> {
            if (renderIndex >= dialog.length()) {
                new Thread(callback).start();
                timer.stop();
            }
            renderIndex++;
            repaint();
        });
        timer.start();
    }

    public void resetShow() {
        continueHintShown = false;
        if (timer != null) {
            timer.stop();
            timer = null;
        }
        renderIndex = 0;
    }

    public boolean isFullyShown() {
        return renderIndex >= dialog.length();
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        setSizeAndLoc();
    }

    private void setSizeAndLoc() {
        setSize((int) (ResourceManager.getGameScreenWidth() / widthDivider), (int) (ResourceManager.getGameScreenHeight() / heightDivider));
        setLocation(ResourceManager.getGameScreenWidth() / 2 - getWidth() / 2, (int) ((int) (ResourceManager.getGameScreenHeight() / heightDivider * (heightDivider -  1))));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //TODO debug mode?
        g.setColor(Color.GREEN);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        g.drawImage(Dialog.DIALOG.getResource(), 0, 0, getWidth(), (int) (getHeight() - (ResourceManager.getGameScreenHeight() / bottomSeparateDivider)), null);

        Font f = titleFont.deriveFont(50 * ResourceManager.getScaling().getX());
        FontMetrics metrics = g.getFontMetrics(f);

        g.setFont(f);
        g.setColor(Color.WHITE);
        g.drawString(title, getWidth() / 2 - metrics.stringWidth(title) / 2, metrics.getHeight());

        TextRenderer.drawFormattedString(g, dialog, getWidth() / 20, (int) (metrics.getHeight() * 1.6f), getWidth() - getWidth() / 6, (int) (getHeight() - (int) (metrics.getHeight() * 1.6f) - (ResourceManager.getGameScreenHeight() / bottomSeparateDivider)), mainFont.deriveFont((float) 30 * ResourceManager.getScaling().getX()), renderIndex);

        if (isContinueHintShown()) {
            drawContinueHint(g);
        }
    }

    private void drawContinueHint(Graphics g) {
        int continueHintWidth = getWidth() / 4;
        int continueHintHeight = getHeight() / 5;

        int continueHintBottomDistance = continueHintHeight / 2;

        g.drawImage(Dialog.DIALOG.getResource(), getWidth() / 2 - continueHintWidth / 2, getHeight() - continueHintHeight - continueHintBottomDistance, continueHintWidth, continueHintHeight, null);

        Font continueHintFont = mainFont.deriveFont((float) 30 * ResourceManager.getScaling().getX());
        FontMetrics m = g.getFontMetrics(continueHintFont);

        g.setColor(Color.WHITE);
        g.drawString(continueHintText, getWidth() / 2 - (m.stringWidth(continueHintText) / 2), getHeight() - continueHintHeight - continueHintBottomDistance + (continueHintHeight / 2) + m.getDescent());

        //g.setColor(Color.GREEN);
        //g.drawRect(getWidth() / 2 - (m.stringWidth(continueHintText) / 2), getHeight() - continueHintHeight - continueHintBottomDistance + (continueHintHeight / 2) - (m.getAscent() / 2), m.stringWidth(continueHintText), m.getAscent());
    }

    public void setDialog(String dialog) {
        resetShow();
        this.dialog = dialog;
    }

    public void setContinueHintShown(boolean continueHintShown) {
        this.continueHintShown = continueHintShown;
    }

    public boolean isContinueHintShown() {
        return continueHintShown;
    }

    public String getDialog() {
        return dialog;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMainFont(Font mainFont) {
        this.mainFont = mainFont;
    }

    public void setTitleFont(Font titleFont) {
        this.titleFont = titleFont;
    }

    public Font getMainFont() {
        return mainFont;
    }

    public Font getTitleFont() {
        return titleFont;
    }

    public String getTitle() {
        return title;
    }
}
