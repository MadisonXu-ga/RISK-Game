package edu.duke.ece651.team5.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import edu.duke.ece651.team5.shared.MyName;

public class PlayerTest {
 
    @Test
    void testNewPlayer(){

        Player redPlayer = new Player("red");

        Territory oneTerritory = new Territory("Narnia", new HashMap<>());

        redPlayer.addTerritory(oneTerritory);

        //check for name
        assertEquals(redPlayer.getName(), "red");
        
        //check that the territory is added to the player
        assertTrue(redPlayer.getTerritories().contains(oneTerritory));

    }

    @Test
    void getAvailableUnit(){
        Player redPlayer = new Player("red");
        assertEquals(50, redPlayer.getAvailableUnit());
    }

    @Test
    void testEqualPlayers(){
        Player p1 = new Player("red");
        Player p2 = new Player("red");
        Player p3 = new Player(null);
        assertTrue(p1.equals(p2));
        assertFalse(p1.equals(p3));
        assertFalse(p1.equals(null));
        assertEquals(p1.hashCode(), p2.hashCode());
        assertEquals(0, p3.hashCode());

    }

}
