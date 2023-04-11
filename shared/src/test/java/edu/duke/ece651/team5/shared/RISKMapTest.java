package edu.duke.ece651.team5.shared;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import edu.duke.ece651.team5.shared.game.RISKMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.Territory;

import static org.junit.jupiter.api.Assertions.*;

public class RISKMapTest {
    private RISKMap map;

    @BeforeEach
    public void setUp() {
        Map<String, Territory> territories = new HashMap<>();
        territories.put("Territory 1", new Territory(1, "Territory 1", new Player("Player 1")));
        territories.put("Territory 2", new Territory(2, "Territory 2", new Player("Player 2")));
        territories.put("Territory 3", new Territory(3, "Territory 3", new Player("Player 2")));
        territories.put("Territory 4", new Territory(4, "Territory 4", new Player("Player 1")));

        HashMap<Integer, List<RISKMap.Edge>> connections = new HashMap<>();
        connections.put(1, Arrays.asList(new RISKMap.Edge(1, 2, 5),
                                         new RISKMap.Edge(1, 4, 1)));
        connections.put(2, Arrays.asList(new RISKMap.Edge(2, 1, 5),
                                         new RISKMap.Edge(2, 4, 3)));
        connections.put(3, Arrays.asList(new RISKMap.Edge(3, 4, 4)));
        connections.put(4, Arrays.asList(new RISKMap.Edge(4, 1, 1),
                                         new RISKMap.Edge(4, 2, 3),
                                         new RISKMap.Edge(4, 3, 4)));

        map = new RISKMap(territories, connections);
    }

    @Test
    public void testMapConfigFile(){
        RISKMap map = new RISKMap();
        map.printMap();

        Territory a = map.getTerritoryByName("Ironcliff");
        Territory b = map.getTerritoryByName("Oz");
        assertTrue(map.isAdjacent(a, b));
    }

    @Test
    public void getTerritoryByName() {
        Territory territory1 = map.getTerritoryByName("Territory 1");
        Territory t = new Territory(1, "Territory 1", new Player("Player 1"));
        assertEquals(t, territory1);
    }

    @Test
    public void testIsAdjacent() {
        Territory territory1 = map.getTerritoryByName("Territory 1");
        Territory territory2 = map.getTerritoryByName("Territory 2");
        Territory territory3 = map.getTerritoryByName("Territory 3");
        assertTrue(map.isAdjacent(territory1, territory2));
        assertFalse(map.isAdjacent(territory1, territory3));
    }

    @Test
    public void testGetShortestPathDistance() {
        int expectedDistance = 1;
        int actualDistance = map.getShortestPathDistance("Territory 1", "Territory 4");
        assertEquals(expectedDistance, actualDistance);
    }

    @Test
    public void testGetShortestPathDistanceNoPath() {
        int expectedDistance = Integer.MAX_VALUE;
        int actualDistance = map.getShortestPathDistance("Territory 1", "Territory 2");
        assertEquals(expectedDistance, actualDistance);
    }

    @Test
    public void testGetShortestPathDistanceSameTerritory() {
        int expectedDistance = 0;
        int actualDistance = map.getShortestPathDistance("Territory 1", "Territory 1");
        assertEquals(expectedDistance, actualDistance);
    }

    @Test
    public void testGetShortestPathDistanceInvalidTerritory() {
        assertThrows(NullPointerException.class, () -> map.getShortestPathDistance("Territory 1", "Territory 5"));
    }
}

