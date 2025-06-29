package de.missiontakedown.gameengine.gameobjects.overlay;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.gameobjects.AbstractGameObject;
import de.missiontakedown.gameengine.inventory.Item;
import de.missiontakedown.resources.GameResource;
import de.missiontakedown.resources.ResourceManager;
import de.missiontakedown.resources.ScalingEvent;
import de.missiontakedown.resources.gameobjects.PlayerResource;
import de.missiontakedown.util.FloatPoint;
import de.missiontakedown.util.FontManager;
import javafx.collections.ListChangeListener;

import java.awt.*;

/**
 * @author Pascal
 */
public class InventoryOverlay extends AbstractGameObject implements ScalingEvent {

    private final GameEngine engine;

    private Font font = FontManager.JERSEY_10;

    private final GameResource inventoryFrame = PlayerResource.INVENTORY_FRAME;

    public InventoryOverlay(GameEngine engine) {
        this.engine = engine;
        setDisableLocationFix(true);
        setRelativeLocation(new FloatPoint(0.01f, 0.1f));
        updateSize();

        engine.getInventoryManager().getItems().addListener((ListChangeListener<? super Item>) c -> {
            updateSize();
            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        for (int i = 0; i < engine.getInventoryManager().getItems().size(); i++) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            g2d.drawImage(inventoryFrame.getResource(), inventoryFrame.getWidth() * i, 0, inventoryFrame.getWidth(), inventoryFrame.getHeight(), null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            GameResource resource = engine.getInventoryManager().getItems().get(i).getResource();
            float difX = 8 * ResourceManager.getScaling().getX();
            float difY = 8 * ResourceManager.getScaling().getY();
            g2d.drawImage(resource.getResource(), (int) (inventoryFrame.getWidth() * i + (difX / 2)), (int) difY / 3 * 2, (int) (inventoryFrame.getWidth() - difX), (int) (inventoryFrame.getHeight() - difY), null);
        }

        g2d.dispose();
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        updateSize();
        super.scale(widthMultiply, heightMultiply);
    }

    private void updateSize() {
        setSize(engine.getInventoryManager().getItems().size() * inventoryFrame.getWidth(), inventoryFrame.getHeight());
        repaint();
    }

    private FontMetrics getFontMatrics() {
        return getFontMetrics(getFont());
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
