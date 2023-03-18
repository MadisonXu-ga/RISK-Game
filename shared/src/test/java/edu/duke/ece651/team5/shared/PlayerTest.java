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
        assertTrue(redPlayer.displayTerritories().contains(oneTerritory));
        
    }

}
