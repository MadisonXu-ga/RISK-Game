package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;

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
}