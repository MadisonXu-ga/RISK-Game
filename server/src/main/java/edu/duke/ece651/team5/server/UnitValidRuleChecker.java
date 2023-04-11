package edu.duke.ece651.team5.server;

import java.util.HashMap;
import java.util.Map;

import edu.duke.ece651.team5.shared.game.*;
import edu.duke.ece651.team5.shared.constant.*;

public class UnitValidRuleChecker {
    public boolean checkUnitValid(RISKMap riskMap, HashMap<String, Integer> placementInfo) {
        int unitSum = 0;
        for (Map.Entry<String, Integer> entry : placementInfo.entrySet()) {
            // check number valid
            if (entry.getValue() <= 0 || entry.getValue() > Constants.AVAILABLE_UNIT) {
                return false;
            }
            unitSum += entry.getValue();
        }
        if (unitSum != Constants.AVAILABLE_UNIT) {
            return false;
        }
        return true;
    }

    
}
