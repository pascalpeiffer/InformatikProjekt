package de.jp.infoprojekt.settings.graphics;

import de.jp.infoprojekt.settings.AbstractSettings;
import org.json.JSONObject;

import java.awt.*;

/**
 * GraphicSettings class
 *
 * @author Pascal
 * @version 14.06.2025
 */
public class GraphicSettings extends AbstractSettings {

    private WindowTypeSetting WINDOW_SETTING = WindowTypeSetting.WINDOWED_FULLSCREEN;

    private String SELECTED_DISPLAY;

    public GraphicsDevice getSelectedDisplay() {
        GraphicsEnvironment ev = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (GraphicsDevice screenDevice : ev.getScreenDevices()) {
            if (screenDevice.getIDstring().equalsIgnoreCase(SELECTED_DISPLAY)) {
                return screenDevice;
            }
        }
        SELECTED_DISPLAY = ev.getDefaultScreenDevice().getIDstring();
        return ev.getDefaultScreenDevice();
    }

    public void setSelectedDisplay(String displayName) {
        SELECTED_DISPLAY = displayName;
    }

    public WindowTypeSetting getCurrentWindowSetting() {
        return WINDOW_SETTING;
    }

    public void setCurrentWindowSetting(WindowTypeSetting setting) {
        this.WINDOW_SETTING = setting;
    }

    @Override
    public JSONObject serialize() {
        JSONObject json = new JSONObject();

        json.put("WINDOW_SETTING", WINDOW_SETTING.getId());
        json.put("SELECTED_DISPLAY", SELECTED_DISPLAY);

        return json;
    }

    @Override
    public void deserialize(JSONObject json) {

        if (json.has("WINDOW_SETTING")) {
            WINDOW_SETTING = WindowTypeSetting.getByID(json.getInt("WINDOW_SETTING"));
        }

        if (json.has("SELECTED_DISPLAY")) {
            SELECTED_DISPLAY = json.getString("SELECTED_DISPLAY");
        }

    }

    @Override
    public String getUniqueName() {
        return "GraphicSettings";
    }

}
