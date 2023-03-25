package edu.duke.ece651.team5.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UnitValidRuleChecker {

    public UnitValidRuleChecker() {

    }

    /**
     * @param riskMap       RISKMap
     * @param placementInfo HashMap<String, Integer>
     * @return boolean
     */
    public boolean checkUnitValid(RISKMap riskMap, HashMap<String, Integer> placementInfo) {
        int unitSum = 0;
        for (Map.Entry<String, Integer> entry : placementInfo.entrySet()) {
            // TODO: check owner
            // check number valid
            // if (entry.getValue() <= 0 || entry.getValue() >
            // riskMap.getPlayers().get(0).getAvailableUnit()) {
            if (entry.getValue() <= 0 || entry.getValue() > riskMap.getAvailableUnit()) {
                return false;
            }
            unitSum += entry.getValue();
        }
        // TODO: I may want to get initial availableUnit from
        if (unitSum != riskMap.getAvailableUnit()) {
            return false;
        }
        return true;
    }

    /**
     * @param map    RISKMap
     * @param orders ArrayList<AttackOrder>
     * @return String
     */
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
