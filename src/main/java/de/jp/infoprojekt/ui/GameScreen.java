package de.jp.infoprojekt.ui;

import de.jp.infoprojekt.scenes.Scene;
import de.jp.infoprojekt.scenes.TitleScene;

import javax.swing.*;
import java.awt.*;

public class GameScreen {

    private JFrame frame;

    private Scene titleScene = new TitleScene();

    public GameScreen() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.setSize(500, 500 / 16 * 8);
        frame.setBackground(Color.BLACK);

        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        device.setFullScreenWindow(frame);

        frame.setSize(device.getFullScreenWindow().getWidth(), device.getFullScreenWindow().getHeight());
        titleScene.setSize(device.getFullScreenWindow().getWidth(), device.getFullScreenWindow().getHeight());

        frame.setContentPane(titleScene);
    }

    public void show() {
        frame.setVisible(true);

        /*for (int i = 0; i < 5; i++) {
            ScreenTile tile = new ScreenTile();
            tile.setBounds(i * 100, 0, 100, 50);
            testPanel.add(tile);
        }*/
    }

}
