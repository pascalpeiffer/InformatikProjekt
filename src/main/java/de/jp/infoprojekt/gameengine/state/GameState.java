package de.jp.infoprojekt.gameengine.state;

public enum GameState {

    PRE_GAME(0),
    TO_LESS_INFO_FOR_MISSION(1),
    MISSION_ACCEPTED(2),
    MISSION_REJECTED(3),
    CHECKED_PC(4),
    NITRO_QUEST(5),

    NITRIC_ACID(6),
    DRIVE_TO_FARM(7),
    AT_FARM(8),
    ARRESTED(9),
    COLLECT_CRAP(10),

    COW_MINIGAME(11),

    HYDROGEN_QUEST(12),
    HYDROGEN_QUEST2(13), //Need it?



    SULFURIC_ACID(1111);

    private final int id;

    GameState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
