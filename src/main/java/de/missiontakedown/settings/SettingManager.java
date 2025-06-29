package de.missiontakedown.settings;

import de.missiontakedown.io.file.FileManager;
import de.missiontakedown.serializable.Serializable;
import de.missiontakedown.settings.graphics.GraphicSettings;
import de.missiontakedown.settings.key.KeyMappingSettings;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class SettingManager implements Serializable {

    private static SettingManager instance;

    private final ArrayList<AbstractSettings> settings = new ArrayList<>();

    private SettingManager() {
        addSettings();
    }

    private void addSettings() {
        settings.add(new GraphicSettings());
        settings.add(new KeyMappingSettings());
    }

    public boolean loadSettingsFromFile() {
        try {
            FileManager.deserializeFromFile(SettingManager.getInstance(), "./settings.conf");
        } catch (IOException | JSONException exception) {
            System.err.println("Failed to load settings config!");
            return false;
        }
        return true;
    }

    public boolean saveSettingsToFile() {
        try {
            FileManager.serializeToFile(SettingManager.getInstance(), "./settings.conf");
        } catch (IOException e) {
            System.err.println("Failed to save Settings!");
            return false;
        }
        return true;
    }

    public JSONObject serialize() {
        JSONObject json = new JSONObject();

        for (AbstractSettings setting : settings) {
            json.put(setting.getUniqueName(), setting.serialize());
        }

        return json;
    }

    public void deserialize(JSONObject json) {
        for (AbstractSettings setting : settings) {
            if (json.has(setting.getUniqueName())) {
                setting.deserialize(json.getJSONObject(setting.getUniqueName()));
            }
        }
    }

    public AbstractSettings getSetting(Class<?> clazz) {
        for (AbstractSettings setting : settings) {
            if (setting.getClass().equals(clazz)) {
                return setting;
            }
        }

        return null;
    }

    public static SettingManager getInstance() {
        if (instance == null) {
            instance = new SettingManager();
        }
        return instance;
    }
}
