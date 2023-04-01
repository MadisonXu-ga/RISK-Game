package edu.duke.ece651.team5.server;

import edu.duke.ece651.team5.server.MyEnum.*;

public class User {
    private String name;
    private String password;
    private UserStatus status;

    public User(String name, String passwrod) {
        this.name = name;
        this.password = passwrod;
        this.status = UserStatus.LOGGED_OUT;
    }

    public String getUserName() {
        return name;
    }

    public String getUserPassword() {
        return password;
    }

    public UserStatus getUserStatus() {
        return status;
    }

    public void setUserStatus(UserStatus newStatus) {
        this.status = newStatus;
    }
}
