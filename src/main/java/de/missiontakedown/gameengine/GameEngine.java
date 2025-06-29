package de.missiontakedown.gameengine;

import de.missiontakedown.gameengine.graphics.GameGraphics;
import de.missiontakedown.gameengine.graphics.dialog.GameDialogManager;
import de.missiontakedown.gameengine.graphics.fade.AbstractFade;
import de.missiontakedown.gameengine.graphics.fade.BlackFade;
import de.missiontakedown.gameengine.inventory.GameInventoryManager;
import de.missiontakedown.gameengine.inventory.Item;
import de.missiontakedown.gameengine.scenes.lab.LabScene;
import de.missiontakedown.gameengine.scenes.main.TitleScene;
import de.missiontakedown.gameengine.scenes.util.ColorScene;
import de.missiontakedown.gameengine.state.GameState;
import de.missiontakedown.gameengine.state.GameStateManager;
import de.missiontakedown.gameengine.state.QuestState;
import de.missiontakedown.settings.SettingManager;
import de.missiontakedown.settings.graphics.WindowTypeSetting;
import de.missiontakedown.settings.key.KeyMappingSettings;
import de.missiontakedown.gameengine.tick.GameTickProvider;
import de.missiontakedown.io.key.GameKeyHandler;

import java.awt.*;

public class GameEngine {

    private static final int ticksPerSecond = 100;

    private final GameGraphics graphics;
    private final GameStateManager stateManager;
    private final GameKeyHandler gameKeyHandler;
    private final GameTickProvider tickProvider;
    private final GameDialogManager dialogManager;
    private final GameInventoryManager inventoryManager;

    private final KeyMappingSettings keyMappingSettings;


    public GameEngine() {
        keyMappingSettings = (KeyMappingSettings) SettingManager.getInstance().getSetting(KeyMappingSettings.class);
        tickProvider = new GameTickProvider(ticksPerSecond);
        graphics = new GameGraphics(this);
        dialogManager = new GameDialogManager(this);
        gameKeyHandler = new GameKeyHandler(this);
        stateManager = new GameStateManager(this);
        inventoryManager = new GameInventoryManager(this);
    }

    public void start() {
        graphics.start();
        addFullscreenKey();
        graphics.switchToScene(new ColorScene(Color.BLACK));
        graphics.switchToScene(new TitleScene(this), new BlackFade(this, 300));
    }

    private void addFullscreenKey() {
        getGameKeyHandler().onKeyDown(getKeyMappingSettings().FULLSCREEN, () -> {
            if (graphics.getSettings().getCurrentWindowSetting() == WindowTypeSetting.WINDOWED) {
                graphics.getSettings().setCurrentWindowSetting(WindowTypeSetting.WINDOWED_FULLSCREEN);
                graphics.updateWindowType();
            }else if (graphics.getSettings().getCurrentWindowSetting() == WindowTypeSetting.WINDOWED_FULLSCREEN) {
                graphics.getSettings().setCurrentWindowSetting(WindowTypeSetting.WINDOWED);
                graphics.updateWindowType();
            }else if (graphics.getSettings().getCurrentWindowSetting() == WindowTypeSetting.FULLSCREEN) {
                graphics.getSettings().setCurrentWindowSetting(WindowTypeSetting.WINDOWED_FULLSCREEN);
                graphics.updateWindowType();
            }
        });
    }

    public void rollbackToNitratorScene(AbstractFade fade) {
        getStateManager().setState(GameState.GET_NITRATOR);
        getStateManager().setQuest(QuestState.GET_NITRATOR);

        //Revert Inv
        getInventoryManager().clearItems();
        //TODO check items
        getInventoryManager().addItem(new Item(Item.Type.Hydrogen));
        getInventoryManager().addItem(new Item(Item.Type.NitricAcid));
        getInventoryManager().addItem(new Item(Item.Type.SulfuricAcid));
        getInventoryManager().addItem(new Item(Item.Type.ColaEmpty));
        getInventoryManager().addItem(new Item(Item.Type.Glycerin));

        if (fade == null) {
            getGraphics().switchToScene(new LabScene(this));
        }else {
            getGraphics().switchToScene(new LabScene(this), fade);
        }
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

    public KeyMappingSettings getKeyMappingSettings() {
        return keyMappingSettings;
    }

    public GameInventoryManager getInventoryManager() {
        return inventoryManager;
    }
}
