package de.missiontakedown.gameengine.scenes.headquarter;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.dialog.headquarter.BombPlacedDialog;
import de.missiontakedown.gameengine.gameobjects.headquarter.ColaBomb;
import de.missiontakedown.gameengine.gameobjects.interaction.InteractionHint;
import de.missiontakedown.gameengine.gameobjects.overlay.*;
import de.missiontakedown.gameengine.gameobjects.player.PlayerCharacter;
import de.missiontakedown.gameengine.graphics.fade.BlackFade;
import de.missiontakedown.gameengine.inventory.Item;
import de.missiontakedown.gameengine.scenes.AbstractScene;
import de.missiontakedown.gameengine.scenes.spawn.SpawnScene;
import de.missiontakedown.gameengine.scenes.travel.TravelScene;
import de.missiontakedown.gameengine.state.GameState;
import de.missiontakedown.gameengine.tick.GameTick;
import de.missiontakedown.resources.GameAudioResource;
import de.missiontakedown.resources.GameResource;
import de.missiontakedown.resources.ResourceManager;
import de.missiontakedown.resources.ScalingEvent;
import de.missiontakedown.resources.scenes.HeadquarterSceneResource;
import de.missiontakedown.resources.scenes.LabSceneResource;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.KeyEvent;

public class HeadquarterScene extends AbstractScene implements ScalingEvent, GameTick {

    private final GameEngine engine;

    private GameResource backgroundResource = HeadquarterSceneResource.BACKGROUND;

    private PlayerCharacter player;

    private GameAudioResource.Instance ambientSound;

    private InteractionHint bombPlaceInteractionHint;

    private ColaBomb colaBomb;

    private DetonatorOverlay detonatorOverlay;

    private TextOverlay exitOverlay;

    private final MoneyOverlay moneyOverlay;
    private final QuestOverlay questOverlay;
    private final InventoryOverlay inventoryOverlay;

    public HeadquarterScene(GameEngine engine) {
        this.engine = engine;
        setLayout(null);

        ResourceManager.addScalingListener(this);

        initDetonatorOverlay();
        initExitOverlay();

        initBombPlaceInteractionHint();

        moneyOverlay = new MoneyOverlay(engine);
        add(moneyOverlay);

        questOverlay = new QuestOverlay(engine);
        add(questOverlay);

        inventoryOverlay = new InventoryOverlay(engine);
        add(inventoryOverlay);

        initPlayer();

        initColaBomb();
    }

    private void initExitOverlay() {
        exitOverlay = new TextOverlay("Verlassen");
        exitOverlay.setRelativeLocation(0.8f, 0.9f);
        exitOverlay.setVisible(false);
        add(exitOverlay);

        exitOverlay.onClick(() -> {
            //GOTO Spawn
            engine.getGraphics().switchToScene(new TravelScene(engine, false, 5, () -> {
                engine.getGraphics().switchToScene(new SpawnScene(engine), new BlackFade(engine));
            }).setNight(true), new BlackFade(engine));
        });
    }

    private void initDetonatorOverlay() {
        detonatorOverlay = new DetonatorOverlay(engine);
        detonatorOverlay.setVisible(false);
        add(detonatorOverlay);
    }

    private void initColaBomb() {
        colaBomb = new ColaBomb();
        colaBomb.setRelativeLocation(0.47f, 0.71f);
        colaBomb.setVisible(false);
        add(colaBomb);
    }

    private void initBombPlaceInteractionHint() {
        bombPlaceInteractionHint = new InteractionHint("Bombe platzieren" +" (" + KeyEvent.getKeyText(engine.getKeyMappingSettings().INTERACT) + ")");
        bombPlaceInteractionHint.setVisible(false);
        add(bombPlaceInteractionHint);
    }

    private void initPlayer() {
        player = new PlayerCharacter(engine);
        player.setRelativeLocation(0.5f, 0.9f);
        player.setMoveable(true);
        player.setBlockingArea(HeadquarterSceneResource.PLAYER_MAP);
        player.setPlayerSteppingSound(LabSceneResource.PLAYER_STEPPING);
        player.setScaling(0.6f);
        player.setPlayerScalingConstant(0.1f);
        player.setNight(true);
        add(player);
    }

    @Override
    public void tick(long currentTick) {
        bombPlaceTick(player.getRGBOnBlockArea() == Color.RED.getRGB());
    }

    private void bombPlaceTick(boolean isPlayerNearby) {
        boolean intHint = false;

        if (isPlayerNearby) {
            if (engine.getStateManager().getState() == GameState.RESTED) {
                intHint = true;
                if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().INTERACT)) {
                    //Place Bomb
                    //TODO sound
                    engine.getInventoryManager().findItemByType(Item.Type.ColaBomb).ifPresent(item -> {
                        engine.getInventoryManager().removeItem(item);
                    });
                    engine.getStateManager().setState(GameState.PLACED_BOMB);
                    colaBomb.setVisible(true);
                    detonatorOverlay.setVisible(true);
                    repaint();

                    BombPlacedDialog dialog = new BombPlacedDialog(engine, this);
                    engine.getDialogManager().setDialog(dialog);
                }
            }
        }

        bombPlaceInteractionHint.setVisible(intHint);
    }

    @Override
    public void sceneShown() {
        super.sceneShown();
        engine.getTickProvider().registerTick(player);
        engine.getTickProvider().registerTick(this);
        repaint();
        ambientSound = HeadquarterSceneResource.AMBIENT.create().loop(Clip.LOOP_CONTINUOUSLY).play();
    }

    @Override
    public void sceneHidden() {
        super.sceneHidden();
        engine.getTickProvider().unregisterTick(player);
        engine.getTickProvider().unregisterTick(this);
        ambientSound.stop();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundResource.getResource(), 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void scale(float width, float height) {
        repaint();
    }

    public PlayerCharacter getPlayer() {
        return player;
    }

    public TextOverlay getExitOverlay() {
        return exitOverlay;
    }

    public void setBackgroundResource(GameResource backgroundResource) {
        this.backgroundResource = backgroundResource;
    }

    public MoneyOverlay getMoneyOverlay() {
        return moneyOverlay;
    }

    public InventoryOverlay getInventoryOverlay() {
        return inventoryOverlay;
    }

    public QuestOverlay getQuestOverlay() {
        return questOverlay;
    }

    public GameAudioResource.Instance getAmbientSound() {
        return ambientSound;
    }

    public ColaBomb getColaBomb() {
        return colaBomb;
    }
}
