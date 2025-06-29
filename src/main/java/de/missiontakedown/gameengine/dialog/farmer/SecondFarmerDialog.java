package de.missiontakedown.gameengine.dialog.farmer;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.graphics.dialog.AbstractDialog;
import de.missiontakedown.gameengine.graphics.fade.BlackFade;
import de.missiontakedown.gameengine.inventory.Item;
import de.missiontakedown.gameengine.scenes.spawn.SpawnScene;
import de.missiontakedown.gameengine.scenes.travel.TravelScene;

/**
 * @author Jan
 */
public class SecondFarmerDialog extends AbstractDialog {

    private final GameEngine engine;

    public SecondFarmerDialog(GameEngine engine) {
        super(engine);
        this.engine = engine;
    }

    @Override
    public void onDialogShow() {
        continueDialog("Bauer:", " Ah, du bist fertig?", () -> {
            continueDialog("Eagle:", "Ja genau, war auch gar nicht so schlimm wie gedacht. Hast du meinen Ammoniak?", () -> {
                continueDialog("Bauer:", "Hui, da hat es aber wer eilig... Aber ja hier ist er.", () -> {
                    //Give player amoniak
                    engine.getInventoryManager().addItem(new Item(Item.Type.Amoniak));

                    optionsDialog("", "Sich verabschieden und gehen.", "Wortlos einfach gehen (ich habe keine Zeit für dumme Gespräche)", optionA -> {
                        if (optionA) {
                            continueDialog("Eagle:", "Ah, vielen Dank! Ich muss dann aber auch los. Meine Zimmerpflanzen warten...", () -> {
                                continueDialog("Bauer:", "Ja klar, auf Wiedersehen!", this::travelBack);
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
