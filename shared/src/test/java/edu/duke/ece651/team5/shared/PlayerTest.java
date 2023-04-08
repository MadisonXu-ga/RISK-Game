package edu.duke.ece651.team5.shared;


import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.allResource.Resource;
import edu.duke.ece651.team5.shared.allResource.ResourceType;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.Territory;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void testConsumeResource() {
        Player p = new Player("Alice");
        p.addResourceFromTerritory(new Resource(ResourceType.FOOD), 10);
        p.consumeResource(new Resource(ResourceType.FOOD), 5);
        assertEquals(5, p.getResourceCount(new Resource(ResourceType.FOOD)));
        p.consumeResource(new Resource(ResourceType.FOOD), 7);
        assertEquals(0, p.getResourceCount(new Resource(ResourceType.FOOD)));
    }


    @Test
    public void testAddResourceFromTerritory() {
        Player p = new Player("Alice");
        p.addResourceFromTerritory(new Resource(ResourceType.FOOD), 5);
        assertEquals(5, p.getResourceCount(new Resource(ResourceType.FOOD)));
        p.addResourceFromTerritory(new Resource(ResourceType.FOOD), 3);
        assertEquals(8, p.getResourceCount(new Resource(ResourceType.FOOD)));
        p.addResourceFromTerritory(new Resource(ResourceType.TECHNOLOGY), 3);
        Map<Resource, Integer> expected = new HashMap<>();
        expected.put(new Resource(ResourceType.FOOD), 8);
        expected.put(new Resource(ResourceType.TECHNOLOGY), 3);
        assertEquals(expected, p.getResourceToAmount());
        
    }


    @Test
    public void testUpgradeTechnologyLevel() {
        Player p = new Player("Alice");
        assertEquals(0, p.getCurrTechnologyLevel());
        p.upgradeTechnologyLevel();
        assertEquals(1, p.getCurrTechnologyLevel());
        p.upgradeTechnologyLevel();
        assertEquals(2, p.getCurrTechnologyLevel());
    }

    @Test
    public void testEqualsAndHashCode() {
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        // Test equals() method
        Player player1Copy = new Player("Player 1");
        Player player1Different = new Player("Player 3");
        Player nullPlayer = null;
        String stringObject = "Not a Player object";

        assertEquals(player1, player1); 
        assertEquals(player1, player1Copy); 
        assertNotEquals(player1, player2); 
        assertNotEquals(player1, player1Different); 
        assertNotEquals(player1, nullPlayer); 
        assertNotEquals(player1, stringObject); 

        // Test hashCode() method
        assertEquals(player1.hashCode(), player1Copy.hashCode()); 
        assertNotEquals(player2.hashCode(), player1.hashCode());
    }

}

