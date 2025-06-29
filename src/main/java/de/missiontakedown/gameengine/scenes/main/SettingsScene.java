package de.missiontakedown.gameengine.scenes.main;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.graphics.fade.BlackFade;
import de.missiontakedown.gameengine.graphics.render.TextRenderer;
import de.missiontakedown.gameengine.scenes.AbstractScene;
import de.missiontakedown.resources.ResourceManager;
import de.missiontakedown.resources.ScalingEvent;
import de.missiontakedown.resources.scenes.TitleSceneResource;
import de.missiontakedown.settings.graphics.WindowTypeSetting;
import de.missiontakedown.util.FontManager;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * @author Pascal
 */
//TODO rewrite this mess
public class SettingsScene extends AbstractScene implements ScalingEvent {

    private GameEngine gameEngine;

    private JComboBox<String> graphicsDeviceList = new JComboBox<>();
    private JComboBox<String> windowTypeList = new JComboBox<>();

    private JButton applyButton = new JButton("Anwenden");

    private final Font font = FontManager.JERSEY_20;

    public SettingsScene(GameEngine engine) {
        this.gameEngine = engine;
        setBackground(Color.BLACK);
        setLayout(null);

        ResourceManager.addScalingListener(this);

        add(addGraphicsDeviceList());
        add(addWindowTypeList());

        add(addApplyButton());

        update();
    }

    private Component addGraphicsDeviceList() {
        List<GraphicsDevice> graphicsDevices = gameEngine.getGraphics().getAvailableGraphicsDevice();
        String[] graphicsDevicesNames = new String[graphicsDevices.size()];
        for (int i = 0; i < graphicsDevices.size(); i++) {
            graphicsDevicesNames[i] = graphicsDevices.get(i).getIDstring();
        }
        graphicsDeviceList.setModel(new DefaultComboBoxModel<>(graphicsDevicesNames));

        graphicsDeviceList.setForeground(Color.BLACK);
        graphicsDeviceList.setFont(new Font("SansSerif", Font.BOLD, 14));

        graphicsDeviceList.setSelectedItem(gameEngine.getGraphics().getSelectedGraphicsDevice().getIDstring());

        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        graphicsDeviceList.setRenderer(renderer);

        graphicsDeviceList.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                return new JButton() {
                    @Override
                    public int getWidth() {
                        return 0; // effectively hides the button
                    }

                    @Override
                    public void paint(Graphics g) {
                        // do nothing
                    }
                };
            }

            @Override
            public void configureArrowButton() {
                // skip adding the button
            }
        });

        return graphicsDeviceList;
    }

    private Component addWindowTypeList() {
        WindowTypeSetting[] windowTypes = WindowTypeSetting.values();
        String[] windowTypeNames = new String[windowTypes.length];

        for (int i = 0; i < windowTypes.length; i++) {
            windowTypeNames[i] = windowTypes[i].getFriendlyName();
        }

        windowTypeList.setModel(new DefaultComboBoxModel<>(windowTypeNames));
        windowTypeList.setBackground(new Color(245, 245, 255));
        windowTypeList.setForeground(Color.BLACK);
        windowTypeList.setFont(new Font("SansSerif", Font.BOLD, 14));

        windowTypeList.setSelectedItem(gameEngine.getGraphics().getSettings().getCurrentWindowSetting().getFriendlyName());

        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        windowTypeList.setRenderer(renderer);

        windowTypeList.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                return new JButton() {
                    @Override
                    public int getWidth() {
                        return 0;
                    }

                    @Override
                    public void paint(Graphics g) {}
                };
            }

            @Override
            public void configureArrowButton() {
                // skip adding the button
            }
        });

        return windowTypeList;
    }

    private void update() {
        graphicsDeviceList.setSize(getWidth() / 4, getHeight() / 10);
        graphicsDeviceList.setLocation((getWidth() / 2) - (graphicsDeviceList.getWidth() / 2), getHeight() / 10 * 2);

        windowTypeList.setSize(getWidth() / 4, getHeight() / 10);
        windowTypeList.setLocation((getWidth() / 2) - (windowTypeList.getWidth() / 2), getHeight() / 10 * 4);

        applyButton.setSize(getWidth() / 4, getHeight() / 10);
        applyButton.setLocation((getWidth() / 2) - (applyButton.getWidth() / 2), getHeight() / 10 * 8);
    }

    private Component addApplyButton() {
        applyButton.setBackground(new Color(245, 245, 255));
        applyButton.setForeground(Color.BLACK);
        applyButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        applyButton.addActionListener(e -> {
            applySettings();
        });

        return applyButton;
    }

    private void applySettings() {

        gameEngine.getGraphics().getSettings().setCurrentWindowSetting(WindowTypeSetting.getByFriendlyName((String) windowTypeList.getSelectedItem()));

        GraphicsEnvironment ev = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (GraphicsDevice screenDevice : ev.getScreenDevices()) {
            if (screenDevice.getIDstring().equalsIgnoreCase((String) graphicsDeviceList.getSelectedItem())) {
                gameEngine.getGraphics().setSelectedDevice(screenDevice);
            }
        }

        gameEngine.getGraphics().updateWindowType();

        gameEngine.getGraphics().switchToScene(new TitleScene(gameEngine));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        g2d.drawImage(TitleSceneResource.TITLE_SCREEN.getResource(), 0, 0, getWidth(), getHeight(), null);
        g2d.dispose();

        FontMetrics metrics = getFontMetrics(getF());
        String text = "Für Style keine Zeit ._.";
        //TOOD fix 10000
        TextRenderer.drawFormattedString(g, text, getWidth() / 2 - (metrics.stringWidth(text) / 2), getHeight() - metrics.getHeight(), 10000, 10000, getF(), Integer.MAX_VALUE);
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        update();
        repaint();
    }

    public Font getF() {
        return font.deriveFont((float) 20 * ResourceManager.getScaling().getX());
    }

    public FontMetrics getFontMetrics(Font font) {
        return super.getFontMetrics(getF());
    }
}
