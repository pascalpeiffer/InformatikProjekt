package de.jp.infoprojekt.gameengine.state;

public enum GameState {

    MENU(0),
    IN_GAME(1),
    PAUSE(2);

    private final int id;

    GameState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
