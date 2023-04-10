package edu.duke.ece651.team5.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

public class UserGameMapTest {
    @Test
    void testAddGameToUser() {
        UserGameMap userGameMap = new UserGameMap();
        User mockUser = mock(User.class);
        GameController mockGame = mock(GameController.class);
        userGameMap.addGameToUser(mockUser, mockGame);
        for (GameController game : userGameMap.getUserGames(mockUser)) {
            assertEquals(mockGame, game);
        }
    }

    @Test
    void testAddUserToGame() {
        UserGameMap userGameMap = new UserGameMap();
        User mockUser = mock(User.class);
        GameController mockGame = mock(GameController.class);
        userGameMap.addUserToGame(mockGame, mockUser);
        for (User user: userGameMap.getGameUsers(mockGame)) {
            assertEquals(mockUser, user);
        }
    }

    @Test
    void testDeleteMap() {
        UserGameMap userGameMap = new UserGameMap();
        User mockUser = mock(User.class);
        GameController mockGame = mock(GameController.class);
        userGameMap.addGameToUser(mockUser, mockGame);
        userGameMap.addUserToGame(mockGame, mockUser);

        userGameMap.deleteMap(mockUser, mockGame);

        assertEquals(0, userGameMap.getGameUsers(mockGame).size());
        assertEquals(0, userGameMap.getUserGames(mockUser).size());
    }

    @Test
    void testGetUserGamesAndGetGameUsers(){
        UserGameMap userGameMap = new UserGameMap();
        User mockUser = mock(User.class);
        GameController mockGame = mock(GameController.class);
        assertEquals(0, userGameMap.getUserGames(mockUser).size());
        assertEquals(0, userGameMap.getGameUsers(mockGame).size());
        userGameMap.addGameToUser(mockUser, mockGame);
        userGameMap.addUserToGame(mockGame, mockUser);
        assertNotNull(userGameMap.getUserGames(mockUser));
        assertNotNull(userGameMap.getGameUsers(mockGame));
    }

    @Test
    void testCheckUserinGame(){
        UserGameMap userGameMap = new UserGameMap();
        User mockUser = mock(User.class);
        GameController mockGame = mock(GameController.class);
        userGameMap.addGameToUser(mockUser, mockGame);
        userGameMap.addUserToGame(mockGame, mockUser);

        assertTrue(userGameMap.checkUserinGame(mockUser, mockGame));
    }
}
