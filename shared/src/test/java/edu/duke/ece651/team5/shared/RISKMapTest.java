package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class RISKMapTest {

    @Test
    void testGetTerritoryByName() {
        RISKMap map = new RISKMap();
        Territory actual = map.getTerritoryByName("Oz");
        assertEquals("Oz", actual.getName());
        assertThrows(IllegalArgumentException.class, () -> map.getTerritoryByName("non-exist"));

    }

    @Test
    void testIsAdjacent() {
        RISKMap map = new RISKMap();
        Territory narnia = map.getTerritoryByName("Narnia");
        Territory elantris = map.getTerritoryByName("Elantris");
        Territory hogwarts = map.getTerritoryByName("Hogwarts");
        assertTrue(map.isAdjacent(narnia, elantris));
        assertFalse(map.isAdjacent(narnia, hogwarts));
    }

    @Test
    void testGetAdjacentTerritories() {
        RISKMap map = new RISKMap();
        Territory narnia = map.getTerritoryByName("Narnia");
        HashSet<Territory> actual = map.getAdjacentTerritories(narnia);
        HashSet<Territory> expected = new HashSet<>(Arrays.asList(
                map.getTerritoryByName("Elantris"),
                map.getTerritoryByName("Midkemia")
        ));
        assertEquals(expected, actual);
    }


    @Test
    void testGetPlayerByName(){

        Player redPlayer = new Player("red");
        Player bluePlayer = new Player("blue");

        ArrayList<Player> playersArray = new ArrayList<>();
        playersArray.add(bluePlayer);
        playersArray.add(redPlayer);

        RISKMap oneMap = new RISKMap(playersArray);

        assertThrows(IllegalArgumentException.class, () -> oneMap.getTerritoryByName("orange"));


    }
}