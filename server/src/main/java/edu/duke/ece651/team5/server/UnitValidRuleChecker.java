package edu.duke.ece651.team5.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.duke.ece651.team5.shared.AttackOrder;
import edu.duke.ece651.team5.shared.BasicOrder;
import edu.duke.ece651.team5.shared.MoveOrder;
import edu.duke.ece651.team5.shared.Player;
import edu.duke.ece651.team5.shared.RISKMap;
import edu.duke.ece651.team5.shared.UnitType;

public class UnitValidRuleChecker {

    public boolean checkUnitValid(RISKMap riskMap, HashMap<String, Integer> uPs) {
        int unitSum = 0;
        for (Map.Entry<String, Integer> entry : uPs.entrySet()) {
            // TODO: check owner
            // check number valid
            if (entry.getValue() < 0 || entry.getValue() > 50) {
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

    // TODO: refactor later
    public boolean checkMoveOrderUnitValid(RISKMap map, ArrayList<MoveOrder> orders) {
        HashMap<String, Integer> tryToUseUnits = new HashMap<>();

        for (BasicOrder order : orders) {
            String src = order.getSourceName();
            int unitNum = order.getNumber();
            if (!tryToUseUnits.containsKey(src)) {
                tryToUseUnits.put(src, unitNum);
            } else {
                int temp = tryToUseUnits.get(src);
                tryToUseUnits.put(src, unitNum + temp);
            }
        }

        for (String terr : tryToUseUnits.keySet()) {
            int terrUnit = map.getTerritoryByName(terr).getUnitNum(UnitType.SOLDIER);
            if (terrUnit < tryToUseUnits.get(terr)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkAttackOrderUnitValid(RISKMap map, ArrayList<AttackOrder> orders) {
        HashMap<String, Integer> tryToUseUnits = new HashMap<>();

        for (BasicOrder order : orders) {
            String src = order.getSourceName();
            int unitNum = order.getNumber();
            if (!tryToUseUnits.containsKey(src)) {
                tryToUseUnits.put(src, unitNum);
            } else {
                int temp = tryToUseUnits.get(src);
                tryToUseUnits.put(src, unitNum + temp);
            }
        }

        for (String terr : tryToUseUnits.keySet()) {
            int terrUnit = map.getTerritoryByName(terr).getUnitNum(UnitType.SOLDIER);
            if (terrUnit < tryToUseUnits.get(terr)) {
                return false;
            }
        }
        return true;
    }
}
