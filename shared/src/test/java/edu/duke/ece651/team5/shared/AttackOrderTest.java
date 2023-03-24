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
        MoveOrder move = new MoveOrder("Narnia", "Midkemia", 2, UnitType.SOLDIER, "Green");
        move.execute(map);

        move.execute(map);
        int narniaUnitNum = narnia.getUnitNum(UnitType.SOLDIER);
        assertEquals(6, narniaUnitNum);
    }
}