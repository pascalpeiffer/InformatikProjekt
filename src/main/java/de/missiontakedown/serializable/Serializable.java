package de.missiontakedown.serializable;

import org.json.JSONObject;

/**
 * @author Pascal
 */
public interface Serializable {

    JSONObject serialize();

    void deserialize(JSONObject jsonObject);

}
