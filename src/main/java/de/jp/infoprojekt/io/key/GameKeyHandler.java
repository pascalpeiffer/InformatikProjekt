package de.jp.infoprojekt.io.key;

import de.jp.infoprojekt.gameengine.GameEngine;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * GameKeyHandler class
 *
 * @author Pascal
 * @version 25.06.2025
 */
public class GameKeyHandler {

    private GameEngine gameEngine;

    private final ObservableList<Integer> keysDown = FXCollections.observableList(new ArrayList<>());

    public GameKeyHandler(GameEngine gameEngine) {
        this.gameEngine = gameEngine;

        gameEngine.getGraphics().getFrame().addKeyListener(new GameKeyHandlerImpl());
    }

    public boolean isKeyDown(int keyCode) {
        return keysDown.contains(keyCode);
    }

    public void onKeyDown(int keyCode, Runnable runnable) {
        keysDown.addListener((ListChangeListener<? super Integer>) c -> {
            if (keysDown.contains(keyCode)) {
                runnable.run();
            }
        });
    }

    private class GameKeyHandlerImpl implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
            System.out.println("Typed");
        }

        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println("Pressed");
            if (!keysDown.contains(e.getKeyCode())) {
                keysDown.add(e.getKeyCode());
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            System.out.println("Released");
            keysDown.remove((Object) e.getKeyCode());
        }
    }
}
