package edu.duke.ece651.team5.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.game.RISKMap;

public class UnitValidRuleCheckerTest {
    @Test
    void testCheckUnitValid() {
        RISKMap testMap = new RISKMap();
        UnitValidRuleChecker checker = new UnitValidRuleChecker();
        assertEquals(true, checker.checkUnitValid(testMap, createPlaceInfo(10, 20, 20)));

        // wrong case: sum of units is not enough
        assertEquals(false, checker.checkUnitValid(testMap, createPlaceInfo(1, 1, 1)));
        // wrong case: sum of units exceed
        assertEquals(false, checker.checkUnitValid(testMap, createPlaceInfo(20, 20, 20)));
        // wrong case: unit number < 0
        assertEquals(false, checker.checkUnitValid(testMap, createPlaceInfo(20, -1, 10)));
        // wrong case: unit number == 0
        assertEquals(false, checker.checkUnitValid(testMap, createPlaceInfo(20, 0, 10)));
        // wrong case: unit number > max number
        assertEquals(false, checker.checkUnitValid(testMap, createPlaceInfo(20, 51, 10)));
    }

    private HashMap<String, Integer> createPlaceInfo(int num1, int num2, int num3) {
        HashMap<String, Integer> placeInfo = new HashMap<>();
        placeInfo.put("Narnia", num1);
        placeInfo.put("Elantris", num2);
        placeInfo.put("Midkemia", num3);
    
        return placeInfo;
      }
}
