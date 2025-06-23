package de.jp.infoprojekt.gameengine;

import de.jp.infoprojekt.gameengine.graphics.GameGraphics;
import de.jp.infoprojekt.gameengine.state.GameStateManager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * GameEngine class
 *
 * @author Pascal
 * @version 14.06.2025
 */
public class GameEngine {

    private GameGraphics graphics;
    private GameStateManager stateManager;

    public GameEngine() {
        graphics = new GameGraphics(this);
        stateManager = new GameStateManager(this);
    }

    public void start() {
        graphics.start();

        /*graphics.getFrame().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("Key Released: " + e.getKeyCode());
                super.keyReleased(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Key Pressed: " + e.getKeyCode());
                super.keyPressed(e);
            }

            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println("Key Typed: " + e.getKeyCode());
                super.keyTyped(e);
            }
        });*/
    }

    public GameGraphics getGraphics() {
        return graphics;
    }

    public GameStateManager getStateManager() {
        return stateManager;
    }

}
