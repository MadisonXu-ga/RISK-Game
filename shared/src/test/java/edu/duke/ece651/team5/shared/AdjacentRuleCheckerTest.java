package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdjacentRuleCheckerTest {

    @Test
    void checkMyRule() {
        AdjacentRuleChecker adjacentRuleChecker = new AdjacentRuleChecker(null);
        RISKMap map = new RISKMap();
        MoveOrder move1 = new MoveOrder("Elantris", "Scadrial", 10, UnitType.SOLDIER);
        assertNull(adjacentRuleChecker.checkMyRule(move1, null, map));

        MoveOrder move2 = new MoveOrder("Narnia", "Scadrial", 5, UnitType.SOLDIER);
        assertEquals("Narnia and Scadrial are not adjacent", adjacentRuleChecker.checkMyRule(move2, null, map));
    }
}