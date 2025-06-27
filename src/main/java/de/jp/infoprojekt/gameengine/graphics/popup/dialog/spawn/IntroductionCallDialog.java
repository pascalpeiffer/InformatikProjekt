package de.jp.infoprojekt.gameengine.graphics.popup.dialog.spawn;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.graphics.popup.AbstractDialog;
import de.jp.infoprojekt.gameengine.scenes.spawn.SpawnScene;
import de.jp.infoprojekt.gameengine.state.GameState;
import de.jp.infoprojekt.gameengine.tick.GameTick;
import de.jp.infoprojekt.resources.scenes.SpawnSceneResource;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class IntroductionCallDialog extends AbstractDialog implements GameTick {

    private final GameEngine engine;

    private int currentMessage = 0;

    private SpawnScene spawnScene;

    public IntroductionCallDialog(GameEngine engine) {
        super(engine);
        this.engine = engine;

        spawnScene = ((SpawnScene) engine.getGraphics().getCurrentScene());

        setOptionAButtonText(KeyEvent.getKeyText(engine.getKeyMappingSettings().LEFT_KEY));
        setOptionBButtonText(KeyEvent.getKeyText(engine.getKeyMappingSettings().RIGHT_KEY));

        engine.getTickProvider().registerTick(this);
    }

    public void tick(long currentTick) {
        if (engine.getDialogManager().getCurrentDialog() != this) {
            return;
        }

        if (isFullyShown() && !isShowOptions()) {
            setContinueHintShown(true);
        }

        if (engine.getGameKeyHandler().isKeyDown(KeyEvent.VK_SPACE) && isFullyShown() && !isShowOptions()) {
            if (currentMessage < 3 || currentMessage == 4 || currentMessage == 8 || currentMessage == 9 || currentMessage == 11) {
                playMessage(currentMessage + 1);
            }else if (currentMessage == 5 || currentMessage == 10) {
                engine.getStateManager().setState(GameState.GAME_INTRODUCED);
                exitDialog();
            }else if (currentMessage == 6) {
                //give player akte
                playMessage(currentMessage + 1);
            }else if (currentMessage == 12) {
                engine.getStateManager().setState(GameState.MISSION_REFUSED);
                exitDialog();
            }
        }

        if (isShowOptions() && currentMessage == 3) {

            if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().LEFT_KEY)) {
                playMessage(6);
            }else if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().RIGHT_KEY)) {
                playMessage(4);
            }

        }else if (isShowOptions() && currentMessage == 7) {

            if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().LEFT_KEY)) {
                playMessage(8);
            }else if (engine.getGameKeyHandler().isKeyDown(engine.getKeyMappingSettings().RIGHT_KEY)) {
                playMessage(11);
            }

        }
    }

    private void exitDialog() {
        SwingUtilities.invokeLater(() -> {
            engine.getDialogManager().unsetDialog();
            spawnScene.getPlayer().setMoveable(true);
            SpawnSceneResource.PHONE_HANG_UP.play(0.5f);
            engine.getTickProvider().unregisterTick(this);
        });
    }

    public void playMessage(int index) {
        currentMessage = index;
        if (index == 0) {
            setTitle("Mr. G:");
            setDialog("Hallo Eagle! Ich habe einen Auftrag für dich. Er ist etwas riskanter, aber ich glaube für dich sollte er kein Problem darstellen.");
        }else if (index == 1) {
            setTitle("Eagle:");
            setDialog("Hört sich interessant an, worum geht es denn?");
        }else if (index == 2) {
            setTitle("Mr. G:");
            setDialog("Du musst deine chemischen Kenntnisse einsetzen, um ein Strategisch wichiges Ziel auszuschalten: Die Führungsstelle der repressiven Regierung. Wenn du es schaffst, werden wir die möglichkeit haben die Unterdrückung zu beenden.");
        }else if (index == 3) {

            setTitle("Mr. G: Nimmst du den Auftrag an Eagle?");
            setShowOptions(true);
            setDialog("");
            setOptionAText("Mr. G das sind mir zu wenige Informationen, ich werde erst bestätigen wenn ich mehr weiß.");
            setOptionBText("Ja ich nehme den Auftrag an.");

        }else if (index == 4) {
            setTitle("Mr. G:");
            setShowOptions(false);
            setDialog("Sehr schön du weißt ja wie das läuft: Du darfst kein Aufsehen erregen. Und vergiss nicht dass das Ziel ist das Gebäude zu zerstören und niemanden zu verletzen. Hier ist noch die Akte der Mission:");
            spawnScene.getMissionTakeDownFile().setVisible(true);
        }else if (index == 5) {
            setTitle("Eagle:");
            setDialog("Danke Mr. G Ich werde mich melden, wenn die Mission erfüllt ist. Eagle out.");
            spawnScene.getMissionTakeDownFile().setVisible(false);
        }else if (index == 6) {
            setTitle("Mr. G:");
            setShowOptions(false);
            setDialog("Ich verstehe, hier ist die Akte der Mission:");
            spawnScene.getMissionTakeDownFile().setVisible(true);
        }else if (index == 7) {
            spawnScene.getMissionTakeDownFile().setVisible(false);
            setTitle("Eagle:");
            setShowOptions(true);
            setDialog("");
            setOptionAText("Ich sehe, warum wir die Regierung ausschalten müssen. Mr. G ich nehme die Mission an.");
            setOptionBText("Nein, dass kann ich nicht tun. Es ist zu gefährlich für mich.");

        }else if (index == 8) {
            setTitle("Mr. G:");
            setShowOptions(false);
            setDialog("Sehr Gut Eagle. Ich erwarte die Bericht nach abschbuss der Mission. Und bedenke, die Mission darf nicht am Tag durchgeführt werden.");
        }else if (index == 9) {
            setTitle("Eagle:");
            setDialog("Ich verstehe, ich melde mich.");
        }else if (index == 10) {
            setTitle("Mr. G:");
            setDialog("G out.");
        }else if (index == 11) {
            setShowOptions(false);
            setTitle("Eagle:");
            setDialog("Nein, dass kann ich nicht tun. Es ist zu gefährlich für mich. Ich glaube, ich werde mich permanent zur Ruhe setzen.");
        }else if (index == 12) {
            setTitle("Mr. G:");
            setDialog("Ich verstehe. Eagle es war eine Ehre mit dir zu arbeiten. Trotzdem verfügst du leider über zu viele Informationen von vergangenen Missionen, dass wir dich einfach aufhören lassen können. Wie gesagt es war mir eine Ehre.");
        }
        show(40);
    }

}
