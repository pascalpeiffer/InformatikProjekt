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

    private final float widthDivider = 2;
    private final float heightDivider = 4;
    private final float bottomSeparateDivider = 25;

    private String title;
    private final List<String> dialogs = new ArrayList<>();
    private int dialogIndex = 0;

    private Font titleFont = FontManager.JERSEY_10;
    private Font mainFont = FontManager.JERSEY_20;


    public AbstractDialog(GameEngine engine) {
        setFocusable(false);
        ResourceManager.addScalingListener(this);

        engine.getGraphics().addDialogLayer(this);

        setSizeAndLoc();
    }

    public void proceed(int index, String title) {
        if (dialogs.size() > index) {
            dialogIndex = index;
        }
        renderIndex = 0;
    }

    public void proceed(String title) {
        proceed(dialogIndex + 1, title);
    }

    public void proceed() {
        proceed(dialogIndex + 1, getTitle());
    }

    public void show(int delay) {
        show(delay, () -> {});
    }

    private int renderIndex = 0;
    private Timer timer;
    public void show(int delay, Runnable callback) {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
        renderIndex = 0;

        timer = new Timer(delay, e -> {
            if (renderIndex >= dialogs.get(dialogIndex).length()) {
                callback.run();
                timer.stop();
            }
            renderIndex++;
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

        //TODO debug mode?
        //g.setColor(Color.GREEN);
        //g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        g.drawImage(Dialog.DIALOG.getResource(), 0, 0, getWidth(), getHeight(), null);

        Font f = titleFont.deriveFont(50 * ResourceManager.getScaling().getX());
        FontMetrics metrics = g.getFontMetrics(f);

        g.setFont(f);
        g.setColor(Color.WHITE);
        g.drawString(title, getWidth() / 2 - metrics.stringWidth(title) / 2, metrics.getHeight());

        TextRenderer.drawFormattedString(g, dialogs.get(dialogIndex), getWidth() / 20, (int) (metrics.getHeight() * 1.6f), getWidth() - getWidth() / 6, getHeight() - (int) (metrics.getHeight() * 1.6f), mainFont.deriveFont((float) 30 * ResourceManager.getScaling().getX()), renderIndex);
    }

    public void addDialog(String dialog) {
        dialogs.add(dialog);
    }

    public List<String> getDialogs() {
        return dialogs;
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
