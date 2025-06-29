package de.missiontakedown.gameengine.dialog.lab;

import de.missiontakedown.gameengine.GameEngine;
import de.missiontakedown.gameengine.graphics.dialog.AbstractDialog;
import de.missiontakedown.gameengine.inventory.Item;
import de.missiontakedown.gameengine.scenes.lab.LabScene;
import de.missiontakedown.gameengine.state.GameState;
import de.missiontakedown.gameengine.state.QuestState;

/**
 * @author Jan
 */
public class NitroglycerinInColaDialog extends AbstractDialog {

    private final GameEngine engine;
    private final LabScene labScene;

    public NitroglycerinInColaDialog(GameEngine engine, LabScene labScene) {
        super(engine);
        this.engine = engine;
        this.labScene = labScene;
    }

    @Override
    public void onDialogShow() {
        labScene.getPlayer().setMoveable(false);
        continueDialog("Eagle:", "Yes, ich habe es geschafft. Jetzt muss ich nur noch das Nitroglycerin vom Müll trennen. Aber wo packe ich es danach rein?", () -> {
            optionsDialog("Aber wo packe ich es danach rein?", "Ah, ich hab ja noch die Cola Flasche von vorhin. Die ist doch passend.", "Hmm, ich schau mich mal um.", optionA -> {
                if (optionA) {
                    createNitroglycerinCola();
                }else {
                    continueDialog("Eagle:", "Verdammt, ich sehe nichts anderes als meine Cola Flasche, dann nehme ich die...", this::createNitroglycerinCola);
                }
            });
        });
    }

    private void createNitroglycerinCola() {
        continueDialog("Eagle:", "§o§lFüllt die Cola Flasche...§r", () -> {
            engine.getInventoryManager().findItemByType(Item.Type.ColaEmpty).ifPresent(item -> {
                engine.getInventoryManager().removeItem(item);
            });
            engine.getInventoryManager().addItem(new Item(Item.Type.ColaBomb));
            continueDialog("Eagle:", "Endlich fertig! Jetzt ist es Zeit den Auftrag zu erfüllen..." +
                    " Auf zur Führungsstelle. Aber ich muss ja bis 3:00 nachts warten.", () -> {

                engine.getStateManager().setState(GameState.DRINK_COLA);
                engine.getStateManager().setQuest(QuestState.DRINK_COLA);
                labScene.getPlayer().setMoveable(true);
                dispose();
            });
        });
    }
}
