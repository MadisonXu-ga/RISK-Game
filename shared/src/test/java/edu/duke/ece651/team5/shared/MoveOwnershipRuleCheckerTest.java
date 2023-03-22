package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveOwnershipRuleCheckerTest {

    @Test
    void checkMyRule() {
        RISKMap map = new RISKMap();
        Territory elantris = map.getTerritoryByName("Elantris");
        Territory narnia = map.getTerritoryByName("Narnia");
        Player blue = new Player("blue");
        elantris.setOwner(blue);
        narnia.setOwner(blue);
        MoveOrder move = new MoveOrder("Elantris", "Narnia", 5, UnitType.SOLDIER);

        MoveOwnershipRuleChecker moveOwnershipRuleChecker = new MoveOwnershipRuleChecker(null);
        assertNull(moveOwnershipRuleChecker.checkMyRule(move, new Player("blue"), map));

        assertEquals("You cannot move between territories that do not belong to you",
                moveOwnershipRuleChecker.checkMyRule(move, new Player("yellow"), map));
    }
}