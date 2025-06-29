package de.missiontakedown.gameengine.graphics.dialog;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.tick.GameTick;
import de.missiontakedown.resources.ResourceManager;
import de.missiontakedown.resources.ScalingEvent;
import de.missiontakedown.resources.dialog.DialogResource;
import de.missiontakedown.util.FontManager;
import de.missiontakedown.gameengine.graphics.render.TextRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class AbstractDialog extends JComponent implements ScalingEvent, GameTick {

    private final GameEngine engine;

    private final float widthDivider = 2;
    private final float heightDivider = 4;
    private final float bottomSeparateDivider = 25;

    private boolean continueHintShown = false;
    private String continueHintText = "␣";

    private boolean drawBaseImage = true;

    private boolean optionsShown = false;
    private String optionAText = "";
    private String optionBText = "";
    private String optionAButtonText = "";
    private String optionBButtonText = "";

    private String title = "";
    private String dialog = "";

    private Font titleFont = FontManager.JERSEY_10;
    private Font mainFont = FontManager.JERSEY_20;

    private int typeDelay = 40;

    private boolean continueDialogType = true;
    private ContinueCallback continueCallback;
    private OptionCallback optionCallback;

    private int answerCooldown = 0;

    private boolean visible = true;

    public AbstractDialog(GameEngine engine) {
        this.engine = engine;
        setFocusable(false);
        ResourceManager.addScalingListener(this);

        setSizeAndLoc();
    }

    public void continueDialog(String title, String text, ContinueCallback callback) {
        setTitle(title);
        setDialog(text);
        show();
        continueCallback = callback;
        continueDialogType = true;
    }

    public void optionsDialog(String title, String optionA, String optionB, OptionCallback callback) {
        setTitle(title);
        setDialog("");
        setOptionAText(optionA);
        setOptionBText(optionB);

        setOptionAButtonText(KeyEvent.getKeyText(engine.getKeyMappingSettings().LEFT_KEY));
        setOptionBButtonText(KeyEvent.getKeyText(engine.getKeyMappingSettings().RIGHT_KEY));

        show();

        optionCallback = callback;
        continueDialogType = false;
    }

    public void show() {
        show(() -> {});
    }

    private int renderIndex = 0;
    private Timer timer;
    public void show(Runnable callback) {
        resetShow();
        timer = new Timer(typeDelay, e -> {
            if (engine.getDialogManager().getCurrentDialog() != this) {
                timer.stop();
            }

            if (renderIndex >= dialog.length()) {
                new Thread(callback).start();
                timer.stop();
            }
            renderIndex++;
            DialogResource.TYPING_SOUND.create().play();
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

    public void onDialogShow() {}
    public void onDialogHide() {}

    @Override
    public void tick(long currentTick) {
        if (isFullyShown()) {
            if (isContinueDialogType()) {
                if (answerCooldown > 0) {
                    answerCooldown--;
                    continueHintShown = false;
                    return;
                }
                continueHintShown = true;
                repaint();
                if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().DIALOG_CONTINUE)) {
                    ContinueCallback c = continueCallback;
                    continueCallback = () -> {};
                    c.callback();
                }
            }else {
                optionsShown = true;
                if (answerCooldown > 0) {
                    answerCooldown--;
                    return;
                }
                if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().LEFT_KEY)) {
                    OptionCallback c = optionCallback;
                    optionCallback = (a) -> {};
                    c.callback(true);
                }else if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().RIGHT_KEY)) {
                    OptionCallback c = optionCallback;
                    optionCallback = (a) -> {};
                    c.callback(false);
                }
            }
        }else {
            continueHintShown = false;
            optionsShown = false;
        }
    }

    public void dispose() {
        SwingUtilities.invokeLater(() -> {
            engine.getDialogManager().unsetDialog();
        });
    }

    public void setAnswerCooldown(int answerCooldown) {
        this.answerCooldown = answerCooldown;
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

        if (!visible) {
            return;
        }

        if (drawBaseImage) {
            g.drawImage(DialogResource.DIALOG.getResource(), 0, 0, getWidth(), (int) (getHeight() - (ResourceManager.getGameScreenHeight() / bottomSeparateDivider)), null);
        }

        //Draw Title
        Font f = titleFont.deriveFont(50 * ResourceManager.getScaling().getX());
        FontMetrics metrics = g.getFontMetrics(f);

        g.setFont(f);
        g.setColor(Color.WHITE);
        g.drawString(title, getWidth() / 2 - metrics.stringWidth(title) / 2, metrics.getHeight());

        if (!isOptionsShown()) {
            drawMainText(g, metrics);
        }

        if (isContinueHintShown() && !isOptionsShown()) {
            drawContinueHint(g);
        }

        if (isOptionsShown()) {
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
        g.drawImage(DialogResource.DIALOG.getResource(), getWidth() / 4 - answerWidth / 2, getHeight() - answerHeight - answerHeightBottomDistance, answerWidth, answerHeight, null);
        TextRenderer.drawFormattedString(g, getOptionAText(), getWidth() / 4 - optionsWidth / 2, getHeight() - optionsHeight - optionsHeightBottomDistance, optionsWidth, optionsHeight,mainFont.deriveFont((float) 25 * ResourceManager.getScaling().getX()), Integer.MAX_VALUE);


        //Option B
        g.drawImage(DialogResource.DIALOG.getResource(), getWidth() / 4 * 3 - answerWidth / 2, getHeight() - answerHeight - answerHeightBottomDistance, answerWidth, answerHeight, null);
        TextRenderer.drawFormattedString(g, getOptionBText(), getWidth() / 4 * 3 - optionsWidth / 2 , getHeight() - optionsHeight - optionsHeightBottomDistance, optionsWidth, optionsHeight,mainFont.deriveFont((float) 25 * ResourceManager.getScaling().getX()), Integer.MAX_VALUE);

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

        g.drawImage(DialogResource.DIALOG.getResource(), getWidth() / 2 - continueHintWidth / 2, getHeight() - continueHintHeight - continueHintBottomDistance, continueHintWidth, continueHintHeight, null);

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

    public boolean isContinueDialogType() {
        return continueDialogType;
    }

    public boolean isOptionsShown() {
        return optionsShown;
    }

    public void setOptionsShown(boolean optionsShown) {
        this.optionsShown = optionsShown;
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

    public void setDrawBaseImage(boolean drawBaseImage) {
        this.drawBaseImage = drawBaseImage;
    }

    public int getTypeDelay() {
        return typeDelay;
    }

    public void setTypeDelay(int typeDelay) {
        this.typeDelay = typeDelay;
    }

    public int getAnswerCooldown() {
        return answerCooldown;
    }

    public void setV(boolean visible) {
        this.visible = visible;
    }

    public boolean isV() {
        return visible;
    }
}
