package de.jp.infoprojekt.gameengine.gameobjects.computer;

import de.jp.infoprojekt.gameengine.gameobjects.AbstractGameObject;
import de.jp.infoprojekt.resources.GameResource;
import de.jp.infoprojekt.resources.ResourceManager;
import de.jp.infoprojekt.resources.ScalingEvent;
import de.jp.infoprojekt.resources.interaction.InteractionResource;
import de.jp.infoprojekt.resources.scenes.ComputerSceneResource;

import java.awt.*;

public class AmazonIcon extends AbstractGameObject implements ScalingEvent {

    private final GameResource background = ComputerSceneResource.AMAZON;

    public AmazonIcon() {
        update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background.getResource(), 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        update();
        super.scale(widthMultiply, heightMultiply);
    }

    private void update() {
        setSize(background.getWidth(), background.getHeight());
        repaint();
    }
}
