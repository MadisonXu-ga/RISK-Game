package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class PlayerTest {

    @Test
    public void testAddTerritory() {
        Player player = new Player("Red");
        Territory territory = new Territory(1, "Territory1", new Player("Blue"));
        player.addTerritory(territory);
        List<Territory> territories = player.getTerritories();
        assertTrue(territories.contains(territory));
    }

    @Test
    public void testGetMaxTechnologyLevel() {
        Player player = new Player("Red");
        int maxTechLevel = 2;
        // player.setMaxTechnologyLevel(maxTechLevel);
        // assertEquals(maxTechLevel, player.getCurrTechnologyLevel());
    }

    @Test
    public void testLoseTerritory() {
        Player player = new Player("Red");
        Territory territory1 = new Territory(1, "Territory1", new Player("Red"));
        Territory territory2 = new Territory(2, "Territory2", new Player("Red"));
        player.addTerritory(territory1);
        player.addTerritory(territory2);
        player.loseTerritory(territory1);
        assertFalse(player.getTerritories().contains(territory1));
        assertTrue(player.getTerritories().contains(territory2));
    }

    @Test
    public void testEquals() {
        Player player1 = new Player("Red");
        Player player2 = new Player("Red");
        Player player3 = new Player("Blue");
        assertTrue(player1.equals(player2));
        assertFalse(player1.equals(player3));
    }

    @Test
    public void testHashCode() {
        Player player1 = new Player("Red");
        Player player2 = new Player("Red");
        assertEquals(player1.hashCode(), player2.hashCode());
    }
}
