package edu.duke.ece651.team5.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    }

    @Test
    void testKickUserOut() {

    }
}
