package edu.duke.ece651.team5.server;

import java.util.Objects;

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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass())
            return false;

        User user = (User) object;
        return Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
