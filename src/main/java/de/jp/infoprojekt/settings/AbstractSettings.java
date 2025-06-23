package de.jp.infoprojekt.settings;

import de.jp.infoprojekt.serializable.Serializable;

/**
 * AbstractSettings class
 *
 * @author Pascal
 * @version 14.06.2025
 */
public abstract class AbstractSettings implements Serializable {

    public abstract String getUniqueName();

}
