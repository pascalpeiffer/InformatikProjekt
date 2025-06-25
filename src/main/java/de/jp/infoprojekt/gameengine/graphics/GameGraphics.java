package de.jp.infoprojekt.gameengine.graphics;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.graphics.fade.AbstractFade;
import de.jp.infoprojekt.gameengine.scenes.AbstractScene;
import de.jp.infoprojekt.gameengine.scenes.spawn.SpawnScene;
import de.jp.infoprojekt.settings.SettingManager;
import de.jp.infoprojekt.settings.graphics.GraphicSettings;
import de.jp.infoprojekt.resources.ResourceManager;
import de.jp.infoprojekt.util.FloatPair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.List;


/**
 * GameGraphics class
 *
 * @author Pascal
 * @version 14.06.2025
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

        frame.setIconImage(ResourceManager.TITLE_SCENE_BACKGROUND);
        frame.setTitle("Game");

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                applyNewSize();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                applyNewSize();
            }

        });

        addUpdateEvent();
    }

    private void addUpdateEvent() {
        final Dimension[] oldDim = {new Dimension(0, 0)};
        new Timer(100, e -> {
            Dimension currentSize = frame.getSize();
            if (!currentSize.equals(oldDim[0])) {
                oldDim[0] = currentSize;
                applyNewSize();
            }
        }).start();
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

        switchToScene(new SpawnScene(gameEngine));

        /*new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            SwingUtilities.invokeLater(() -> {
                switchToScene(new SpawnScene(), new BlackFade());
            });
        }).start();*/
    }

    public void switchToScene(AbstractScene scene) {
        currentScene = scene;
        int width = getFrame().getWidth() - getFrame().getInsets().left - getFrame().getInsets().right;
        int height = getFrame().getHeight() - getFrame().getInsets().top - getFrame().getInsets().bottom;
        currentScene.setSize(width, height);
        frame.setContentPane(currentScene);
    }

    public void switchToScene(AbstractScene scene, AbstractFade fader) {
        fader.fade(scene, this);
    }

    public void addFadeOverlay(Component component) {
        frame.getLayeredPane().add(component, new Integer(Integer.MAX_VALUE)); //Max value -> nothing should be higher
    }

    public void removeLayer(Component component) {
        frame.getLayeredPane().remove(component);
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
}
