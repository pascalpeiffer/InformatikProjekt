package de.jp.infoprojekt.gameengine;

import de.jp.infoprojekt.gameengine.graphics.GameGraphics;
import de.jp.infoprojekt.gameengine.graphics.dialog.GameDialogManager;
import de.jp.infoprojekt.gameengine.inventory.GameInventoryManager;
import de.jp.infoprojekt.gameengine.scenes.farmer.CowMinigameScene;
import de.jp.infoprojekt.gameengine.scenes.farmer.FarmerScene;
import de.jp.infoprojekt.gameengine.scenes.gameovershotdead.GameOverShotDeadScene;
import de.jp.infoprojekt.gameengine.scenes.lab.LabScene;
import de.jp.infoprojekt.gameengine.scenes.lab.WorkbenchScene;
import de.jp.infoprojekt.gameengine.scenes.spawn.SpawnScene;
import de.jp.infoprojekt.gameengine.scenes.travel.TravelScene;
import de.jp.infoprojekt.gameengine.state.GameStateManager;
import de.jp.infoprojekt.settings.SettingManager;
import de.jp.infoprojekt.settings.graphics.WindowTypeSetting;
import de.jp.infoprojekt.settings.key.KeyMappingSettings;
import de.jp.infoprojekt.gameengine.tick.GameTickProvider;
import de.jp.infoprojekt.io.key.GameKeyHandler;

/**
 * GameEngine class
 *
 * @author Pascal
 * @version 14.06.2025
 */
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
        graphics.switchToScene(new WorkbenchScene(this));
        addFullscreenKey();
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
