package edu.duke.ece651.team5.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.duke.ece651.team5.shared.BasicOrder;
import edu.duke.ece651.team5.shared.Player;
import edu.duke.ece651.team5.shared.RISKMap;
import edu.duke.ece651.team5.shared.UnitType;

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

    public boolean checkOrderUnitValid(RISKMap map, ArrayList<BasicOrder> orders){
        int availableUnit = 0;
        int tryToUseUnit = 0;
        for(BasicOrder order: orders){
            availableUnit += map.getTerritoryByName(order.getSourceName()).getUnitNum(UnitType.SOLDIER);
            tryToUseUnit += order.getNumber();
        }
        return tryToUseUnit <= availableUnit;
    }
}
