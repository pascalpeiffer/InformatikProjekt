package de.jp.infoprojekt.gameengine.inventory;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.resources.item.ItemResource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Optional;

public class GameInventoryManager {

    private final GameEngine engine;

    private ObservableList<Item> items = FXCollections.observableList(new ArrayList<>());

    public GameInventoryManager(GameEngine engine) {
        this.engine = engine;

//        addItem(new Item(Item.Type.Amoniak));
//        addItem(new Item(Item.Type.Electrolysis));
//        addItem(new Item(Item.Type.FogFluid));
//        addItem(new Item(Item.Type.Glycerin));
//        addItem(new Item(Item.Type.Oxygen));
//        addItem(new Item(Item.Type.Hydrogen));
//        addItem(new Item(Item.Type.NitricAcid));
//        addItem(new Item(Item.Type.Nitrator));
//        addItem(new Item(Item.Type.AcidGen));
//        addItem(new Item(Item.Type.ColaBomb));
//        addItem(new Item(Item.Type.ColaEmpty));
//        addItem(new Item(Item.Type.SulfuricAcid));
//        addItem(new Item(Item.Type.Destillation));
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

    public ObservableList<Item> getItems() {
        return items;
    }
}
