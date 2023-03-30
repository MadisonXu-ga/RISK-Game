package edu.duke.ece651.team5.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SoldierTest {

    @Test
    public void testEqualsAndHashCode() {
        Soldier soldier1 = new Soldier(SoldierType.INFANTRY, 1);
        Soldier soldier2 = new Soldier(SoldierType.INFANTRY, 1);
        Soldier soldier3 = new Soldier(SoldierType.ARTILLERY, 2);

        // test equals method
        assertTrue(soldier1.equals(soldier2));
        assertFalse(soldier1.equals(soldier3));

        // test hashCode method
        assertEquals(soldier1.hashCode(), soldier2.hashCode());
        assertNotEquals(soldier1.hashCode(), soldier3.hashCode());
    }

    @Test
    public void testSetLevel() {
        Soldier soldier = new Soldier(SoldierType.CAVALRY, 3);

        assertEquals(3, soldier.getLevel());

        soldier.setLevel(4);

        assertEquals(4, soldier.getLevel());
    }

    @Test
    public void testToString() {
        Soldier soldier = new Soldier(SoldierType.INFANTRY, 2);

        assertEquals("{ type='INFANTRY', level='2'}", soldier.toString());
    }
}

