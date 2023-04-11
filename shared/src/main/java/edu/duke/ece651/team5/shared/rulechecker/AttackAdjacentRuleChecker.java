package edu.duke.ece651.team5.shared.rulechecker;

import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.order.BasicOrder;
import edu.duke.ece651.team5.shared.game.*;

public class AttackAdjacentRuleChecker extends OrderRuleChecker {
    /**
     * Constructor to chain the rule checkers
     * @param next next rule checker to be checked
     */
    public AttackAdjacentRuleChecker(OrderRuleChecker next) {
        super(next);
    }


    /**
     * Check if the order meets the rule that src and dest must be adjacent
     * @param order attack order
     * @param map the map
     * @return error message if it does not meet, null if it does
     */
    @Override
    protected String checkMyRule(BasicOrder order, RISKMap map) {
        String sourceName = order.getSourceName();
        String destinationName = order.getDestinationName();
        Territory source = map.getTerritoryByName(sourceName);
        Territory destination = map.getTerritoryByName(destinationName);
        if (map.isAdjacent(source, destination)) {
            return null;
        } else {
            return sourceName + " and " + destinationName + " are not adjacent";
        }
    }
}
