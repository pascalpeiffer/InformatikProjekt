package de.jp.infoprojekt.gameengine.state;

import de.jp.infoprojekt.gameengine.GameEngine;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * GameStateManager class
 *
 * @author Pascal
 * @version 19.06.2025
 */
public class GameStateManager {

    private final GameEngine engine;

    private GameState currentGameState = GameState.GAME_ENTRY;

    private final SimpleIntegerProperty moneyCount = new SimpleIntegerProperty(200);

    public GameStateManager(GameEngine engine) {
        this.engine = engine;
    }

    public GameState getState() {
        return currentGameState;
    }

    public void setState(GameState currentGameState) {
        //TODO remove dev
        System.out.println("New State: " + currentGameState.name());
        this.currentGameState = currentGameState;
    }

    public int getMoneyCount() {
        return moneyCount.get();
    }

    public void setMoneyCount(int moneyCount) {
        this.moneyCount.set(moneyCount);
    }

    public SimpleIntegerProperty moneyCountProperty() {
        return moneyCount;
    }
}
