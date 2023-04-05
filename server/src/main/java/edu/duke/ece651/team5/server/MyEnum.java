package edu.duke.ece651.team5.server;

public class MyEnum {
    public enum Operation {
        LOGIN, SIGNUP, LOGOUT
    }

    public enum UserStatus {
        LOGGED_IN, LOGGED_OUT
    }

    public enum GameStatus {
        WAITING, INITIALIZING, STARTED, PAUSED, ENDED
    }
}
