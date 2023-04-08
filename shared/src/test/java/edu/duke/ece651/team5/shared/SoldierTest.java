package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;

import static org.junit.jupiter.api.Assertions.*;

public class SoldierTest {

    @Test
    public void testEqualsAndHashCode() {
        Soldier soldier1 = new Soldier(SoldierLevel.INFANTRY);
        Soldier soldier2 = new Soldier(SoldierLevel.INFANTRY);
        Soldier soldier3 = new Soldier(SoldierLevel.ARTILLERY);

        // test equals method
        assertTrue(soldier1.equals(soldier2));
        assertFalse(soldier1.equals(soldier3));

        // test hashCode method
        assertEquals(soldier1.hashCode(), soldier2.hashCode());
        assertNotEquals(soldier1.hashCode(), soldier3.hashCode());
    }

    @Test
    public void testGetLevel() {
        Soldier soldier = new Soldier(SoldierLevel.CAVALRY);

        assertEquals(SoldierLevel.CAVALRY, soldier.getLevel());
    }

    @Test
    public void testUpgrade() {
        Soldier soldier = new Soldier(SoldierLevel.CAVALRY);
        soldier.upgradeLevel(SoldierLevel.ARTILLERY);
        assertEquals(SoldierLevel.ARTILLERY, soldier.getLevel());
    }
}

