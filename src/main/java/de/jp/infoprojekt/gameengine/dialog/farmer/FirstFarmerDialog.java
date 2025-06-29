package de.jp.infoprojekt.gameengine.dialog.farmer;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.graphics.dialog.AbstractDialog;
import de.jp.infoprojekt.gameengine.graphics.fade.BlackFade;
import de.jp.infoprojekt.gameengine.scenes.arrest.ArrestScene;
import de.jp.infoprojekt.gameengine.scenes.farmer.CowMinigameScene;
import de.jp.infoprojekt.gameengine.scenes.spawn.SpawnScene;
import de.jp.infoprojekt.gameengine.state.GameState;
import de.jp.infoprojekt.gameengine.state.QuestState;
import de.jp.infoprojekt.resources.scenes.FarmerSceneResource;

public class FirstFarmerDialog extends AbstractDialog {

    private final GameEngine engine;

    public FirstFarmerDialog(GameEngine engine) {
        super(engine);
        this.engine = engine;
    }

    @Override
    public void onDialogShow() {
        continueDialog("Bauer:", "Hallo Stadtmensch, was treibt dich hier soweit aufs Land?", () -> {
            optionsDialog("Was treibt dich soweit aufs Land?", "Ich mische gerade Sprengstoff zusammen und brauche dafür Ammoniak.", "Hauptsächlich die Landluft und meine Zimmerpflanzen. Die sind  am absterben und ich habe gelesen du hättest Dünger?", optionA -> {
                if (optionA) {

                    continueDialog("Bauer:", "Bitte WAS?! Sprengstoff, bist du ein Terrorist?? Ich rufe sofort die Polizei!", () -> {
                        setContinueHintShown(false);
                        setV(false);
                        repaint();
                        FarmerSceneResource.POLIZEI.create().onEnd(() -> {
                            continueDialog("Bauer:", "Ja, hallo Polizei? Ja ich habe hier jemanden der mich nach Zutaten für Sprengstoff fragt. Ja genau, der Müller Hof. Kommen sie schnell bevor er abhaut.",() -> {
                                engine.getGraphics().switchToScene(new ArrestScene(engine, 60, new SpawnScene(engine), new BlackFade(engine)), new BlackFade(engine));
                                dispose();
                            });
                            setV(true);
                            repaint();
                        }).play();

                    });

                }else {

                    continueDialog("Bauer:", "Ja genau ich habe Ammoniak von meiner Kuhgülle übrig. Das entsteht nämlich beim Abbau der Kuhscheiße.", () -> {
                        continueDialog("Eagle:", "Genau das brauche ich. Außerdem bin ich etwas knapp bei Kasse. Brauchst du vielleicht irgendwie Hilfe auf dem Hof?", () -> {
                            continueDialog("Bauer:", "Hmmm, ich glaube ich hätte da etwas bei dem du mir helfen könntest: Jemand müsste mal wieder die Kuhfladen im Stall wegmachen.", () -> {
                                optionsDialog("Kuhfladen im Stall wegmachen?", "Ja das kann ich machen...", "Wirklich? Hast du nichts besseres? Vielleicht etwas was weniger stinkt?", (optionA1) -> {
                                    if (optionA1) {
                                        continueFarmWork();
                                    }else {

                                        continueDialog("Bauer:", "Ne, tut mir leid, dass ist das Einzige was ich dir anbieten könnte... Sicher, dass du das nicht machen willst, ich bezahle dich auch gut.", () -> {
                                            continueDialog("Eagle:", "Ja gut machen wir das...", this::continueFarmWork);
                                        });

                                    }
                                });
                            });
                        });
                    });

                }
            });
        });
    }

    private void continueFarmWork() {
        continueDialog("Bauer:", "Sehr gut hier hast du eine Schaufel. Du kannst von mir aus sofort anfangen...", () -> {
            engine.getGraphics().switchToScene(new CowMinigameScene(engine), new BlackFade(engine));
            engine.getStateManager().setQuest(QuestState.EARN_MONEY);
            engine.getStateManager().setState(GameState.EARNING_MONEY);
        });
    }
}
