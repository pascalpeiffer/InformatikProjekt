package de.missiontakedown.gameengine.scenes.farmer;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.gameobjects.cowminigame.Poop;
import de.missiontakedown.gameengine.gameobjects.overlay.MoneyOverlay;
import de.missiontakedown.gameengine.gameobjects.overlay.QuestOverlay;
import de.missiontakedown.gameengine.gameobjects.overlay.TextOverlay;
import de.missiontakedown.gameengine.graphics.fade.BlackFade;
import de.missiontakedown.gameengine.scenes.AbstractScene;
import de.missiontakedown.gameengine.state.QuestState;
import de.missiontakedown.gameengine.tick.GameTick;
import de.missiontakedown.resources.GameAudioResource;
import de.missiontakedown.resources.GameResource;
import de.missiontakedown.resources.scenes.CowMinigameSceneResource;
import de.missiontakedown.settings.GAME_SETTINGS;
import de.missiontakedown.util.FloatPoint;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.util.Random;

public class CowMinigameScene extends AbstractScene implements GameTick {

    private final GameEngine engine;

    private GameResource backgroundResource = CowMinigameSceneResource.BACKGROUND;

    private final TextOverlay exitOverlay;

    private final FloatPoint row1Begin = new FloatPoint(0.44f, 0.69f);
    private final FloatPoint row1End = new FloatPoint(0.312f, 0.98f);

    private final FloatPoint row2Begin = new FloatPoint(0.535f, 0.69f);
    private final FloatPoint row2End = new FloatPoint(0.673f, 0.98f);

    private int poopCount = 0;
    private final int maxPoop = 2;

    private final float poopDelaySec = 0.8f;

    public CowMinigameScene(GameEngine engine) {
        this.engine = engine;
        setLayout(null);

        add(new MoneyOverlay(engine));
        add(new QuestOverlay(engine));

        exitOverlay = new TextOverlay("Verlassen");
        exitOverlay.setRelativeLocation(0.05f,0.85f);
        exitOverlay.setVisible(false);
        add(exitOverlay);

        exitOverlay.onClick(() -> {
            engine.getGraphics().switchToScene(new FarmerScene(engine), new BlackFade(engine));
        });
    }

    private void spawnRandomPoop() {
        Poop poop = new Poop();

        if (new Random().nextBoolean()) {
            FloatPoint pos = FloatPoint.getRandomPointBetween(row1Begin, row1End);
            poop.setScale(pos.getY());
            poop.setRelativeLocation(pos);
        }else {
            FloatPoint pos = FloatPoint.getRandomPointBetween(row2Begin, row2End);
            poop.setScale(pos.getY());
            poop.setRelativeLocation(pos);
        }

        poop.onClick(() -> {
            remove(poop);
            repaint();
            engine.getStateManager().addMoney(5);
            poopCount--;
        });

        poopCount++;
        add(poop);
        repaint();
    }

    private int poopTick = 0;
    @Override
    public void tick(long currentTick) {
        if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
            spawnRandomPoop();
        }

        if (engine.getStateManager().getMoneyCount() >= GAME_SETTINGS.SULFURIC_ACID_COST + GAME_SETTINGS.SMOKE_FLUID_COST) {
            if (!exitOverlay.isVisible()) {
                exitOverlay.setVisible(true);
                engine.getStateManager().setQuest(QuestState.NO_QUEST);
            }
        }

        if (poopTick <= 0) {
            poopTick = (int) (engine.getTickProvider().getTicksPerSecond() * poopDelaySec);
            if (poopCount < maxPoop) {
                spawnRandomPoop();
            }
        }else {
            poopTick--;
        }
    }

    private GameAudioResource.Instance cowAmbient;

    @Override
    public void sceneShown() {
        super.sceneShown();
        cowAmbient = CowMinigameSceneResource.COW_AMBIENT.create().loop(Clip.LOOP_CONTINUOUSLY).play();
        engine.getTickProvider().registerTick(this);
        repaint();
    }

    @Override
    public void sceneHidden() {
        super.sceneHidden();
        engine.getTickProvider().unregisterTick(this);
        if (cowAmbient.isActive()) {
            cowAmbient.stop();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundResource.getResource(), 0, 0, getWidth(), getHeight(), null);
    }
}
