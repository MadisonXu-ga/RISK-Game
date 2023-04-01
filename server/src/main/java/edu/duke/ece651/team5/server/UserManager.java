package edu.duke.ece651.team5.server;

import java.util.ArrayList;
import java.util.HashMap;

import edu.duke.ece651.team5.server.MyEnum.*;

/**
 * Manage all the users.
 */
public class UserManager {

    private HashMap<String, User> users;

    // abstract UserGameAssociation ---> map user and game
    // HashMap gameid ArrayList<User>
    // HashMap user -> game

    public UserManager() {
        this.users = new HashMap<>();
    }

    public void addUser(String name, String password) {
        users.put(name, new User(name, password));
    }

    public boolean findUser(String name) {
        if (users.containsKey(name)) {
            return true;
        }
        return false;
    }

    public boolean authenticate(String name, String password) {
        if (users.get(name).getUserPassword().equals(password)) {
            return true;
        }
        return false;
    }

    /**
     * Change specific user's status
     * 
     * @param name
     * @param userStatus
     */
    public void changeUserStatus(String name, UserStatus userStatus) {
        users.get(name).setUserStatus(userStatus);
    }
}
