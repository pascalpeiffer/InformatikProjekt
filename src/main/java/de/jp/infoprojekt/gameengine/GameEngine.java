package de.jp.infoprojekt.gameengine;

import de.jp.infoprojekt.gameengine.graphics.GameGraphics;
import de.jp.infoprojekt.gameengine.graphics.popup.GameDialogManager;
import de.jp.infoprojekt.gameengine.state.GameStateManager;
import de.jp.infoprojekt.util.GameTickProvider;
import de.jp.infoprojekt.io.key.GameKeyHandler;

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
    private GameDialogManager dialogManager;

    public GameEngine() {
        tickProvider = new GameTickProvider(100); //Ticks per second
        graphics = new GameGraphics(this);
        dialogManager = new GameDialogManager(this);
        gameKeyHandler = new GameKeyHandler(this);
        stateManager = new GameStateManager(this);
    }

    public void start() {
        graphics.start();
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

    public GameDialogManager getDialogManager() {
        return dialogManager;
    }
}
