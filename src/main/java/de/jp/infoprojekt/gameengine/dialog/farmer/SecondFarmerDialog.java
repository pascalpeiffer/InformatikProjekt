package de.jp.infoprojekt.gameengine.dialog.farmer;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.graphics.dialog.AbstractDialog;
import de.jp.infoprojekt.gameengine.graphics.fade.BlackFade;
import de.jp.infoprojekt.gameengine.inventory.Item;
import de.jp.infoprojekt.gameengine.scenes.spawn.SpawnScene;
import de.jp.infoprojekt.gameengine.scenes.travel.TravelScene;

public class SecondFarmerDialog extends AbstractDialog {

    private final GameEngine engine;

    public SecondFarmerDialog(GameEngine engine) {
        super(engine);
        this.engine = engine;
    }

    @Override
    public void onDialogShow() {
        continueDialog("Bauer:", " Ah, du bist fertig?", () -> {
            continueDialog("Eagle:", "Ja genau, war auch gar nicht so schlimm wie gedacht. Hast du meinen Amoniak?", () -> {
                continueDialog("Bauer:", "Hui, da hat es aber wer eilig... Aber ja hier ist er.", () -> {
                    //Give player amoniak
                    engine.getInventoryManager().addItem(new Item(Item.Type.Amoniak));

                    optionsDialog("", "Sich verabschieden und gehen.", "Wortlos einfach gehen (ich habe keine Zeit für dumme Gespräche)", optionA -> {
                        if (optionA) {
                            continueDialog("Eagle:", "Ah, vielen Dank! Ich muss dann aber auch los. Meine Zimmerpflanzen warten...", () -> {
                                continueDialog("Bauer:", "Ja klar, auf wiedersehen!", this::travelBack);
                            });
                        }else {
                            travelBack();
                        }
                    });
                });
            });
        });
    }

    private void travelBack() {
        engine.getGraphics().switchToScene(new TravelScene(engine, false, 5, () -> {
            engine.getGraphics().switchToScene(new SpawnScene(engine), new BlackFade(engine));
        }), new BlackFade(engine));
    }
}
