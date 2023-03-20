package edu.duke.ece651.team5.server;

import java.util.HashMap;
import java.util.Map;

import edu.duke.ece651.team5.shared.RISKMap;

public class UnitValidRuleChecker {

    public boolean checkUnitValid(RISKMap riskMap, HashMap<String, Integer> uPs){
        int unitSum = 0;
        for(Map.Entry<String, Integer> entry: uPs.entrySet()){
            // check owner
            // check number valid
            unitSum += entry.getValue();
        }
        // if(unitSum < riskMap)
        return false;
    }
}
