package edu.duke.ece651.team5.shared.rulechecker;
import edu.duke.ece651.team5.shared.order.ResearchOrder;

import static edu.duke.ece651.team5.shared.constant.Constants.GAME_MAX_TECHNOLOGY_LEVEL;

public class ResearchLevelBoundRuleChecker extends ResearchOrderRuleChecker{

    /**
     * Constructor to chain the rule checkers
     * @param next next rule checker to be checked
     */
    public ResearchLevelBoundRuleChecker(ResearchOrderRuleChecker next) {
        super(next);
    }

    /**
     * check if the technology level has reached the bound
     * @param order the order that should be checked
     * @return error message if target level is smaller or equal to current level
     *         null if it is legal
     */
    @Override
    protected String checkMyRule(ResearchOrder order) {
        if (order.getPlayer().getCurrTechnologyLevel() == GAME_MAX_TECHNOLOGY_LEVEL) {
            return ("Cannot upgrade technology level anymore");
        }
        return null;
    }

}
