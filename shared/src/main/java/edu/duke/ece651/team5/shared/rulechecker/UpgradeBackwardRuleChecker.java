package edu.duke.ece651.team5.shared.rulechecker;

import edu.duke.ece651.team5.shared.Pair;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import edu.duke.ece651.team5.shared.order.UpgradeOrder;

import java.util.Map;

public class UpgradeBackwardRuleChecker extends UpgradeOrderRuleChecker {

    public UpgradeBackwardRuleChecker(UpgradeOrderRuleChecker next) {
        super(next);
    }


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
