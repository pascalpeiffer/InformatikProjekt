package de.missiontakedown.settings.graphics;

public enum WindowTypeSetting {

    FULLSCREEN (0, "Vollbild"),
    WINDOWED_FULLSCREEN (1, "Vollbild im Fenster"),
    WINDOWED (2, "Fenstermodus");

    private final int id;
    private final String friendlyName;

    WindowTypeSetting(int id, String friendlyName) {
        this.id = id;
        this.friendlyName = friendlyName;
    }

    public int getId() {
        return id;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public static WindowTypeSetting getByID(int id) {
        for (WindowTypeSetting value : WindowTypeSetting.values()) {
            if (value.getId() == id) {
                return value;
            }
        }
        return FULLSCREEN;
    }

    public static WindowTypeSetting getByFriendlyName(String name) {
        for (WindowTypeSetting value : WindowTypeSetting.values()) {
            if (value.getFriendlyName().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return FULLSCREEN;
    }

}
