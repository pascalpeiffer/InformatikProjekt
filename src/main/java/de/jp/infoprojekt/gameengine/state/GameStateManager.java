package de.jp.infoprojekt.gameengine.state;

import de.jp.infoprojekt.gameengine.GameEngine;

/**
 * GameStateManager class
 *
 * @author Pascal
 * @version 19.06.2025
 */
public class GameStateManager {

    private final GameEngine engine;

    private GameState currentGameState = GameState.GAME_ENTRY;

    public GameStateManager(GameEngine engine) {
        this.engine = engine;
    }

    public GameState getState() {
        return currentGameState;
    }

    public void setState(GameState currentGameState) {
        System.out.println("New State: " + currentGameState.name());
        this.currentGameState = currentGameState;
    }
}
