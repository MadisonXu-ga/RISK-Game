package edu.duke.ece651.team5.server;

import java.util.HashMap;
import java.util.Map;

import edu.duke.ece651.team5.shared.RISKMap;

public class UnitValidRuleChecker {

    public boolean checkUnitValid(RISKMap riskMap, HashMap<String, Integer> uPs) {
        int unitSum = 0;
        for (Map.Entry<String, Integer> entry : uPs.entrySet()) {
            // TODO: check owner
            // check number valid
            if (entry.getValue() <= 0 || entry.getValue() > 50) {
                return false;
            }
            unitSum += entry.getValue();
        }
        // TODO: I may want to get initial availableUnit from
        if (unitSum != 50) {
            return false;
        }
        return true;
    }
}
