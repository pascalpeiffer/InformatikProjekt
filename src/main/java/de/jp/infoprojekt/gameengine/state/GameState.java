package de.jp.infoprojekt.gameengine.state;

public enum GameState {

    DEV(-1),
    GAME_ENTRY(1),
    INTRODUCTION_CALLING(2),
    INTRODUCTION_CALL(3),

    MISSION_REFUSED(7), // -> Game END
    MISSION_REFUSED_DOOR_KNOCKED(8), // -> Game END
    GAME_OVER(9),

    GAME_INTRODUCED(10);

    private final int id;

    GameState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
