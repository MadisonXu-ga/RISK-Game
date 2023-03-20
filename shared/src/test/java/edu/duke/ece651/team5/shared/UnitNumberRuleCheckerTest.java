package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitNumberRuleCheckerTest {

    @Test
    void checkMyRule() {
        UnitNumberRuleChecker unitNumberRuleChecker = new UnitNumberRuleChecker(null);
        RISKMap map = new RISKMap();
        MoveOrder move1 = new MoveOrder("Elantris", "Scadrial", 2, UnitType.SOLDIER);
        assertNull(unitNumberRuleChecker.checkMyRule(move1, null, map));

        MoveOrder move2 = new MoveOrder("Elantris", "Scadrial", 100, UnitType.SOLDIER);
        assertEquals("There are only 6 units in Elantris, but you entered 100", unitNumberRuleChecker.checkMyRule(move2, null, map));
    }
}