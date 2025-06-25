package de.jp.infoprojekt.gameengine.graphics.popup.dialog.spawn;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.graphics.popup.AbstractDialog;
import java.awt.*;

public class PreGameDialog extends AbstractDialog {

    public PreGameDialog(GameEngine engine) {
        super(engine);
        setTitle("Mr. G:");
        addDialog("Hallo Eagle! Ich habe einen Auftrag für dich. Er ist etwas riskanter, aber ich glaube für dich sollte er kein Problem darstellen.");
        addDialog("Hört sich interessant an, worum geht es denn?");
        addDialog("Du musst deine Chemischen kenntnisse einsetzen, um ein Strategisch wichiges Ziel auszuschalten: Die Führungsstelle der repressiven Regierung. Wenn du es schaffst, werden wir die möglichkeit haben die Unterdrückung zu beenden.");
        show(50, () -> {
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                proceed("Eagle:");
                show(50, () -> {
                    new Thread(() -> {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        proceed("Mr. G:");
                        show(50);
                    }).start();
                });
            }).start();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
