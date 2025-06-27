package de.jp.infoprojekt.gameengine.graphics.popup;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.resources.ResourceManager;
import de.jp.infoprojekt.resources.ScalingEvent;
import de.jp.infoprojekt.resources.dialog.Dialog;
import de.jp.infoprojekt.util.FontManager;
import de.jp.infoprojekt.gameengine.graphics.render.TextRenderer;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractDialog extends JComponent implements ScalingEvent {

    private final GameEngine engine;

    private final float widthDivider = 2;
    private final float heightDivider = 4;
    private final float bottomSeparateDivider = 25;

    private boolean continueHintShown = false;
    private String continueHintText = "␣";

    private boolean showOptions = false;
    private String optionAText = "";
    private String optionBText = "";
    private String optionAButtonText = "";
    private String optionBButtonText = "";

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
            Dialog.TYPING_SOUND.play(0.6f);
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
        setLocation(ResourceManager.getGameScreenWidth() / 2 - getWidth() / 2, (int) (ResourceManager.getGameScreenHeight() / heightDivider * (heightDivider -  1)));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //TODO debug mode?
        //g.setColor(Color.GREEN);
        //g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        g.drawImage(Dialog.DIALOG.getResource(), 0, 0, getWidth(), (int) (getHeight() - (ResourceManager.getGameScreenHeight() / bottomSeparateDivider)), null);

        //Draw Title
        Font f = titleFont.deriveFont(50 * ResourceManager.getScaling().getX());
        FontMetrics metrics = g.getFontMetrics(f);

        g.setFont(f);
        g.setColor(Color.WHITE);
        g.drawString(title, getWidth() / 2 - metrics.stringWidth(title) / 2, metrics.getHeight());

        if (!isShowOptions()) {
            drawMainText(g, metrics);
        }

        if (isContinueHintShown() && !isShowOptions()) {
            drawContinueHint(g);
        }

        if (isShowOptions()) {
            drawOptions(g);
        }
    }

    private void drawOptions(Graphics g) {
        int optionsWidth = getWidth() / 3;
        int optionsHeight = (int) (getHeight() / 2.2f);
        int optionsHeightBottomDistance = (int) (optionsHeight / 2.5f);

        int answerWidth = getWidth() / 4;
        int answerHeight = getHeight() / 5;
        int answerHeightBottomDistance = answerHeight / 2;

        //Option A
        g.drawImage(Dialog.DIALOG.getResource(), getWidth() / 4 - answerWidth / 2, getHeight() - answerHeight - answerHeightBottomDistance, answerWidth, answerHeight, null);
        TextRenderer.drawFormattedString(g, getOptionAText(), getWidth() / 4 - optionsWidth / 2, getHeight() - optionsHeight - optionsHeightBottomDistance, optionsWidth, optionsHeight,mainFont.deriveFont((float) 25 * ResourceManager.getScaling().getX()), Integer.MAX_VALUE);

        //Option B
        g.drawImage(Dialog.DIALOG.getResource(), getWidth() / 4 * 3 - answerWidth / 2, getHeight() - answerHeight - answerHeightBottomDistance, answerWidth, answerHeight, null);
        TextRenderer.drawFormattedString(g, getOptionBText(), getWidth() / 4 * 3 - optionsWidth / 2, getHeight() - optionsHeight - optionsHeightBottomDistance, optionsWidth, optionsHeight,mainFont.deriveFont((float) 25 * ResourceManager.getScaling().getX()), Integer.MAX_VALUE);

        g.setColor(Color.WHITE);
        g.setFont(mainFont.deriveFont((float) 25 * ResourceManager.getScaling().getX()));
        FontMetrics metrics = g.getFontMetrics(g.getFont());

        //g.setColor(Color.GREEN);
        //g.drawRect((int) ((float) getWidth() / 4), getHeight() - answerHeight / 2 - answerHeightBottomDistance + metrics.getDescent(), 100, 100);
        g.drawString(optionAButtonText, (int) ((float) getWidth() / 4) - (metrics.stringWidth(optionAButtonText) / 2), getHeight() - answerHeight / 2 - answerHeightBottomDistance + metrics.getDescent());
        g.drawString(optionBButtonText, (int) ((float) getWidth() / 4 * 3) - (metrics.stringWidth(optionBButtonText) / 2), getHeight() - answerHeight / 2 - answerHeightBottomDistance + metrics.getDescent());
    }

    private void drawMainText(Graphics g, FontMetrics titleFontMetrics) {
        TextRenderer.drawFormattedString(g, dialog, getWidth() / 20, (int) (titleFontMetrics.getHeight() * 1.6f), getWidth() - getWidth() / 8, (int) (getHeight() - (int) (titleFontMetrics.getHeight() * 1.6f) - (ResourceManager.getGameScreenHeight() / bottomSeparateDivider)), mainFont.deriveFont((float) 30 * ResourceManager.getScaling().getX()), renderIndex);
    }

    private void drawContinueHint(Graphics g) {
        int continueHintWidth = getWidth() / 4;
        int continueHintHeight = getHeight() / 5;

        int continueHintBottomDistance = continueHintHeight / 2;

        g.drawImage(Dialog.DIALOG.getResource(), getWidth() / 2 - continueHintWidth / 2, getHeight() - continueHintHeight - continueHintBottomDistance, continueHintWidth, continueHintHeight, null);

        //Font continueHintFont = mainFont.deriveFont((float) 30 * ResourceManager.getScaling().getX());
        //g.setFont(continueHintFont);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Serif",Font.PLAIN, (int) (30 * ResourceManager.getScaling().getX())));
        FontMetrics m = g.getFontMetrics(g.getFont());
        g.drawString(continueHintText, getWidth() / 2 - (m.stringWidth(continueHintText) / 2), getHeight() - continueHintHeight - continueHintBottomDistance + (continueHintHeight / 2) + m.getDescent());

        //g.setColor(Color.GREEN);
        //g.drawRect(getWidth() / 2 - (m.stringWidth(continueHintText) / 2), getHeight() - continueHintHeight - continueHintBottomDistance + (continueHintHeight / 2) - (m.getAscent() / 2), m.stringWidth(continueHintText), m.getAscent());
    }

    public void setDialog(String dialog) {
        resetShow();
        this.dialog = dialog;
    }

    public boolean isShowOptions() {
        return showOptions;
    }

    public void setShowOptions(boolean showOptions) {
        this.showOptions = showOptions;
        repaint();
    }

    public void setOptionAText(String optionAText) {
        this.optionAText = optionAText;
    }

    public void setOptionBText(String optionBText) {
        this.optionBText = optionBText;
    }

    public String getOptionAText() {
        return optionAText;
    }

    public String getOptionBText() {
        return optionBText;
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

    public void setContinueHintText(String continueHintText) {
        this.continueHintText = continueHintText;
    }

    public String getContinueHintText() {
        return continueHintText;
    }

    public void setOptionAButtonText(String optionAButtonText) {
        this.optionAButtonText = optionAButtonText;
    }

    public void setOptionBButtonText(String optionBButtonText) {
        this.optionBButtonText = optionBButtonText;
    }

    public String getOptionAButtonText() {
        return optionAButtonText;
    }

    public String getOptionBButtonText() {
        return optionBButtonText;
    }
}
