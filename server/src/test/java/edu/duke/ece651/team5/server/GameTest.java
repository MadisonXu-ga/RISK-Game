package edu.duke.ece651.team5.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class GameTest {
    @Test
    void testGetID() {
        GameController game1 = new GameController();
        GameController game2 = new GameController();

        assertEquals(1, game1.getID());
        assertEquals(2, game2.getID());
    }
}
