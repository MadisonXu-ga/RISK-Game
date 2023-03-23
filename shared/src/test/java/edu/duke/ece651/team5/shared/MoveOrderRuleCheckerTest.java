package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MoveOrderRuleCheckerTest {

    @Test
    void testCheckOrder() {
        MoveOwnershipRuleChecker ruleChecker =
                new MoveOwnershipRuleChecker(
                new UnitNumberRuleChecker(null)
        );
        RISKMap map = new RISKMap();
        Territory elantris = map.getTerritoryByName("Elantris");
        Territory narnia = map.getTerritoryByName("Narnia");
        elantris.updateUnitCount(UnitType.SOLDIER, false, 6);
        narnia.updateUnitCount(UnitType.SOLDIER, false, 10);
        Player blue = new Player("blue");
        elantris.setOwner(blue);
        narnia.setOwner(blue);
        MoveOrder move = new MoveOrder("Elantris", "Narnia", 5, UnitType.SOLDIER, "Green");
        assertNull(ruleChecker.checkOrder(move, blue, map));
    }

    @Test
    void testCheckOrderWithError() {
        MoveOwnershipRuleChecker ruleChecker =
                new MoveOwnershipRuleChecker(
                        new UnitNumberRuleChecker(null)
                );
        RISKMap map = new RISKMap();
        Territory elantris = map.getTerritoryByName("Elantris");
        Territory narnia = map.getTerritoryByName("Narnia");
        elantris.updateUnitCount(UnitType.SOLDIER, false, 6);
        narnia.updateUnitCount(UnitType.SOLDIER, false, 10);
        Player blue = new Player("blue");
        elantris.setOwner(blue);
        narnia.setOwner(blue);
        MoveOrder move = new MoveOrder("Elantris", "Narnia", 100, UnitType.SOLDIER, "Green");
        assertEquals("There are only 6 units in Elantris, but you entered 100",
                ruleChecker.checkOrder(move, blue, map));

    }

    @Test
    void testCheckOrderNoNext() {
        MoveOwnershipRuleChecker ruleChecker = new MoveOwnershipRuleChecker(null);
        RISKMap map = new RISKMap();
        Territory elantris = map.getTerritoryByName("Elantris");
        Territory narnia = map.getTerritoryByName("Narnia");
        Player blue = new Player("blue");
        elantris.setOwner(blue);
        narnia.setOwner(blue);
        MoveOrder move = new MoveOrder("Elantris", "Narnia", 5, UnitType.SOLDIER, "Green");
        assertNull(ruleChecker.checkOrder(move, blue, map));
    }
}
