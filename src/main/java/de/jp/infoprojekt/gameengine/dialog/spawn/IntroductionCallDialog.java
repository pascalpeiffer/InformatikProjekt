package de.jp.infoprojekt.gameengine.dialog.spawn;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.graphics.dialog.AbstractDialog;
import de.jp.infoprojekt.gameengine.graphics.dialog.ContinueCallback;
import de.jp.infoprojekt.gameengine.scenes.spawn.SpawnScene;
import de.jp.infoprojekt.gameengine.state.GameState;
import de.jp.infoprojekt.gameengine.state.QuestState;
import de.jp.infoprojekt.resources.interaction.InteractionResource;
import de.jp.infoprojekt.resources.scenes.SpawnSceneResource;

import javax.swing.*;

public class IntroductionCallDialog extends AbstractDialog {

    private final GameEngine engine;
    private final SpawnScene spawnScene;

    public IntroductionCallDialog(GameEngine engine, SpawnScene spawnScene) {
        super(engine);
        this.engine = engine;
        this.spawnScene = spawnScene;
    }

    @Override
    public void onDialogShow() {
        spawnScene.getPlayer().setMoveable(false);
        continueDialog("Mr. G:", "Hallo Eagle! Ich habe einen Auftrag für dich. Er ist etwas riskanter, aber ich glaube für dich sollte er kein Problem darstellen.", () -> {
            continueDialog("Eagle:", "Hört sich interessant an, worum geht es denn?", () -> {
                continueDialog("Mr. G:", "Du musst deine chemische Kenntnisse einsetzen, um ein Strategisch wichtiges Ziel auszuschalten: Die Führungsstelle der repressiven Regierung. Wenn du es schaffst, werden wir die möglichkeit haben die Unterdrückung zu beenden.", () -> {
                    optionsDialog("Mr.G: Nimmst du den Auftrag an Eagle?", "Mr. G das sind mir zu wenige Informationen, ich werde erst bestätigen wenn ich mehr weiß.", "Ja ich nehme den Auftrag an.", optionA -> {
                        if (optionA) {

                            continueDialog("Mr. G:", " Ich verstehe, hier ist die Akte der Mission (TAB):", () -> {
                                missionFileDialog(() -> {
                                    optionsDialog("Eagle:", " Ich sehe, warum wir die Regierung ausschalten müssen. Mr. G ich nehme die Mission an.", "Nein, dass kann ich nicht tun. Es ist zu gefährlich für mich.", optionA1 -> {
                                        if (optionA1) {

                                            continueDialog("Mr. G:" ,"Sehr Gut Eagle. Ich erwarte die Bericht nach abschluss der Mission. Und bedenke, die Mission darf nicht am Tag durchgeführt werden.", () -> {
                                                continueDialog("Eagle:", "Ich verstehe, ich melde mich.", () -> {
                                                    continueDialog("Mr. G:", "G out.", () -> {
                                                        engine.getStateManager().setState(GameState.GAME_INTRODUCED);
                                                        engine.getStateManager().setQuest(QuestState.USE_COMPUTER);
                                                        spawnScene.getMoneyOverlay().setVisible(true);
                                                        exitDialog();
                                                    });
                                                });
                                            });

                                        }else {

                                            continueDialog("Eagle:", "Nein, dass kann ich nicht tun. Es ist zu gefährlich für mich. Ich glaube, ich werde mich permanent zur Ruhe setzen.", () -> {
                                                continueDialog("Mr. G:", "Ich verstehe. Eagle es war eine Ehre mit dir zu arbeiten. Trotzdem verfügst du leider über zu viele Informationen von vergangenen Missionen, dass wir dich einfach aufhören lassen können. Wie gesagt es war mir eine Ehre.", () -> {
                                                    engine.getStateManager().setState(GameState.MISSION_REFUSED);
                                                    exitDialog();
                                                });
                                            });

                                        }
                                    });
                                });
                            });

                        }else {

                            continueDialog("Mr. G:", "Sehr schön du weißt ja wie das läuft: Du darfst kein Aufsehen erregen." +
                                    " Und vergiss nicht dass das Ziel ist das Gebäude zu zerstören und niemanden zu verletzen. Hier ist noch die Akte der Mission (TAB):", () -> {
                                missionFileDialog(() -> {
                                    continueDialog("Eagle:", "Danke Mr. G Ich werde mich melden, wenn die Mission erfüllt ist. Eagle out.", () -> {
                                        engine.getStateManager().setState(GameState.GAME_INTRODUCED);
                                        engine.getStateManager().setQuest(QuestState.USE_COMPUTER);
                                        spawnScene.getMoneyOverlay().setVisible(true);
                                        exitDialog();
                                    });
                                });
                            });

                        }
                    });
                });
            });
        });
    }

    private void exitDialog() {
        SpawnSceneResource.PHONE_HANG_UP.create().play();
        spawnScene.getPlayer().setMoveable(true);
        dispose();
    }

    public void missionFileDialog(ContinueCallback callback) {
        InteractionResource.FILE_INTERACTION.create().play();
        setDrawBaseImage(false);
        spawnScene.getMissionTakedownFile().setVisible(true);
        setAnswerCooldown((int) (engine.getTickProvider().getTicksPerSecond() * 0.5f));
        continueDialog("", "", () -> {
            setDrawBaseImage(true);
            callback.callback();
            SwingUtilities.invokeLater(() -> {
                spawnScene.getMissionTakedownFile().setVisible(false);
            });
        });
    }

}
