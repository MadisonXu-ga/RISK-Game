package edu.duke.ece651.team5.shared.rulechecker;

import edu.duke.ece651.team5.shared.allResource.Resource;
import edu.duke.ece651.team5.shared.allResource.ResourceType;
import edu.duke.ece651.team5.shared.order.UpgradeOrder;

public class UpgradeEnoughResourceRuleChecker extends UpgradeOrderRuleChecker{

    public UpgradeEnoughResourceRuleChecker(UpgradeOrderRuleChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(UpgradeOrder upgradeOrder) {
        int need = upgradeOrder.getSoldierToUpgrade().entrySet().stream()
        .mapToInt(entry ->
            UpgradeOrder.researchConsumeCost
            .get(entry.getValue().ordinal() - entry.getKey().getFirst().getLevel().ordinal()))
        .sum();

        int actual = upgradeOrder.getPlayer().getResourceToAmount().get(new Resource(ResourceType.TECHNOLOGY));
        if(need > actual){
            return "You do not have enough technology resources.";
        }
        return null;
    }
    
    
}

//can not backward
