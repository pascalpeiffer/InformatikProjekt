package de.jp.infoprojekt;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.settings.SettingManager;
import de.jp.infoprojekt.resources.ResourceManager;

import java.awt.*;
import java.io.IOException;

public class GameMain {

    private static GameEngine gameEngine;

    public static void main(String[] args) {

        //Check Headless
        if (GraphicsEnvironment.isHeadless()) {
            //Running on non-gui systems
            System.err.println("This Game is not supported on NON-UI platforms!");
            System.exit(-1);
        }

        //Load Settings
        if (!SettingManager.getInstance().loadSettingsFromFile()) {
            System.err.println("Failed to load Settings");
            System.exit(-1);
        }

        //Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> SettingManager.getInstance().saveSettingsToFile()));

        //Load image resources
        try {
            ResourceManager.loadImageResources();
        } catch (IOException ignored) {
            System.err.println("Failed to load image resources!");
            System.exit(-1);
        }

        //Start the game
        gameEngine = new GameEngine();
        gameEngine.start();
    }

}
