package de.jp.infoprojekt.gameengine.graphics.fade;

import de.jp.infoprojekt.gameengine.graphics.GameGraphics;
import de.jp.infoprojekt.gameengine.scenes.AbstractScene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

/**
 * BlackFade class
 *
 * @author Pascal
 * @version 19.06.2025
 */
public class BlackFade extends AbstractFade {

    private float alpha = 0f;
    private Timer timer;
    private boolean increment;

    public BlackFade() {
        setOpaque(false);

        //Disable inputs
        addMouseListener(new MouseAdapter() {});
        addMouseMotionListener(new MouseMotionAdapter() {});
        //setFocusTraversalKeysEnabled(false);
    }

    @Override
    public void fade(AbstractScene scene, GameGraphics graphics) {
        graphics.getFrame().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setSize(e.getComponent().getWidth(), e.getComponent().getHeight());
            }
        });
        setSize(graphics.getFrame().getContentPane().getWidth(), graphics.getFrame().getContentPane().getHeight());

        alpha = 0f;
        increment = true;
        graphics.addOverlayLayer(this);

        timer = new Timer(20, e -> {
            alpha += increment ? 0.05f : -0.05f;
            if (alpha >= 1f) {
                alpha = 1f;
                increment = false;
                graphics.switchToScene(scene);
            }
            if (alpha <= 0f) {
                timer.stop();
                graphics.removeLayer(this);
            }
            repaint();
        });

        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (alpha > 0f) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        }
    }

}
