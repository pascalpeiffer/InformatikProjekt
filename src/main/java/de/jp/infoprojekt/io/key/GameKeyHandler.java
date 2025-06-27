package de.jp.infoprojekt.io.key;

import de.jp.infoprojekt.gameengine.GameEngine;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * GameKeyHandler class
 *
 * @author Pascal
 * @version 25.06.2025
 */
public class GameKeyHandler {

    private GameEngine gameEngine;

    private final ObservableList<Integer> keysDown = FXCollections.observableList(new ArrayList<>());

    private boolean inputEnabled = true;

    public GameKeyHandler(GameEngine gameEngine) {
        this.gameEngine = gameEngine;

        gameEngine.getGraphics().getFrame().addKeyListener(new GameKeyHandlerImpl());

        gameEngine.getGraphics().getFrame().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                keysDown.clear();
            }
        });
    }

    public void onKeyDown(int keyCode, Runnable run) {
        keysDown.addListener((ListChangeListener<? super Integer>) c -> {
            if (keysDown.contains(keyCode)) {
                if (!inputEnabled) {
                    return;
                }
                run.run();
            }
        });
    }

    public boolean isKeyDown(int keyCode) {
        return inputEnabled && keysDown.contains(keyCode);
    }

    public void setInputEnabled(boolean inputEnabled) {
        this.inputEnabled = inputEnabled;
    }

    public boolean isInputEnabled() {
        return inputEnabled;
    }

    private class GameKeyHandlerImpl implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (!keysDown.contains(e.getKeyCode())) {
                keysDown.add(e.getKeyCode());
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keysDown.remove((Object) e.getKeyCode());
        }
    }
}
