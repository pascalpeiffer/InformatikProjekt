package de.jp.infoprojekt.gameengine.gameobjects.interaction;

import de.jp.infoprojekt.gameengine.gameobjects.AbstractGameObject;
import de.jp.infoprojekt.resources.ResourceManager;
import de.jp.infoprojekt.resources.ScalingEvent;
import de.jp.infoprojekt.resources.interaction.InteractionResource;

import java.awt.*;

public class MissionTakeDownFile extends AbstractGameObject implements ScalingEvent {

    public MissionTakeDownFile() {
        setSize(InteractionResource.MISSION_TAKEDOWN_FILE.getWidth(), InteractionResource.MISSION_TAKEDOWN_FILE.getHeight());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(InteractionResource.MISSION_TAKEDOWN_FILE.getResource(), 0, 0, InteractionResource.MISSION_TAKEDOWN_FILE.getWidth(), InteractionResource.MISSION_TAKEDOWN_FILE.getHeight(), null);
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        setSize(InteractionResource.MISSION_TAKEDOWN_FILE.getWidth(), InteractionResource.MISSION_TAKEDOWN_FILE.getHeight());
        super.scale(widthMultiply, heightMultiply);
    }
}
