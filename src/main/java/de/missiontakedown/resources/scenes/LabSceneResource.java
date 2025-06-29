package de.missiontakedown.resources.scenes;

import de.missiontakedown.resources.GameAudioResource;
import de.missiontakedown.resources.GameResource;

/**
 * @author Jan
 */
public class LabSceneResource {

    public static final GameResource BACKGROUND = new GameResource("/scenes/lab/LaboratoryDefault.png");
    public static final GameResource BACKGROUND_ZOOMED = new GameResource("/scenes/lab/LaboratoryZoomed.jpg");
    public static final GameResource BACKGROUND_EXPLODED = new GameResource("/scenes/lab/LabExploded.png");
    public static final GameResource PLAYER_SPACE = new GameResource("/scenes/lab/LabMapping.png");

    public static final GameResource GAUGE1 = new GameResource("/scenes/lab/Gauge1.png", 0.27f, 0.3f);
    public static final GameResource GAUGE2 = new GameResource("/scenes/lab/Gauge2.png");
    public static final GameResource KNOB = new GameResource("/scenes/lab/Knob.png");
    public static final GameResource TEMP_GAUGE = new GameResource("/scenes/lab/TemperaturGuage.png", 0.4f);
    public static final GameResource ARROW_UP = new GameResource("/scenes/lab/ArrowUp.png", 0.2f);
    public static final GameResource ARROW_DOWN = new GameResource("/scenes/lab/ArrowDown.png", 0.2f);
    public static final GameResource EMPTY_TEMPLATE = new GameResource("/scenes/lab/EmptyTemplate.png", 0.2f);
    public static final GameResource COOL_BUTTON = new GameResource("/scenes/lab/CoolButton.png", 0.2f);

    public static final GameResource SWITCH_ON = new GameResource("/scenes/lab/SwitchON.png", 0.2f);
    public static final GameResource SWITCH_OFF = new GameResource("/scenes/lab/SwitchOFF.png", 0.2f);

    public static final GameAudioResource DISTILLATION = new GameAudioResource("/scenes/lab/DistillationBlubbern.wav", 0.6f);
    public static final GameAudioResource DONE_DING = new GameAudioResource("/scenes/lab/LabDoneDing.wav");
    public static final GameAudioResource ELECTROLYSIS = new GameAudioResource("/scenes/lab/LabElectrolysis.wav");
    public static final GameAudioResource NITRIC_ACID_GEN_FILL = new GameAudioResource("/scenes/lab/NitricAcidGenFillUp.wav");
    public static final GameAudioResource NITRIC_ACID_RUNNING = new GameAudioResource("/scenes/lab/LabNitricAcidRunning.wav");
    public static final GameAudioResource PICKING_UP_OBJECT = new GameAudioResource("/scenes/lab/PickingUpObject.wav");
    public static final GameAudioResource PLACING_OBJECT_TABLE = new GameAudioResource("/scenes/lab/PlacingObjectTable.wav");
    public static final GameAudioResource PLAYER_STEPPING = new GameAudioResource("/scenes/lab/SteppingLaboratory.wav", 0.15f);
    public static final GameAudioResource DROP = new GameAudioResource("/scenes/lab/Drop.wav", 0.5f);
    public static final GameAudioResource LAP_EXPLOSION = new GameAudioResource("/scenes/lab/LabExplosion.wav", 0.5f);

}
