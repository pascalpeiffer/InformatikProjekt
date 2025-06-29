package de.missiontakedown.settings;

import de.missiontakedown.serializable.Serializable;

/**
 * @author Pascal
 */
public abstract class AbstractSettings implements Serializable {

    public abstract String getUniqueName();

}
