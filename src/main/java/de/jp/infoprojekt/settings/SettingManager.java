package de.jp.infoprojekt.settings;

import com.sun.istack.internal.Nullable;
import de.jp.infoprojekt.io.file.FileManager;
import de.jp.infoprojekt.serializable.Serializable;
import de.jp.infoprojekt.settings.graphics.GraphicSettings;
import de.jp.infoprojekt.settings.key.KeyMappingSettings;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * SettingManager class
 *
 * @author Pascal
 * @version 09.06.2025
 */
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
            return true;
        }
        return false;
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

    @Nullable
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
