package edu.duke.ece651.team5.shared.rulechecker;

import edu.duke.ece651.team5.shared.datastructure.Pair;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import edu.duke.ece651.team5.shared.order.UpgradeOrder;

import java.util.Map;

public class UpgradeBackwardRuleChecker extends UpgradeOrderRuleChecker {

    /**
     * Constructor to chain the rule checkers
     * @param next next rule checker to be checked
     */
    public UpgradeBackwardRuleChecker(UpgradeOrderRuleChecker next) {
        super(next);
    }

    /**
     * check if this upgrade target level is higher than current level
     * @param upgradeOrder the order that should be checked
     * @return error message if target level is smaller or equal to current level
     *         null if it is legal
     */
    @Override
    protected String checkMyRule(UpgradeOrder upgradeOrder) {
        for (Map.Entry<Pair<Soldier, Integer>, SoldierLevel> entry : upgradeOrder.getSoldierToUpgrade().entrySet()) {
            int targetUpgradeLevel = entry.getValue().ordinal();
            int currSoldierLevel = entry.getKey().getFirst().getLevel().ordinal();
            if (targetUpgradeLevel <= currSoldierLevel) {
                return ("Cannot upgrade unit level smaller or equal to current soldier level.");
            }
        }
        return null;
    }
    
}
