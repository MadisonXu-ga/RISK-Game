package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
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
        assertEquals("red", oneMap.getPlayerByName("red").getName());
        assertThrows(IllegalArgumentException.class, () -> oneMap.getPlayerByName("rex"));
    }

    @Test
    void testInitPlayer(){
        Player redPlayer = new Player("red");
        Player bluePlayer = new Player("blue");

        ArrayList<Player> playersArray = new ArrayList<>();
        playersArray.add(bluePlayer);
        playersArray.add(redPlayer);
        RISKMap oneMap = new RISKMap();
        oneMap.initPlayers(playersArray);
        assertEquals(playersArray, oneMap.getPlayers());

    }

    @Test
    void testGetTerritories(){
        RISKMap map = new RISKMap();
        RISKMap map2 = new RISKMap();
        assertEquals(map2.getTerritories(), map.getTerritories());
    }

    @Test
    void testHasPathWithSameOwner() {
        Player blue = new Player("Blue");
        Player green = new Player("green");
        RISKMap map = new RISKMap("test_map_config.txt");
        ArrayList<Player> players = new ArrayList<>();
        players.add(blue);
        players.add(green);
        map.initPlayers(players);
        ArrayList<Territory> territories = map.getTerritories();
        for (int i = 0; i < territories.size(); i++) {
            Territory territory = territories.get(i);
            territory.setOwner(blue);
            blue.addTerritory(territory);
        }
        for (int i = territories.size() / 2; i < territories.size(); i++) {
            Territory territory = territories.get(i);
            territory.setOwner(green);
            green.addTerritory(territory);
        }
        Territory narnia = map.getTerritoryByName("Narnia");
        Territory elantris = map.getTerritoryByName("Elantris");
        boolean actual = map.hasPathWithSameOwner(narnia, elantris);
        assertTrue(actual);

        Territory roshar = map.getTerritoryByName("Roshar");
        assertFalse(map.hasPathWithSameOwner(narnia, roshar));

    }
}