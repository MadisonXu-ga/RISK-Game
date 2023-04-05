package edu.duke.ece651.team5.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.server.MyEnum.GameStatus;
import edu.duke.ece651.team5.shared.Player;

public class GameControllerTest {
    @Test
    void testCreatePlayers() {
        // call create players inside it
        GameController gameController = new GameController(3);
        ArrayList<Player> players = gameController.getPlayers();
        assertEquals(3, players.size());
        assertEquals("Green", players.get(0).getName());
        assertEquals("Blue", players.get(1).getName());
        assertEquals("Red", players.get(2).getName());
    }

    @Test
    void testGetID() {
        GameController game1 = new GameController(2);
        GameController game2 = new GameController(3);

        assertEquals(1, game1.getID());
        assertEquals(2, game2.getID());
    }

    @Test
    void testGetStatus() {
        GameController gameController = new GameController(3);
        assertEquals(GameStatus.WAITING, gameController.getStatus());
    }

    @Test
    void testJoinGame() {
        GameController gameController = new GameController(3);
        User user1 = mock(User.class);
        User user2 = mock(User.class);
        User user3 = mock(User.class);
        User user4 = mock(User.class);
        String msg1 = gameController.joinGame(user1);
        String msg2 = gameController.joinGame(user2);
        String msg3 = gameController.joinGame(user3);
        String msg4 = gameController.joinGame(user4);

        assertNull(msg1);
        assertNull(msg2);
        assertNull(msg3);
        assertEquals("Full", msg4);
    }

    @Test
    void testKickUserOut() {
        GameController gameController = new GameController(3);
        User user1 = mock(User.class);
        User user2 = mock(User.class);
        User user3 = mock(User.class);

        gameController.joinGame(user1);
        gameController.joinGame(user2);
        gameController.joinGame(user3);

        assertTrue(gameController.getPlayerToUserMap().containsKey("Blue"));
        gameController.kickUserOut(user2);
        assertFalse(gameController.getPlayerToUserMap().containsKey("Blue"));
    }
}
