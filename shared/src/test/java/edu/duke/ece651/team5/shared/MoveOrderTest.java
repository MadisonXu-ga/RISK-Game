package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveOrderTest {

    @Test
    void testExecute() {
        RISKMap map = new RISKMap();
        MoveOrder move = new MoveOrder("Narnia", "Midkemia", 2, UnitType.SOLDIER);
        move.execute(map);
        Territory narnia = map.getTerritoryByName("Narnia");
        Territory midkemia = map.getTerritoryByName("Midkemia");
        // MoveOrder move = new MoveOrder(narnia, midkemia, 2, UnitType.SOLDIER);
        move.execute(map);
        int narniaUnitNum = narnia.getUnitNum(UnitType.SOLDIER);
        int midkemiaUnitNum = midkemia.getUnitNum(UnitType.SOLDIER);
        assertEquals(6, narniaUnitNum);
        assertEquals(16, midkemiaUnitNum);
    }


    @Test
    void testEqualOrder(){
        MoveOrder move1 = new MoveOrder("Narnia", "Midkemia", 2, UnitType.SOLDIER);
        MoveOrder move2 = new MoveOrder("Narnia", "Midkemia", 2, UnitType.SOLDIER);
        MoveOrder move3 = new MoveOrder("Narnia", "Midkemia", 3, UnitType.SOLDIER);
        assertTrue(move1.equals(move2));
        assertFalse(move1.equals(move3));

    }
}