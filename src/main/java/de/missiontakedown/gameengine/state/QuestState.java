package de.missiontakedown.gameengine.state;

public enum QuestState {

    NO_QUEST(""),
    USE_COMPUTER("Benutze den Computer"),

    CREATE_NITRIC_ACID("Stelle Salpetersäure her"),

    BUY_SULFURIC_ACID("Bestelle Schwefelsäure"),

    GO_TO_FARMER("Fahre zum Bauernhof um Ammoniak zu besorgen"),
    EARN_MONEY("Spare auf mindestens 520 Euro"),

    CREATE_OXYGEN("Stelle Sauerstoff her (Im Labor)"),
    GET_ELECTROLYSIS("Nimm das Elektrolysegerät aus dem Regal"),
    PLACE_ELECTROLYSIS("Platziere das Elektrolysegerät auf der Werkbank"),

    GET_SALPTERGEN("Finde den Salpetersäuregenerator und stelle ihn auf die Werkbank"),
    PLACE_SALPTERGEN("Platziere das Salpetersäuregenerator auf der Werkbank"),

    NEED_COLA("Ich brauche eine Cola!"),

    BUY_FOG_FLUID("Bestelle das Nebelfluid"),
    BOUGHT_FOG_FLUID("Nimm das Nebelfluid und destilliere es im Labor"),
    GET_DESTILLATION_DEVICE("Finde die Destille und Platziere sie auf der Werkbank"),
    PLACE_DESTILLATION_DEVICE("Destille Platzieren und Nebelfluid reingießen"),
    GET_NITRATOR("Finde den Nitrator und platziere ihn auf der Werkbank"),
    PLACE_NITRATOR("Platziere den Nitrator auf der Werkbank"),

    DRINK_COLA("Trinke noch eine Cola und lege dich hin"),
    GO_TO_REST("Lege dich hin"),

    PLACE_BOMB("Fahre zur Führungsstelle und platziere die Bombe"),
    DETONATE_BOMB("Zünde die Bombe"),
    CALL_G("Rufe den Boss an");

    private final String questText;

    QuestState(String questText) {
        this.questText = questText;
    }

    public String getQuestText() {
        return questText;
    }
}
