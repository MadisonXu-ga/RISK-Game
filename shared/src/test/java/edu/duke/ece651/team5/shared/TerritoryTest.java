package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryTest {

    @Test
    void testGetName() {
        Territory hogwarts = new Territory("Hogwarts", null);
        String actual = hogwarts.getName();
        String expected = "Hogwarts";
        assertEquals(expected, actual);
    }

    @Test
    void testGetUnitNum() {
        HashMap<Unit, Integer> units = new HashMap<>();
        units.put(UnitType.SOLDIER, 10);
        Territory hogwarts = new Territory("Hogwarts", units);
        int actual = hogwarts.getUnitNum(UnitType.SOLDIER);
        assertEquals(10, actual);
    }

    @Test
    void testUpdateUnitCount() {
        HashMap<Unit, Integer> units = new HashMap<>();
        units.put(UnitType.SOLDIER, 10);
        Territory hogwarts = new Territory("Hogwarts", units);
        hogwarts.updateUnitCount(UnitType.SOLDIER, true, 5);
        int actual1 = hogwarts.getUnitNum(UnitType.SOLDIER);
        int expected1 = 5;
        assertEquals(expected1, actual1);

        hogwarts.updateUnitCount(UnitType.SOLDIER, false, 3);
        int actual2 = hogwarts.getUnitNum(UnitType.SOLDIER);
        int expected2 = 8;
        assertEquals(expected2, actual2);

        // assertThrows(IllegalArgumentException.class, () -> hogwarts.updateUnitCount(new Soldier(), false, 1));

    }

    @Test
    void testAddHasOwner(){

        Territory oneTerritory = new Territory("Mordor", new HashMap<>());
        Player greenPlayer = new Player("green");

        oneTerritory.setOwner(greenPlayer);

        assertTrue(oneTerritory.hasOwner());
        assertEquals(greenPlayer, oneTerritory.getOwner());
        
    }

    @Test
    void testEquals_and_Hashcode_Territory(){

        //TODO here we need to test when a territory doesnt equate to "this "
        Territory terr1 = new Territory("green", new HashMap<>());
        Territory terr2 = new Territory("green", new HashMap<>() );
        Territory terr3 = new Territory("red", new HashMap<>() );
        
        assertTrue(terr1.equals(terr2));
        assertFalse(terr1.equals(terr3));
        assertEquals(terr1.hashCode(), terr2.hashCode());
    }
}