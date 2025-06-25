package de.jp.infoprojekt.gameengine;

import de.jp.infoprojekt.gameengine.graphics.GameGraphics;
import de.jp.infoprojekt.gameengine.state.GameStateManager;
import de.jp.infoprojekt.gameengine.util.GameTickProvider;
import de.jp.infoprojekt.io.key.GameKeyHandler;

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
    private GameKeyHandler gameKeyHandler;
    private GameTickProvider tickProvider;

    public GameEngine() {
        tickProvider = new GameTickProvider(100); //Ticks per second
        graphics = new GameGraphics(this);
        gameKeyHandler = new GameKeyHandler(this);
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

    public GameKeyHandler getGameKeyHandler() {
        return gameKeyHandler;
    }

    public GameTickProvider getTickProvider() {
        return tickProvider;
    }
}
