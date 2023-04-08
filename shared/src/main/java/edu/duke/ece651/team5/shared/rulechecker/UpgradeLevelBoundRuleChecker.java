package edu.duke.ece651.team5.shared.rulechecker;

import edu.duke.ece651.team5.shared.Pair;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import edu.duke.ece651.team5.shared.order.UpgradeOrder;

import java.util.Map;

public class UpgradeLevelBoundRuleChecker extends UpgradeOrderRuleChecker{

    public UpgradeLevelBoundRuleChecker(UpgradeOrderRuleChecker next) {
        super(next);
    }

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
