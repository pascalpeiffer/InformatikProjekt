package de.jp.infoprojekt.gameengine.scenes.spawn;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.dialog.spawn.ChooseRecipeDialog;
import de.jp.infoprojekt.gameengine.dialog.spawn.TooLessMoneyForSulfuricDialog;
import de.jp.infoprojekt.gameengine.gameobjects.computer.AmazonIcon;
import de.jp.infoprojekt.gameengine.gameobjects.computer.WikipediaIcon;
import de.jp.infoprojekt.gameengine.gameobjects.overlay.MoneyOverlay;
import de.jp.infoprojekt.gameengine.gameobjects.overlay.QuestOverlay;
import de.jp.infoprojekt.gameengine.graphics.fade.BlackFade;
import de.jp.infoprojekt.gameengine.scenes.AbstractScene;
import de.jp.infoprojekt.gameengine.state.GameState;
import de.jp.infoprojekt.gameengine.state.QuestState;
import de.jp.infoprojekt.gameengine.tick.GameTick;
import de.jp.infoprojekt.resources.GameResource;
import de.jp.infoprojekt.resources.ResourceManager;
import de.jp.infoprojekt.resources.ScalingEvent;
import de.jp.infoprojekt.resources.scenes.ComputerSceneResource;
import de.jp.infoprojekt.settings.GAME_SETTINGS;
import de.jp.infoprojekt.util.FloatPoint;
import de.jp.infoprojekt.util.FloatRectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ComputerScene extends AbstractScene implements ScalingEvent, GameTick {

    private final GameEngine engine;

    private AmazonIcon amazonIcon;
    private WikipediaIcon wikipediaIcon;

    private FloatRectangle exitButton;

    private final FloatPoint oldPlayerPos;

    //Amazon Hits //TODO easter egg
    private FloatRectangle sulfuricAcid;
    private FloatRectangle fogJuice;

    //Wiki Hits
    private FloatRectangle salpeterHyperlink;
    private FloatRectangle ammoniakHyperlink;

    private GameResource wikiBackground = ComputerSceneResource.WIKIPEDIA_NITROGLYCERIN;
    private GameResource amazonBackground = ComputerSceneResource.AMAZON_DESKTOP;

    private State computerState = State.DESKTOP;

    public ComputerScene(GameEngine engine, FloatPoint playerPos) {
        this.oldPlayerPos = playerPos;
        this.engine = engine;
        setLayout(null);
        ResourceManager.addScalingListener(this);

        initMoneyOverlay();
        initQuestOverlay();

        exitButton = new FloatRectangle(0.8f, 0.12f, 0.05f, 0.03f);

        sulfuricAcid = new FloatRectangle(0.5f, 0.465f, 0.05f, 0.035f);
        fogJuice = new FloatRectangle(0.27f,0.72f,0.05f, 0.035f);

        salpeterHyperlink = new FloatRectangle(0.53f,0.66f,0.09f,0.04f);
        ammoniakHyperlink = new FloatRectangle(0.67f, 0.55f, 0.06f, 0.04f);

        amazonIcon = new AmazonIcon();
        amazonIcon.setRelativeLocation(new FloatPoint(0.4f, 0.5f));
        add(amazonIcon);

        wikipediaIcon = new WikipediaIcon();
        wikipediaIcon.setRelativeLocation(new FloatPoint(0.6f, 0.5f));

        wikipediaIcon.onClick(() -> {
            computerState = State.WIKIPEDIA;
            amazonIcon.setVisible(false);
            wikipediaIcon.setVisible(false);
            repaint();
        });

        amazonIcon.onClick(() -> {
            computerState = State.AMAZON;
            amazonIcon.setVisible(false);
            wikipediaIcon.setVisible(false);
            repaint();
        });

        repaint();

        add(wikipediaIcon);

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

        chooseDialogCooldown = engine.getTickProvider().getTicksPerSecond() * 5;

        if (engine.getStateManager().getState() == GameState.CLICKED_SALPETER_HYPERLINK) {
            wikiBackground = ComputerSceneResource.WIKIPEDIA_SALPETER_AMMONIAK_MARKED;
        }else if (engine.getStateManager().getState() == GameState.GO_TO_FARMER) {
            wikiBackground = ComputerSceneResource.WIKIPEDIA_AMMONIAK;
        }
    }

    private void onMouseClick(FloatPoint mouseHit) {

        //System.out.println(mouseHit);

        //Exit button hit
        if (exitButton.contains(mouseHit)) {
            if (computerState != State.DESKTOP) {
                computerState = State.DESKTOP;
                amazonIcon.setVisible(true);
                wikipediaIcon.setVisible(true);
                repaint();
            }
        }

        //Buy button - Amazon
        if (computerState == State.AMAZON) {

            if (sulfuricAcid.contains(mouseHit)) {

            }

            if (fogJuice.contains(mouseHit)) {

            }

        }

        if (wikiBackground == ComputerSceneResource.WIKIPEDIA_NITROGLYCERIN_SALPETER_MARKED && computerState == State.WIKIPEDIA) {
            if (salpeterHyperlink.contains(mouseHit)) {
                engine.getStateManager().setState(GameState.CLICKED_SALPETER_HYPERLINK);
                wikiBackground = ComputerSceneResource.WIKIPEDIA_SALPETER_AMMONIAK_MARKED;
                repaint();
            }
        }else if (wikiBackground == ComputerSceneResource.WIKIPEDIA_SALPETER_AMMONIAK_MARKED && computerState == State.WIKIPEDIA) {
            if (ammoniakHyperlink.contains(mouseHit)) {
                wikiBackground = ComputerSceneResource.WIKIPEDIA_AMMONIAK;
                //engine.getStateManager().setState(GameState.CLICKED_AMMONIAK_HYPERLINK); //TODO if delay
                engine.getStateManager().setState(GameState.GO_TO_FARMER);
                engine.getStateManager().setQuest(QuestState.GO_TO_FARMER);
                repaint();
            }
        }
    }

    private void initMoneyOverlay() {
        add(new MoneyOverlay(engine));
    }

    private void initQuestOverlay() {
        QuestOverlay questOverlay = new QuestOverlay(engine);
        add(questOverlay);
    }

    private int chooseDialogCooldown;
    @Override
    public void tick(long currentTick) {
        computerEscapeTick();

        if (engine.getStateManager().getState() == GameState.FIRST_NITRIC_ACID) {
            if (wikiBackground != ComputerSceneResource.WIKIPEDIA_NITROGLYCERIN_SALPETER_MARKED) {
                wikiBackground = ComputerSceneResource.WIKIPEDIA_NITROGLYCERIN_SALPETER_MARKED;
                repaint();
            }
        }


        if (engine.getStateManager().getState() == GameState.GAME_INTRODUCED && computerState == State.WIKIPEDIA) {
            if (chooseDialogCooldown <= 0) {
                ChooseRecipeDialog dialog = new ChooseRecipeDialog(engine);
                engine.getDialogManager().setDialog(dialog);
                engine.getStateManager().setState(GameState.CHOOSING_RECIPE);
            }else {
                chooseDialogCooldown--;
            }
        }else {
            chooseDialogCooldown = engine.getTickProvider().getTicksPerSecond() * 5;
        }

        if (engine.getStateManager().getState() == GameState.FIRST_SULFURIC_ACID && computerState == State.AMAZON) {
            if (engine.getStateManager().getMoneyCount() < GAME_SETTINGS.SULFURIC_ACID_COST) {
                engine.getStateManager().setState(GameState.FIRST_NITRIC_ACID);
                new Thread(() -> {
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    SwingUtilities.invokeLater(() -> {
                        TooLessMoneyForSulfuricDialog dialog = new TooLessMoneyForSulfuricDialog(engine);
                        engine.getDialogManager().setDialog(dialog);
                    });
                }).start();
            }
        }
    }

    private void computerEscapeTick() {
        if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().ESCAPE)) {
            SpawnScene spawnScene = new SpawnScene(engine);
            spawnScene.getPlayer().setRelativeLocation(oldPlayerPos);
            engine.getGraphics().switchToScene(spawnScene, new BlackFade(engine));
        }
    }

    @Override
    public void scale(float widthMultiply, float heightMultiply) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (computerState == State.DESKTOP) {
            g.drawImage(ComputerSceneResource.DESKTOP.getResource(), 0, 0, getWidth(), getHeight(), null);
        }else if (computerState == State.WIKIPEDIA) {
            g.drawImage(wikiBackground.getResource(), 0, 0, getWidth(), getHeight(), null);
        }else if (computerState == State.AMAZON) {
            g.drawImage(amazonBackground.getResource(), 0, 0, getWidth(), getHeight(), null);
        }

    }

    @Override
    public void sceneShown() {
        super.sceneShown();
        engine.getTickProvider().registerTick(this);
    }

    @Override
    public void sceneHidden() {
        super.sceneHidden();
        engine.getTickProvider().unregisterTick(this);
    }

    private enum State {
        DESKTOP,
        WIKIPEDIA,
        AMAZON;
    }

}
