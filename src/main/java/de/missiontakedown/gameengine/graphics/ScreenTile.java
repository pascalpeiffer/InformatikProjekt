package de.missiontakedown.gameengine.graphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class ScreenTile extends JPanel {

    public ScreenTile() {
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            g.drawImage(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/test.jpg")))).getImage(), 0, 0, getWidth(), getHeight(), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("DRAW");
    }
}
