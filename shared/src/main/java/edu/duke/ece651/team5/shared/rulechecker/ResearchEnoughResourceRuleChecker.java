package edu.duke.ece651.team5.shared.rulechecker;

import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;
import edu.duke.ece651.team5.shared.order.ResearchOrder;

public class ResearchEnoughResourceRuleChecker extends ResearchOrderRuleChecker{

    public ResearchEnoughResourceRuleChecker(ResearchOrderRuleChecker next) {
        super(next);
    }

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
