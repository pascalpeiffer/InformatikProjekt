package de.jp.infoprojekt.gameengine.state;

public enum QuestState {

    NO_QUEST(""),
    USE_COMPUTER("Benutze den Computer"),

    CREATE_NITRIC_ACID("Stelle Salpetersäure her"),

    BUY_SULFURIC_ACID("Bestelle Schwelsäure"),

    GO_TO_FARMER("Fahre zum Bauernhof um Ammoniak zu besorgen");

    private final String questText;

    QuestState(String questText) {
        this.questText = questText;
    }

    public String getQuestText() {
        return questText;
    }
}
