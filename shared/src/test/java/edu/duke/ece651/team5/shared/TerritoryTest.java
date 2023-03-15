package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryTest {

    @Test
    void testGetName() {
        Territory hogwarts = new Territory("Hogwarts");
        String actual = hogwarts.getName();
        String expected = "Hogwarts";
        assertEquals(expected, actual);
    }

    @Test
    void testGetUnitNum() {
        Soldier soldier = new Soldier();
        HashMap<Unit, Integer> units = new HashMap<>();
        units.put(soldier, 10);
        Territory hogwarts = new Territory("Hogwarts", units);
        int actual = hogwarts.getUnitNum(soldier);
        assertEquals(10, actual);
    }
}