package de.jp.infoprojekt.scenes;

import java.awt.*;

public class TitleScene extends Scene {

    public TitleScene() {
        setBackground(Color.BLACK);
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.RED);
        g.drawLine(0, 0, (int) g.getClipBounds().getWidth(), (int) g.getClipBounds().getHeight());
    }
}
