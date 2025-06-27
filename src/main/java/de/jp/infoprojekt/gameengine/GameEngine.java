package de.jp.infoprojekt.gameengine;

import de.jp.infoprojekt.gameengine.graphics.GameGraphics;
import de.jp.infoprojekt.gameengine.graphics.dialog.GameDialogManager;
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

    private GameGraphics graphics;
    private GameStateManager stateManager;
    private GameKeyHandler gameKeyHandler;
    private GameTickProvider tickProvider;
    private GameDialogManager dialogManager;

    private final KeyMappingSettings keyMappingSettings;


    public GameEngine() {
        keyMappingSettings = (KeyMappingSettings) SettingManager.getInstance().getSetting(KeyMappingSettings.class);
        tickProvider = new GameTickProvider(ticksPerSecond);
        graphics = new GameGraphics(this);
        dialogManager = new GameDialogManager(this);
        gameKeyHandler = new GameKeyHandler(this);
        stateManager = new GameStateManager(this);
    }

    public void start() {
        graphics.start();
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
