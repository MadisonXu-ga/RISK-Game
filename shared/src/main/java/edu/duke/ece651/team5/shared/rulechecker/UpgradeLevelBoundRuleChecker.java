package edu.duke.ece651.team5.shared.rulechecker;

import edu.duke.ece651.team5.shared.datastructure.Pair;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import edu.duke.ece651.team5.shared.order.UpgradeOrder;

import java.util.Map;

public class UpgradeLevelBoundRuleChecker extends UpgradeOrderRuleChecker{

    /**
     * Constructor to chain the rule checkers
     * @param next next rule checker to be checked
     */
    public UpgradeLevelBoundRuleChecker(UpgradeOrderRuleChecker next) {
        super(next);
    }

    /**
     * check if upgrade order has already reached the bound
     * @param upgradeOrder the order that should be checked
     * @return error message if it has reached, which means the soldier cannot be upgraded
     *         null if ok
     */
    @Override
    protected String checkMyRule(UpgradeOrder upgradeOrder) {
        for (Map.Entry<Pair<Soldier, Integer>, SoldierLevel> entry : upgradeOrder.getSoldierToUpgrade().entrySet()) {
            int targetUpgradeLevel = entry.getValue().ordinal();
            int currTechLevel = upgradeOrder.getPlayer().getCurrTechnologyLevel();
            if (targetUpgradeLevel > currTechLevel) {
                return ("Cannot upgrade unit level larger than current technology level.");
            }
        }

        
        return null;
    }
    
    
}
