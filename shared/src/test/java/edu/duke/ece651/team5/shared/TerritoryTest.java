package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryTest {

    @Test
    void testGetName() {
        Territory hogwarts = new Territory("Hogwarts");
        String actual = hogwarts.getName();
        String expected = "Hogwarts";
        assertEquals(expected, actual);
    }
}