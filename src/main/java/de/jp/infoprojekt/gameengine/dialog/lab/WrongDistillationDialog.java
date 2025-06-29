package de.jp.infoprojekt.gameengine.dialog.lab;

import de.jp.infoprojekt.gameengine.GameEngine;
import de.jp.infoprojekt.gameengine.gameobjects.overlay.DistillationOverlay;
import de.jp.infoprojekt.gameengine.graphics.dialog.AbstractDialog;

public class WrongDistillationDialog extends AbstractDialog {

    private final GameEngine engine;

    private final DistillationOverlay distillationSetting;

    public WrongDistillationDialog(GameEngine engine, DistillationOverlay distillationSetting) {
        super(engine);
        this.engine = engine;
        this.distillationSetting = distillationSetting;
    }

    @Override
    public void onDialogShow() {
        if (distillationSetting.getTempIndex() == 0) {
            continueDialog("Eagle:", "Verdammt, das Produkt verbrennt, ich muss eine andere Temperatur probieren.", () -> {
                distillationSetting.setToggleSwitch(false);
                distillationSetting.setChangeable(true);
                dispose();
            });
        }else if (distillationSetting.getTempIndex() == 2) {
            continueDialog("Eagle:", "Verdammt das Produkt ist unrein, ich muss eine andere Temperatur probieren.", () -> {
                distillationSetting.setToggleSwitch(false);
                distillationSetting.setChangeable(true);
                dispose();
            });
        }
    }
}
