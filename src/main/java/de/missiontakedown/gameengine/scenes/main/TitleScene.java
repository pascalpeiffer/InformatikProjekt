package de.missiontakedown.gameengine.scenes.main;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.graphics.fade.BlackFade;
import de.missiontakedown.gameengine.graphics.render.TextRenderer;
import de.missiontakedown.gameengine.scenes.AbstractScene;
import de.missiontakedown.gameengine.scenes.spawn.SpawnScene;
import de.missiontakedown.gameengine.scenes.util.ColorScene;
import de.missiontakedown.resources.ResourceManager;
import de.missiontakedown.resources.ScalingEvent;
import de.missiontakedown.resources.scenes.TitleSceneResource;
import de.missiontakedown.util.FloatPoint;
import de.missiontakedown.util.FloatRectangle;
import de.missiontakedown.util.FontManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Pascal
 */
public class TitleScene extends AbstractScene implements ScalingEvent {

    private final Font titleFont = FontManager.JERSEY_20;

    private final FloatRectangle playHitBox = new FloatRectangle(0.32f, 0.37f, 0.35f, 0.15f);
    private final FloatRectangle settingsHitBox = new FloatRectangle(0.32f, 0.58f, 0.35f, 0.15f);
    private final FloatRectangle exitHitBox = new FloatRectangle(0.32f, 0.79f, 0.35f, 0.15f);

    private final GameEngine engine;

    private boolean clickable = true;

    public TitleScene(GameEngine engine) {
        this.engine = engine;
        setBackground(Color.BLACK);
        setLayout(null);
        ResourceManager.addScalingListener(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                float relativeX = (float) e.getX() / ResourceManager.getGameScreenWidth();
                float relativeY = (float) e.getY() / ResourceManager.getGameScreenHeight();

                FloatPoint mouseHit = new FloatPoint(relativeX, relativeY);

                onMouseClick(mouseHit);
            }
        });
    }

    private void onMouseClick(FloatPoint mouseHit) {
        if (!clickable) {
            return;
        }

        if (playHitBox.contains(mouseHit)) {
            clickable = false;
            engine.getStateManager().reset();
            engine.getGraphics().switchToScene(new SpawnScene(engine), new BlackFade(engine, 400));
        }


        if (settingsHitBox.contains(mouseHit)) {
            engine.getGraphics().switchToScene(new SettingsScene(engine), new BlackFade(engine, 400));
        }

        if (exitHitBox.contains(mouseHit)) {
            clickable = false;
            engine.getGraphics().switchToScene(new ColorScene(Color.BLACK), new BlackFade(engine, 400, () -> {
                System.exit(0);
            }));
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.drawImage(TitleSceneResource.TITLE_SCREEN.getResource(), 0, 0, getWidth(), getHeight(), null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        g2d.drawImage(TitleSceneResource.TAKE_DOWN_TITLE.getResource(), getWidth() / 2 - TitleSceneResource.TAKE_DOWN_TITLE.getWidth() / 2, 0, TitleSceneResource.TAKE_DOWN_TITLE.getWidth(), TitleSceneResource.TAKE_DOWN_TITLE.getHeight(), null);

        g2d.drawImage(TitleSceneResource.BUTTON_FRAME.getResource(), getWidth() / 2 - TitleSceneResource.BUTTON_FRAME.getWidth() / 2, TitleSceneResource.TAKE_DOWN_TITLE.getHeight(), TitleSceneResource.BUTTON_FRAME.getWidth(), TitleSceneResource.BUTTON_FRAME.getHeight(), null);

        g2d.drawImage(TitleSceneResource.BUTTON_FRAME.getResource(), getWidth() / 2 - TitleSceneResource.BUTTON_FRAME.getWidth() / 2, TitleSceneResource.TAKE_DOWN_TITLE.getHeight() + TitleSceneResource.BUTTON_FRAME.getHeight() + (ResourceManager.getGameScreenHeight() / 15), TitleSceneResource.BUTTON_FRAME.getWidth(), TitleSceneResource.BUTTON_FRAME.getHeight(), null);

        g2d.drawImage(TitleSceneResource.BUTTON_FRAME.getResource(), getWidth() / 2 - TitleSceneResource.BUTTON_FRAME.getWidth() / 2, TitleSceneResource.TAKE_DOWN_TITLE.getHeight() + TitleSceneResource.BUTTON_FRAME.getHeight() * 2 + (ResourceManager.getGameScreenHeight() / 15 * 2), TitleSceneResource.BUTTON_FRAME.getWidth(), TitleSceneResource.BUTTON_FRAME.getHeight(), null);


        FontMetrics metrics = g2d.getFontMetrics(getTitleFont());

        //Button1
        String button1 = "Play";
        //TODO fix max 10000
        TextRenderer.drawFormattedString(g2d, button1, getWidth() / 2 - metrics.stringWidth(button1) / 2, (int) (TitleSceneResource.TAKE_DOWN_TITLE.getHeight() + TitleSceneResource.BUTTON_FRAME.getHeight() * 0.5f + metrics.getDescent()), 10000, 10000, getTitleFont(), Integer.MAX_VALUE);

        //Buton2
        String button2 = "Settings";
        TextRenderer.drawFormattedString(g2d, button2, getWidth() / 2 - metrics.stringWidth(button2) / 2, (int) (TitleSceneResource.TAKE_DOWN_TITLE.getHeight() + TitleSceneResource.BUTTON_FRAME.getHeight() * 1.5f + metrics.getDescent() + ((float) ResourceManager.getGameScreenHeight() / 15)), 10000, 10000, getTitleFont(), Integer.MAX_VALUE);

        //Buton3
        String button3 = "Exit";
        TextRenderer.drawFormattedString(g2d, button3, getWidth() / 2 - metrics.stringWidth(button3) / 2, (int) (TitleSceneResource.TAKE_DOWN_TITLE.getHeight() + TitleSceneResource.BUTTON_FRAME.getHeight() * 2.5f + metrics.getDescent() + ((float) ResourceManager.getGameScreenHeight() / 15 * 2)), 10000, 10000, getTitleFont(), Integer.MAX_VALUE);

        g2d.dispose();
    }

    public Font getTitleFont() {
        return titleFont.deriveFont(60 * ResourceManager.getScaling().getX());
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        repaint();
    }

}
