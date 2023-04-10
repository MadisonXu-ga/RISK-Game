package edu.duke.ece651.team5.shared;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;

import static edu.duke.ece651.team5.shared.constant.Constants.DEFAULT_INIT_SOLDIER_NUM;
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
        Soldier soldier = new Soldier(SoldierLevel.INFANTRY);
        int count = 5;
        army.addSoldier(soldier, count);

        assertEquals(count + DEFAULT_INIT_SOLDIER_NUM, army.getSoldierCount(soldier));
    }

    @Test
    public void testRemoveSoldier() {
        Soldier soldier = new Soldier(SoldierLevel.INFANTRY);
        int count = 5;
        army.addSoldier(soldier, count);

        army.removeSoldier(soldier, 3);
        assertEquals(count + DEFAULT_INIT_SOLDIER_NUM - 3, army.getSoldierCount(soldier));

        army.removeSoldier(soldier, 5);
        assertFalse(army.getAllSoldiers().containsKey(soldier));
    }

    @Test
    public void testSetSoldier() {
        Soldier soldier1 = new Soldier(SoldierLevel.INFANTRY);
        Soldier soldier2 = new Soldier(SoldierLevel.ARTILLERY);
        int count1 = 5;
        int count2 = 3;

        Map<Soldier, Integer> soldiers = new HashMap<>();
        soldiers.put(soldier1, count1);
        soldiers.put(soldier2, count2);

        army.setSoldiers(soldiers);

        assertEquals(count1, army.getSoldierCount(soldier1));
        assertEquals(count2, army.getSoldierCount(soldier2));
        assertTrue(army.getAllSoldiers().containsKey(new Soldier(SoldierLevel.INFANTRY)));
        assertFalse(army.getAllSoldiers().containsKey(new Soldier(SoldierLevel.ARMOR)));
    }

    @Test
    public void testGetAllSoldiers() {
        Soldier soldier = new Soldier(SoldierLevel.INFANTRY);
        int count = 5;
        army.addSoldier(soldier, count);

        Map<Soldier, Integer> allSoldiers = army.getAllSoldiers();

        assertTrue(allSoldiers.containsKey(soldier));
        assertEquals(count + DEFAULT_INIT_SOLDIER_NUM, allSoldiers.get(soldier));
        assertEquals(Collections.singletonMap(soldier, count + DEFAULT_INIT_SOLDIER_NUM), allSoldiers);
    }


    @Test
    public void testUpgradeSoldierAndCountSoldier() {
        Soldier soldier = new Soldier(SoldierLevel.INFANTRY);
        int count = 5;
        army.addSoldier(soldier, count);

        Soldier infantry = new Soldier(SoldierLevel.INFANTRY);
        int initialCount = army.getSoldierCount(infantry);
        int upgradeCount = 3;
        SoldierLevel targetLevel = SoldierLevel.ARTILLERY;

        army.upgradeSoldier(infantry, upgradeCount, targetLevel);

        assertEquals(initialCount - upgradeCount, army.getSoldierCount(infantry));
        assertEquals(upgradeCount, army.getSoldierCount(new Soldier(targetLevel)));
        assertEquals(6, army.getTotalCountSolider());
    }

}

