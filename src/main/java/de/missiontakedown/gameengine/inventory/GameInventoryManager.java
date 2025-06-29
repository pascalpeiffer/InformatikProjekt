package de.missiontakedown.gameengine.inventory;

import de.missiontakedown.gameengine.GameEngine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Pascal
 */
public class GameInventoryManager {

    private final GameEngine engine;

    private ObservableList<Item> items = FXCollections.observableList(new ArrayList<>());

    public GameInventoryManager(GameEngine engine) {
        this.engine = engine;
    }

    public Optional<Item> findItemByType(Item.Type type) {
        return items.stream().filter(item -> item.getType() == type).findAny();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public void clearItems() {
        items.clear();
    }

    public ObservableList<Item> getItems() {
        return items;
    }
}
