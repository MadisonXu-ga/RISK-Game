package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

public class TerritoryTest {

    @Test
    public void testConstructorWithSoldierArmy() {
        SoldierArmy sa = new SoldierArmy();
        Territory t = new Territory(1, "Territory A", new Player("Player 1"), sa);
        assertEquals(1, t.getId());
        assertEquals("Territory A", t.getName());
        assertEquals("Player 1", t.getOwner().getName());
        assertEquals(sa, t.getSoldierArmy());
    }

    @Test
    public void testConstructorWithoutSoldierArmy() {
        Territory t = new Territory(1, "Territory A", new Player("Player 1"));
        assertEquals(1, t.getId());
        assertEquals("Territory A", t.getName());
        assertEquals("Player 1", t.getOwner().getName());
        assertNotNull(t.getSoldierArmy());
    }

    @Test
    public void testGettersAndSetters() {
        Territory t = new Territory(1, "Territory A", new Player("Player 1"));
        assertEquals(1, t.getId());
        assertEquals("Territory A", t.getName());
        assertEquals("Player 1", t.getOwner().getName());
        assertNotNull(t.getSoldierArmy());

        t.setId(2);
        t.setName("Territory B");
        t.setOwner(new Player("Player 2"));
        assertEquals(2, t.getId());
        assertEquals("Territory B", t.getName());
        assertEquals("Player 2", t.getOwner().getName());
    }

    @Test
    public void testEqualsAndHashCode() {
        Territory t1 = new Territory(1, "Territory A", new Player("Player 1"));
        Territory t2 = new Territory(1, "Territory A", new Player("Player 1"));
        Territory t3 = new Territory(2, "Territory B", new Player("Player 2"));

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
        assertNotEquals(t1, t3);
        assertNotEquals(t1.hashCode(), t3.hashCode());
    }

    @Test
    public void testGetSoldierCount() {
        Territory t = new Territory(1, "Territory A", new Player("Player 1"));
        Soldier s = new Soldier(SoldierLevel.INFANTRY);

        assertEquals(1, t.getSoldierArmy().getSoldierCount(s));
        t.getSoldierArmy().addSoldier(s, 1);
        assertEquals(2, t.getSoldierArmy().getSoldierCount(s));
    }

    @Test
    public void testGetAllSoldiers() {
        Territory t = new Territory(1, "Territory A", new Player("Player 1"));
        Soldier s1 = new Soldier(SoldierLevel.INFANTRY);
        Soldier s2 = new Soldier(SoldierLevel.ARTILLERY);
        t.getSoldierArmy().addSoldier(s1, 1);
        t.getSoldierArmy().addSoldier(s2, 1);

        assertEquals(2, t.getSoldierArmy().getAllSoldiers().get(s1));
        assertEquals(1, t.getSoldierArmy().getAllSoldiers().get(s2));
    }

    @Test
    public void testUpdateSoldier() {
        SoldierArmy army1 = new SoldierArmy();
        army1.addSoldier(new Soldier(SoldierLevel.INFANTRY), 5);
        army1.addSoldier(new Soldier(SoldierLevel.ARTILLERY), 3);

        SoldierArmy army2 = new SoldierArmy();
        army2.addSoldier(new Soldier(SoldierLevel.INFANTRY), 10);

        Territory territory = new Territory(2, "Territory 1", new Player("Player 1"), army1);
        territory.getSoldierArmy().setSoldiers(army2.getAllSoldiers());

        Map<Soldier, Integer> expectedArmy = new HashMap<>();
        expectedArmy.put(new Soldier(SoldierLevel.INFANTRY), 11);

        assertEquals(expectedArmy, territory.getSoldierArmy().getAllSoldiers());
    }

}