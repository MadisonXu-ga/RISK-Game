package edu.duke.ece651.team5.shared.rulechecker;

import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;
import edu.duke.ece651.team5.shared.order.ResearchOrder;

public class ResearchEnoughResourceRuleChecker extends ResearchOrderRuleChecker{

    /**
     * Constructor to chain the rule checkers
     * @param next next rule checker to be checked
     */
    public ResearchEnoughResourceRuleChecker(ResearchOrderRuleChecker next) {
        super(next);
    }
    /**
     * check if the player has enough tech resource to execute this research order
     * @param order the order that should be checked
     * @return error message if the player does not have enough tech resource
     *         null if ok
     */
    @Override
    protected String checkMyRule(ResearchOrder order) {
        int need = order.researchConsumeCost.get(order.getPlayer().getCurrTechnologyLevel());
        int actual = order.getPlayer().getResourceToAmount().get(new Resource(ResourceType.TECHNOLOGY));
        if (need > actual) {
            return "You do not have enough technical resource for this research order.";
        }
        return null;
    }
    
}
