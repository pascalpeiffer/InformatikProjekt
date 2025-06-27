package de.jp.infoprojekt.gameengine;

import de.jp.infoprojekt.gameengine.graphics.GameGraphics;
import de.jp.infoprojekt.gameengine.graphics.popup.GameDialogManager;
import de.jp.infoprojekt.gameengine.state.GameStateManager;
import de.jp.infoprojekt.settings.SettingManager;
import de.jp.infoprojekt.settings.graphics.WindowTypeSetting;
import de.jp.infoprojekt.settings.key.KeyMappingSettings;
import de.jp.infoprojekt.gameengine.tick.GameTickProvider;
import de.jp.infoprojekt.io.key.GameKeyHandler;

import java.awt.event.KeyEvent;

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

    private final KeyMappingSettings keyMappingSettings;

    public GameEngine() {
        keyMappingSettings = (KeyMappingSettings) SettingManager.getInstance().getSetting(KeyMappingSettings.class);
        tickProvider = new GameTickProvider(100); //Ticks per second
        graphics = new GameGraphics(this);
        dialogManager = new GameDialogManager(this);
        gameKeyHandler = new GameKeyHandler(this);
        stateManager = new GameStateManager(this);
    }

    public void start() {
        graphics.start();

        getGameKeyHandler().onKeyDown(KeyEvent.VK_F11, () -> {
            if (graphics.getSettings().getCurrentWindowSetting() == WindowTypeSetting.WINDOWED) {
                graphics.getSettings().setCurrentWindowSetting(WindowTypeSetting.WINDOWED_FULLSCREEN);
                graphics.updateWindowType();
            }else if (graphics.getSettings().getCurrentWindowSetting() == WindowTypeSetting.WINDOWED_FULLSCREEN) {
                graphics.getSettings().setCurrentWindowSetting(WindowTypeSetting.WINDOWED);
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
}
