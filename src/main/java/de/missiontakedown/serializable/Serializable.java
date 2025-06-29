package de.missiontakedown.serializable;

import org.json.JSONObject;

public interface Serializable {

    JSONObject serialize();

    void deserialize(JSONObject jsonObject);

}
