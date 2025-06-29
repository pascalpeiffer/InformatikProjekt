package de.missiontakedown.gameengine.gameobjects.overlay;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.gameobjects.AbstractGameObject;
import de.missiontakedown.gameengine.graphics.render.TextRenderer;
import de.missiontakedown.resources.ResourceManager;
import de.missiontakedown.resources.ScalingEvent;
import de.missiontakedown.resources.gameobjects.PlayerResource;
import de.missiontakedown.util.FloatPoint;
import de.missiontakedown.util.FontManager;

import javax.swing.*;
import java.awt.*;

/**
 * @author Pascal
 */
public class MoneyOverlay extends AbstractGameObject implements ScalingEvent {

    private final GameEngine engine;

    private Font font = FontManager.JERSEY_10;

    private String moneyText = "";

    public MoneyOverlay(GameEngine engine) {
        this.engine = engine;
        setDisableLocationFix(true);
        setRelativeLocation(new FloatPoint(0f, 0.01f));
        updateSize();

        getMoneyValue();

        engine.getStateManager().moneyCountProperty().addListener((observable, oldValue, newValue) -> {
            SwingUtilities.invokeLater(this::getMoneyValue);
        });
    }

    public void getMoneyValue() {
        moneyText = engine.getStateManager().moneyCountProperty().get() + " €";
        updateSize();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(PlayerResource.MONEY_ICON.getResource(),0,0, PlayerResource.MONEY_ICON.getWidth(), PlayerResource.MONEY_ICON.getHeight(), null);



        TextRenderer.drawFormattedString(g, moneyText, PlayerResource.MONEY_ICON.getWidth(),getHeight() / 2 + getFontMatrics().getDescent(), 1000,1000, getFont(), Integer.MAX_VALUE);
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        updateSize();
        super.scale(widthMultiply, heightMultiply);
    }

    private void updateSize() {
        setSize(PlayerResource.MONEY_ICON.getWidth() + getFontMatrics().stringWidth(moneyText), PlayerResource.MONEY_ICON.getHeight());
        repaint();
    }

    private FontMetrics getFontMatrics() {
        return getFontMetrics(getFont());
    }

    public void setMoneyText(String moneyText) {
        this.moneyText = moneyText;
        updateSize();
    }

    @Override
    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    public Font getFont() {
        return font.deriveFont((float) 40 * ResourceManager.getScaling().getX());
    }
}
