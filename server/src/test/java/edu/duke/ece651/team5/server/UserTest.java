package edu.duke.ece651.team5.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.server.MyEnum.UserStatus;

public class UserTest {
    @Test
    void testGetUserName() {
        User user = new User("user1", "abc123");
        assertEquals("user1", user.getUserName());
    }

    @Test
    void testGetUserPassword() {
        User user = new User("user1", "abc123");
        assertEquals("abc123", user.getUserPassword());
    }

    @Test
    void testGetUserStatus() {
        User user = new User("user1", "abc123");
        assertEquals(UserStatus.LOGGED_OUT, user.getUserStatus());
    }

    @Test
    void testSetUserStatus() {
        User user = new User("user1", "abc123");
        user.setUserStatus(UserStatus.LOGGED_OUT);
        assertEquals(UserStatus.LOGGED_OUT, user.getUserStatus());
    }
}
