package de.jp.infoprojekt.gameengine.scenes.main;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.scenes.AbstractScene;
import de.jp.infoprojekt.resources.scenes.TitleSceneResource;
import de.jp.infoprojekt.settings.graphics.WindowTypeSetting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

//TODO rewrite

/**
 * SettingsScene class
 *
 * @author Pascal
 * @version 19.06.2025
 */
public class SettingsScene extends AbstractScene {

    private GameEngine gameEngine;

    private Box verticalSettingsBox = Box.createVerticalBox();

    private JComboBox<String> graphicsDeviceList = new JComboBox<>();
    private JComboBox<String> windowTypeList = new JComboBox<>();

    private JButton applyButton = new JButton("Anwenden");

    public SettingsScene(GameEngine engine) {
        this.gameEngine = engine;

        setBackground(Color.BLACK);

        setLayout(new BorderLayout());

        /*JTextField inputField = new JTextField();
        inputField.setBounds(50, 50, 250, 35);
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        inputField.setForeground(Color.DARK_GRAY);
        inputField.setBackground(new Color(230, 230, 250)); // Light lavender
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 200), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setBounds(310, 50, 90, 35);
        confirmButton.setBackground(new Color(100, 149, 237)); // Cornflower blue
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        confirmButton.setFocusPainted(false);
        confirmButton.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));

        // Add button listener
        confirmButton.addActionListener(e -> {
            String text = inputField.getText();
            JOptionPane.showMessageDialog(null, "You entered: " + text);
        });

        add(confirmButton);
        add(inputField);*/


        verticalSettingsBox.add(addGraphicsDeviceList());
        verticalSettingsBox.add(Box.createRigidArea(new Dimension(0, 10)));
        verticalSettingsBox.add(addWindowTypeList());

        add(verticalSettingsBox, BorderLayout.PAGE_START);

        add(addApplyButton(), BorderLayout.PAGE_END);
    }

    private Component addGraphicsDeviceList() {
        List<GraphicsDevice> graphicsDevices = gameEngine.getGraphics().getAvailableGraphicsDevice();
        String[] graphicsDevicesNames = new String[graphicsDevices.size()];
        for (int i = 0; i < graphicsDevices.size(); i++) {
            graphicsDevicesNames[i] = graphicsDevices.get(i).getIDstring();
        }
        graphicsDeviceList.setModel(new DefaultComboBoxModel<>(graphicsDevicesNames));
        graphicsDeviceList.setSize(250, 35);
        graphicsDeviceList.setBackground(new Color(245, 245, 255));
        graphicsDeviceList.setForeground(Color.BLACK);
        graphicsDeviceList.setFont(new Font("SansSerif", Font.BOLD, 14));

        graphicsDeviceList.setSelectedItem(gameEngine.getGraphics().getSelectedGraphicsDevice().getIDstring());

        //graphicsList.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                graphicsDeviceList.setLocation((getWidth() / 2) - (graphicsDeviceList.getWidth() / 2), 100);
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
        windowTypeList.setSize(250, 35);
        windowTypeList.setBackground(new Color(245, 245, 255));
        windowTypeList.setForeground(Color.BLACK);
        windowTypeList.setFont(new Font("SansSerif", Font.BOLD, 14));

        windowTypeList.setSelectedItem(gameEngine.getGraphics().getSettings().getCurrentWindowSetting().getFriendlyName());

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                windowTypeList.setLocation((getWidth() / 2) - (windowTypeList.getWidth() / 2), 300);
            }
        });

        return windowTypeList;
    }

    private Component addApplyButton() {
        applyButton.setSize(250, 35);
        applyButton.setBackground(new Color(245, 245, 255));
        applyButton.setForeground(Color.BLACK);
        applyButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                applyButton.setLocation((getWidth() / 2) - (applyButton.getWidth() / 2), 500);
            }
        });

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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        g2d.drawImage(TitleSceneResource.TITLE_SCREEN.getResource(), 0, 0, getWidth(), getHeight(), null);
        g2d.dispose();
    }

}
