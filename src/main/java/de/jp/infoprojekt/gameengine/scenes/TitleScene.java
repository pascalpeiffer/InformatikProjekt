package de.jp.infoprojekt.gameengine.scenes;

import de.jp.infoprojekt.util.ResourceManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TitleScene extends AbstractScene {

    public JButton exitButton = new JButton("Exit");

    public TitleScene() {
        setBackground(Color.BLACK);
        setLayout(null);

        exitButton.setBackground(Color.WHITE);
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);

        exitButton.setBorder(new LineBorder(Color.WHITE));

        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        //exitButton.setBounds(getWidth() / 2 -100, getHeight() - 100, 100, 20);
        add(exitButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        exitButton.setBounds(getWidth() / 2 - 100, getHeight() - 150, 200, 50);

        g.drawImage(ResourceManager.TITLE_SCENE_BACKGROUND, 0, 0, getWidth(), getHeight(), null);


        //g.setColor(Color.RED);
        //g.drawLine(0, 0, (int) g.getClipBounds().getWidth(), (int) g.getClipBounds().getHeight());
    }
}
