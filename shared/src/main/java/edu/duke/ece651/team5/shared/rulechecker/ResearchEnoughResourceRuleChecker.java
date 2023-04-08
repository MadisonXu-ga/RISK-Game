package edu.duke.ece651.team5.shared.rulechecker;

import edu.duke.ece651.team5.shared.allResource.Resource;
import edu.duke.ece651.team5.shared.allResource.ResourceType;
import edu.duke.ece651.team5.shared.order.ResearchOrder;

public class ResearchEnoughResourceRuleChecker extends ReserachOrderRuleChecker{

    public ResearchEnoughResourceRuleChecker(ReserachOrderRuleChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(ResearchOrder order) {
        int need = order.researchConsumeCost.get(order.getPlayer().getCurrTechnologyLevel());
        int actual = order.getPlayer().getResourceToAmount().get(new Resource(ResourceType.TECHNOLOGY));
        if (need > actual) {
            return "You do not enough technical resource for this research order.";
        }
        return null;
    }
    
}
