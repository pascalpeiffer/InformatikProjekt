package de.jp.infoprojekt.gameengine.scenes;

import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractScene extends JPanel {

    private final List<Rectangle2D> playerBlockingArea = new ArrayList<>();

    public List<Rectangle2D> getPlayerBlockingArea() {
        return playerBlockingArea;
    }

    public void addBlockingArea(Rectangle2D rect) {
        playerBlockingArea.add(rect);
    }

    public void removeBlockingArea(Rectangle2D rect) {
        playerBlockingArea.remove(rect);
    }
}
