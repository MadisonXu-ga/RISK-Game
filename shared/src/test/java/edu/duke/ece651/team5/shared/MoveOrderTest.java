package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MoveOrderTest {

    @Test
    void testExecute() {
        RISKMap map = new RISKMap();
        Territory narnia = map.getTerritoryByName("Narnia");
        Territory midkemia = map.getTerritoryByName("Midkemia");
        MoveOrder move = new MoveOrder(narnia, midkemia, 2, UnitType.SOLDIER);
        move.execute();
        int narniaUnitNum = narnia.getUnitNum(UnitType.SOLDIER);
        int midkemiaUnitNum = midkemia.getUnitNum(UnitType.SOLDIER);
        assertEquals(8, narniaUnitNum);
        assertEquals(14, midkemiaUnitNum);
    }
}