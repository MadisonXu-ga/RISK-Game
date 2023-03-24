package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttackOrderTest {

    @Test
    void execute() {
        RISKMap map = new RISKMap();
        Territory narnia = map.getTerritoryByName("Narnia");
        Territory midkemia = map.getTerritoryByName("Midkemia");
        narnia.updateUnitCount(UnitType.SOLDIER, false, 10);
        midkemia.updateUnitCount(UnitType.SOLDIER, false, 12);
        AttackOrder attack = new AttackOrder("Narnia", "Midkemia", 2, UnitType.SOLDIER, "Green");
        attack.execute(map);
        int narniaUnitNum = narnia.getUnitNum(UnitType.SOLDIER);
        assertEquals(8, narniaUnitNum);
    }

    @Test
    void testLoseOneUnit() {
        AttackOrder attack = new AttackOrder("Narnia", "Midkemia",
                2, UnitType.SOLDIER, "Green");
        attack.loseOneUnit();
        assertEquals(1, attack.getNumber());
    }

    @Test
    void testUpdateUnitNumber() {
        AttackOrder attack = new AttackOrder("Narnia", "Midkemia",
                2, UnitType.SOLDIER, "Green");
        attack.updateUnitNumber(3);
        assertEquals(5, attack.getNumber());
    }

    @Test
    void testGetPlayerName() {
        AttackOrder attack = new AttackOrder("Narnia", "Midkemia",
                2, UnitType.SOLDIER, "Green");
        assertEquals("Green", attack.getPlayerName());
    }
}