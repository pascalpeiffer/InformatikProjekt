package de.missiontakedown.gameengine.scenes.spawn;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.dialog.computer.ColaInFridgeDialog;
import de.missiontakedown.gameengine.dialog.spawn.ChooseRecipeDialog;
import de.missiontakedown.gameengine.dialog.spawn.TooLessMoneyForSulfuricDialog;
import de.missiontakedown.gameengine.gameobjects.computer.AmazonIcon;
import de.missiontakedown.gameengine.gameobjects.computer.WikipediaIcon;
import de.missiontakedown.gameengine.gameobjects.interaction.InteractionHint;
import de.missiontakedown.gameengine.gameobjects.overlay.InventoryOverlay;
import de.missiontakedown.gameengine.gameobjects.overlay.MoneyOverlay;
import de.missiontakedown.gameengine.gameobjects.overlay.QuestOverlay;
import de.missiontakedown.gameengine.graphics.fade.BlackFade;
import de.missiontakedown.gameengine.inventory.Item;
import de.missiontakedown.gameengine.scenes.AbstractScene;
import de.missiontakedown.gameengine.state.GameState;
import de.missiontakedown.gameengine.state.QuestState;
import de.missiontakedown.gameengine.tick.GameTick;
import de.missiontakedown.resources.GameResource;
import de.missiontakedown.resources.ResourceManager;
import de.missiontakedown.resources.ScalingEvent;
import de.missiontakedown.resources.scenes.ComputerSceneResource;
import de.missiontakedown.settings.GAME_SETTINGS;
import de.missiontakedown.util.FloatPoint;
import de.missiontakedown.util.FloatRectangle;

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
    private FloatRectangle fogFluid;
    private FloatRectangle cola;

    //Wiki Hits
    private FloatRectangle salpeterHyperlink;
    private FloatRectangle ammoniakHyperlink;

    private GameResource wikiBackground = ComputerSceneResource.WIKIPEDIA_NITROGLYCERIN;
    private GameResource amazonBackground = ComputerSceneResource.AMAZON_DESKTOP;

    private State computerState = State.DESKTOP;

    private final InteractionHint hint;
    private int hintShowTicks = 0;

    public ComputerScene(GameEngine engine, FloatPoint playerPos) {
        this.oldPlayerPos = playerPos;
        this.engine = engine;
        setLayout(null);
        ResourceManager.addScalingListener(this);

        initMoneyOverlay();
        initQuestOverlay();
        initInventoryOverlay();

        hint = new InteractionHint("");
        hint.setVisible(false);
        add(hint);

        exitButton = new FloatRectangle(0.8f, 0.12f, 0.05f, 0.03f);

        sulfuricAcid = new FloatRectangle(0.5f, 0.465f, 0.05f, 0.035f);
        fogFluid = new FloatRectangle(0.27f,0.72f,0.05f, 0.035f);
        cola = new FloatRectangle(0.28f,0.46f,0.05f, 0.035f);

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

        chooseDialogCooldown = engine.getTickProvider().getTicksPerSecond() * 3;

        if (engine.getStateManager().getState() == GameState.CLICKED_SALPETER_HYPERLINK) {
            wikiBackground = ComputerSceneResource.WIKIPEDIA_SALPETER_AMMONIAK_MARKED;
        }else if (engine.getStateManager().getState() == GameState.GO_TO_FARMER) {
            wikiBackground = ComputerSceneResource.WIKIPEDIA_AMMONIAK;
        }
    }

    private void initInventoryOverlay() {
        add(new InventoryOverlay(engine));
    }

    private void onMouseClick(FloatPoint mouseHit) {

        ComputerSceneResource.COMPUTER_CLICk.create().play();

        //Exit button hit
        if (exitButton.contains(mouseHit)) {
            if (computerState != State.DESKTOP) {
                computerState = State.DESKTOP;
                amazonIcon.setVisible(true);
                wikipediaIcon.setVisible(true);
                repaint();
            }
        }

        if (computerState == State.AMAZON) {
            if (cola.contains(mouseHit)) {
                ColaInFridgeDialog dialog = new ColaInFridgeDialog(engine);
                engine.getDialogManager().setDialog(dialog);
            }
        }

        //Buy button - Amazon
        if (computerState == State.AMAZON) {

            if (sulfuricAcid.contains(mouseHit)) {
                if (engine.getStateManager().getMoneyCount() >= GAME_SETTINGS.SULFURIC_ACID_COST) {
                    hint.setHint("Schwefelsäure gekauft");
                    hintShowTicks = engine.getTickProvider().getTicksPerSecond() * 4;
                    hint.setVisible(true);
                    repaint();
                    engine.getStateManager().removeMoney(GAME_SETTINGS.SULFURIC_ACID_COST);
                    engine.getInventoryManager().addItem(new Item(Item.Type.SulfuricAcid));
                    ComputerSceneResource.AMAZON_BUY.create().play();
                }else {
                    hint.setHint("Nicht genug Geld!");
                    hintShowTicks = engine.getTickProvider().getTicksPerSecond() * 4;
                    hint.setVisible(true);
                    repaint();
                }
            }

            if (fogFluid.contains(mouseHit)) {

                if (engine.getStateManager().getState() == GameState.BUY_FOG_FLUID) {
                    if (engine.getStateManager().getMoneyCount() >= GAME_SETTINGS.SMOKE_FLUID_COST) {
                        hint.setHint("Nebelfluid gekauft");
                        hintShowTicks = engine.getTickProvider().getTicksPerSecond() * 4;
                        hint.setVisible(true);
                        repaint();
                        engine.getStateManager().removeMoney(GAME_SETTINGS.SMOKE_FLUID_COST);
                        engine.getInventoryManager().addItem(new Item(Item.Type.FogFluid));
                        engine.getStateManager().setState(GameState.BOUGHT_FOG_FLUID);
                        engine.getStateManager().setQuest(QuestState.BOUGHT_FOG_FLUID);
                        ComputerSceneResource.AMAZON_BUY.create().play();
                    }else {
                        hint.setHint("Nicht genug Geld!");
                        hintShowTicks = engine.getTickProvider().getTicksPerSecond() * 4;
                        hint.setVisible(true);
                        repaint();
                    }
                }else {
                    hint.setHint("Kann nicht gekauft werden!");
                    hintShowTicks = engine.getTickProvider().getTicksPerSecond() * 4;
                    hint.setVisible(true);
                    repaint();
                }

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
        add(new QuestOverlay(engine));
    }

    private int chooseDialogCooldown;
    @Override
    public void tick(long currentTick) {
        computerEscapeTick();

        if (hintShowTicks <= 0) {
            if (hint.isVisible()) {
                hint.setVisible(false);
                repaint();
            }
        }else {
            hintShowTicks--;
        }

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
