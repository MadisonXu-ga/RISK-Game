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
            if (entry.getValue() <= 0 || entry.getValue() > riskMap.getPlayers().get(0).getAvailableUnit()) {
                System.out.println("check number failed");
                return false;
            }
            unitSum += entry.getValue();
        }
        // TODO: I may want to get initial availableUnit from
        System.out.println("unit Sum: " + unitSum);
        if (unitSum != riskMap.getPlayers().get(0).getAvailableUnit()) {
            return false;
        }
        return true;
    }

    // TODO: refactor later
    public String checkMoveOrderUnitValid(RISKMap map, ArrayList<MoveOrder> orders) {
        // String message = null;
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
                return "The number of units to move exceeds source territory's available units number!";
            }
        }
        return null;
    }

    public String checkAttackOrderUnitValid(RISKMap map, ArrayList<AttackOrder> orders) {
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
                return "The number of units to attack exceeds source territory's available units number!";
            }
        }
        return null;
    }
}
