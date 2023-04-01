package edu.duke.ece651.team5.server;

import edu.duke.ece651.team5.server.MyEnum.*;

public class GameController {
    private static int nextId = 1;
    private final int id;

    private GameStatus status;
    private 

    public GameController() {
        this.id = nextId;
        ++nextId;

        this.status = GameStatus.INITIALIZED;
    }

    public int getID() {
        return id;
    }
}
