package de.jp.infoprojekt.gameengine.state;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.resources.quest.QuestResource;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * GameStateManager class
 *
 * @author Pascal
 * @version 19.06.2025
 */
public class GameStateManager {

    private final GameEngine engine;

    private GameState currentGameState = GameState.BUY_SULFURIC_ACID;

    private final ObjectProperty<QuestState> currentQuest = new SimpleObjectProperty<>(QuestState.NO_QUEST);

    private final SimpleIntegerProperty moneyCount = new SimpleIntegerProperty();

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

    public void setQuest(QuestState quest) {
        if (quest != QuestState.NO_QUEST) {
            QuestResource.NEW_QUEST.create().play();
        }
        currentQuest.set(quest);
    }

    public void reset() {
        setState(GameState.GAME_ENTRY);
        setQuest(QuestState.NO_QUEST);
        engine.getInventoryManager().clearItems();
        setMoneyCount(200);
    }

    public QuestState getQuest() {
        return currentQuest.get();
    }

    public ObjectProperty<QuestState> currentQuestProperty() {
        return currentQuest;
    }

    public int getMoneyCount() {
        return moneyCount.get();
    }

    public void setMoneyCount(int moneyCount) {
        this.moneyCount.set(moneyCount);
    }

    public void addMoney(int moneyCount) {
        this.moneyCount.set(this.moneyCount.get() + moneyCount);
    }

    public void removeMoney(int moneyCount) {
        this.moneyCount.set(this.moneyCount.get() - moneyCount);
    }

    public SimpleIntegerProperty moneyCountProperty() {
        return moneyCount;
    }
}
