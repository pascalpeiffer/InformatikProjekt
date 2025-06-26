package de.jp.infoprojekt.gameengine.graphics.popup.dialog.spawn;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.graphics.popup.AbstractDialog;
import java.awt.*;
import java.awt.event.KeyEvent;

public class PreGameDialog extends AbstractDialog {

    public PreGameDialog(GameEngine engine) {
        super(engine);
        setTitle("Mr. G:");
        setDialog("Hallo §4Eagle§r! Ich habe einen Auftrag für dich. Er ist etwas riskanter, aber ich glaube für dich sollte er kein Problem darstellen.");
        show(40);

        engine.getTickProvider().onTick(() -> {
            if (isFullyShown()) {
                setContinueHintShown(true);
            }

            if (engine.getGameKeyHandler().isKeyDown(KeyEvent.VK_SPACE) && isFullyShown()) {
                setTitle("Eagle:");
                setDialog("Hört sich interessant an, worum geht es denn?");
                show(40);
            }
        });
    }

}
