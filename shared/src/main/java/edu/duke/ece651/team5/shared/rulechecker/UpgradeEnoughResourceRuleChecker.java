package edu.duke.ece651.team5.shared.rulechecker;

import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;
import edu.duke.ece651.team5.shared.order.UpgradeOrder;

public class UpgradeEnoughResourceRuleChecker extends UpgradeOrderRuleChecker{

    /**
     * Constructor to chain the rule checkers
     * @param next next rule checker to be checked
     */
    public UpgradeEnoughResourceRuleChecker(UpgradeOrderRuleChecker next) {
        super(next);
    }

    /**
     * check if the player has enough tech resource to execute this upgrade order
     * @param upgradeOrder the order that should be checked
     * @return error message if the player does not have enough tech resource
     *         null if ok
     */
    @Override
    protected String checkMyRule(UpgradeOrder upgradeOrder) {
        int need = upgradeOrder.getSoldierToUpgrade().entrySet().stream()
        .mapToInt(entry ->
            UpgradeOrder.upgradeConsumeCost
            .get(entry.getValue().ordinal() - entry.getKey().getFirst().getLevel().ordinal()))
        .sum();

        int actual = upgradeOrder.getPlayer().getResourceToAmount().get(new Resource(ResourceType.TECHNOLOGY));
        if(need > actual){
            return "You do not have enough technology resources.";
        }
        return null;
    }
    
    
}


