package de.missiontakedown.gameengine.graphics;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.graphics.fade.AbstractFade;
import de.missiontakedown.gameengine.scenes.AbstractScene;
import de.missiontakedown.settings.SettingManager;
import de.missiontakedown.settings.graphics.GraphicSettings;
import de.missiontakedown.resources.ResourceManager;
import de.missiontakedown.util.FloatPair;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pascal
 */
public class GameGraphics {

    private final GameEngine gameEngine;

    private final JFrame frame;

    private final GraphicSettings settings;

    private AbstractScene currentScene;

    public GameGraphics(GameEngine engine) {
        this.gameEngine = engine;

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        settings = ((GraphicSettings) SettingManager.getInstance().getSetting(GraphicSettings.class));

        updateWindowType();

        frame.setFocusTraversalKeysEnabled(false);

        frame.setIconImage(ResourceManager.MAIN_LOGO.getResource());
        frame.setTitle("Mission Takedown");

        addUpdateEvent();
    }

    private void addUpdateEvent() {
        final Dimension[] oldDim = {new Dimension(0, 0)};
        final int[] tickIndex = {0};
        gameEngine.getTickProvider().registerTick(currentTick -> {
            if (tickIndex[0] >= 10) {
                Dimension currentSize = frame.getSize();
                if (!currentSize.equals(oldDim[0])) {
                    oldDim[0] = currentSize;
                    applyNewSize();
                }
                tickIndex[0] = 0;
            }
            tickIndex[0]++;
        });
    }

    private void applyNewSize() {
        int width = getFrame().getWidth() - getFrame().getInsets().left - getFrame().getInsets().right;
        int height = getFrame().getHeight() - getFrame().getInsets().top - getFrame().getInsets().bottom;
        if (currentScene != null) {
            currentScene.setSize(width, height);
        }

        ResourceManager.scalingProperty().set(new FloatPair((float) width / 1920, (float) height / 1080));
    }

    public void start() {
        frame.setVisible(true);
    }

    public void switchToScene(AbstractScene scene) {
        if (currentScene != null) {
            currentScene.sceneHidden();
        }

        currentScene = scene;
        int width = getFrame().getWidth() - getFrame().getInsets().left - getFrame().getInsets().right;
        int height = getFrame().getHeight() - getFrame().getInsets().top - getFrame().getInsets().bottom;
        currentScene.setSize(width, height);
        gameEngine.getDialogManager().unsetDialog();
        frame.setContentPane(currentScene);
        currentScene.sceneShown();
        applyNewSize();
    }

    public void switchToScene(AbstractScene scene, AbstractFade fader) {
        fader.fade(scene, this);
    }

    public void addFadeOverlay(Component component) {
        frame.getLayeredPane().add(component, new Integer(Integer.MAX_VALUE)); //Max value -> nothing should be higher
    }

    public void addDialogLayer(Component component) {
        frame.getLayeredPane().add(component, new Integer(6000));
    }

    public void removeLayer(Component component) {
        frame.getLayeredPane().remove(component);
        frame.repaint();
    }

    public JFrame getFrame() {
        return frame;
    }

    public void updateWindowType() {
        boolean wasVisible = frame.isDisplayable() || frame.isVisible();

        if (wasVisible) {
            frame.dispose();
        }

        GraphicsDevice selectedDisplay = settings.getSelectedDisplay();

        switch (settings.getCurrentWindowSetting()) {
            case WINDOWED: {
                frame.setUndecorated(false);
                Rectangle displayBounds = selectedDisplay.getDefaultConfiguration().getBounds();
                int width = (int) (displayBounds.getWidth() / 2);
                int height = (int) (displayBounds.getHeight() / 2);

                int x = (int) (displayBounds.getX() + displayBounds.getWidth() / 2 - (double) width / 2);
                int y = (int) (displayBounds.getY() + displayBounds.getHeight() / 2 - (double) height / 2);
                frame.setBounds(x, y, width, height);

                break;
            }
            case WINDOWED_FULLSCREEN: {
                frame.setUndecorated(true);
                frame.setBounds(selectedDisplay.getDefaultConfiguration().getBounds());
                break;
            }
            case FULLSCREEN: {
                frame.setUndecorated(false);
                selectedDisplay.setFullScreenWindow(frame);
                break;
            }

        }

        if (wasVisible) {
            frame.setVisible(true);
        }

        frame.repaint();
    }

    public List<GraphicsDevice> getAvailableGraphicsDevice() {
        return Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices());
    }

    public GraphicsDevice getSelectedGraphicsDevice() {
        return settings.getSelectedDisplay();
    }

    public void setSelectedDevice(GraphicsDevice graphicsDevice) {
        settings.setSelectedDisplay(graphicsDevice.getIDstring());
    }

    public GraphicSettings getSettings() {
        return settings;
    }

    public AbstractScene getCurrentScene() {
        return currentScene;
    }
}
