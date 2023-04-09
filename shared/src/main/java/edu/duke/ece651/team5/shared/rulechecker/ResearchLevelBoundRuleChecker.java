package edu.duke.ece651.team5.shared.rulechecker;
import edu.duke.ece651.team5.shared.order.ResearchOrder;

import static edu.duke.ece651.team5.shared.constant.Constants.GAME_MAX_TECHNOLOGY_LEVEL;

public class ResearchLevelBoundRuleChecker extends ResearchOrderRuleChecker{

    public ResearchLevelBoundRuleChecker(ResearchOrderRuleChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(ResearchOrder order) {
        if (order.getPlayer().getCurrTechnologyLevel() == GAME_MAX_TECHNOLOGY_LEVEL) {
            return ("Cannot upgrade technology level anymore");
        }
        return null;
    }

}
