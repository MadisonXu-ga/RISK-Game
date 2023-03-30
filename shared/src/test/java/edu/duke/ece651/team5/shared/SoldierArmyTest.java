package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static edu.duke.ece651.team5.shared.Constants.DEFAULT_INIT_SOLDIER_NUM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class SoldierArmyTest {

    private SoldierArmy army;

    @BeforeEach
    public void setup() {
        army = new SoldierArmy();
    }

    @Test
    public void testAddSoldier() {
        Soldier soldier = new Soldier(SoldierType.INFANTRY, 1);
        int count = 5;
        army.addSoldier(soldier, count);

        assertEquals(count + DEFAULT_INIT_SOLDIER_NUM, army.getSoldierCount(soldier));
    }

    @Test
    public void testRemoveSoldier() {
        Soldier soldier = new Soldier(SoldierType.INFANTRY, 1);
        int count = 5;
        army.addSoldier(soldier, count);

        army.removeSoldier(soldier, 3);
        assertEquals(count + DEFAULT_INIT_SOLDIER_NUM - 3, army.getSoldierCount(soldier));

        army.removeSoldier(soldier, 5);
        assertFalse(army.getAllSoldiers().containsKey(soldier));
    }

    @Test
    public void testUpdateSoldier() {
        Soldier soldier1 = new Soldier(SoldierType.INFANTRY, 1);
        Soldier soldier2 = new Soldier(SoldierType.ARTILLERY, 2);
        int count1 = 5;
        int count2 = 3;

        Map<Soldier, Integer> soldiers = new HashMap<>();
        soldiers.put(soldier1, count1);
        soldiers.put(soldier2, count2);

        army.updateSoldier(soldiers);

        assertEquals(count1, army.getSoldierCount(soldier1));
        assertEquals(count2, army.getSoldierCount(soldier2));
        assertFalse(army.getAllSoldiers().containsKey(new Soldier(SoldierType.INFANTRY, 2)));
    }

    @Test
    public void testGetAllSoldiers() {
        Soldier soldier = new Soldier(SoldierType.INFANTRY, 1);
        int count = 5;
        army.addSoldier(soldier, count);

        Map<Soldier, Integer> allSoldiers = army.getAllSoldiers();

        assertTrue(allSoldiers.containsKey(soldier));
        assertEquals(count + DEFAULT_INIT_SOLDIER_NUM, allSoldiers.get(soldier).intValue());
        assertEquals(Collections.singletonMap(soldier, count + DEFAULT_INIT_SOLDIER_NUM), allSoldiers);
    }

}

