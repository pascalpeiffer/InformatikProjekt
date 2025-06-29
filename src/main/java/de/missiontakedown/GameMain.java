package de.missiontakedown;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.settings.SettingManager;
import de.missiontakedown.util.FontManager;

import java.awt.*;

public class GameMain {

    private static GameEngine gameEngine;

    public static void main(String[] args) {

        //Check Headless
        if (GraphicsEnvironment.isHeadless()) {
            //Running on non-gui systems
            System.err.println("This Game is not supported on NON-UI platforms!");
            System.exit(-1);
        }

        try {
            FontManager.registerFonts();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Load Settings
        if (!SettingManager.getInstance().loadSettingsFromFile()) {
            System.err.println("Failed to load Settings");
            System.exit(-1);
        }

        //Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> SettingManager.getInstance().saveSettingsToFile()));

        //Start the game
        gameEngine = new GameEngine();
        gameEngine.start();
    }

}
