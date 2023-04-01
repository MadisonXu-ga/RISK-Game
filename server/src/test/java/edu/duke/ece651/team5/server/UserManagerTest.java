package edu.duke.ece651.team5.server;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UserManagerTest {
    @Test
    void testAddUser() {
        UserManager userManager = new UserManager();
        userManager.addUser("user1", "abc123");
        assertTrue(userManager.findUser("user1"));
    }

    @Test
    void testAuthenticate() {
        UserManager userManager = new UserManager();
        userManager.addUser("user1", "abc123");
        boolean correct = userManager.authenticate("user1", "abc123");
        boolean incorrect = userManager.authenticate("user1", "123abc");

        assertTrue(correct);
        assertFalse(incorrect);
    }

    @Test
    void testFindUser() {
        UserManager userManager = new UserManager();
        userManager.addUser("user1", "abc123");
        assertTrue(userManager.findUser("user1"));
        assertFalse(userManager.findUser("user2"));
    }
}
