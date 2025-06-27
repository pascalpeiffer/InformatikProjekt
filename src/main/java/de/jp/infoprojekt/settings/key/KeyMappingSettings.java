package de.jp.infoprojekt.settings.key;

import de.jp.infoprojekt.settings.AbstractSettings;
import org.json.JSONObject;

import java.awt.event.KeyEvent;

public class KeyMappingSettings extends AbstractSettings {

    public int FORWARD_KEY = KeyEvent.VK_W;
    public int BACKWARD_KEY = KeyEvent.VK_S;
    public int LEFT_KEY = KeyEvent.VK_A;
    public int RIGHT_KEY = KeyEvent.VK_D;
    public int INTERACT = KeyEvent.VK_E;
    public int SPRINT = KeyEvent.VK_SHIFT;
    public int DIALOG_CONTINUE = KeyEvent.VK_SPACE;

    @Override
    public String getUniqueName() {
        return "KeyMapping";
    }

    @Override
    public JSONObject serialize() {
        JSONObject json = new JSONObject();

        json.put("forward", FORWARD_KEY);
        json.put("backward", BACKWARD_KEY);
        json.put("left", LEFT_KEY);
        json.put("right", RIGHT_KEY);
        json.put("interact", INTERACT);
        json.put("sprint", SPRINT);
        json.put("dialog_continue", DIALOG_CONTINUE);

        return json;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        if (jsonObject.has("forward")) {
            FORWARD_KEY = jsonObject.getInt("forward");
        }
        if (jsonObject.has("backward")) {
            BACKWARD_KEY = jsonObject.getInt("backward");
        }
        if (jsonObject.has("left")) {
            LEFT_KEY = jsonObject.getInt("left");
        }
        if (jsonObject.has("right")) {
            RIGHT_KEY = jsonObject.getInt("right");
        }
        if (jsonObject.has("interact")) {
            INTERACT = jsonObject.getInt("interact");
        }
        if (jsonObject.has("sprint")) {
            SPRINT = jsonObject.getInt("sprint");
        }
        if (jsonObject.has("dialog_continue")) {
            DIALOG_CONTINUE = jsonObject.getInt("dialog_continue");
        }
    }

}
