package edu.duke.ece651.team5.shared.rulechecker;

import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.order.BasicOrder;

public class MovePathWithSameOwnerRuleChecker extends OrderRuleChecker{
    public MovePathWithSameOwnerRuleChecker(OrderRuleChecker next) {
        super(next);
    }

    /**
     * To check there is a path from src to dest
     * all owned by the player issued this order
     *
     * @param order  the order that should be checked
     * @param map    the map
     * @return error message if it does not meet the rule, null if it does
     */
    @Override
    protected String checkMyRule(BasicOrder order, RISKMap map) {
        String destinationName = order.getDestinationName();
        String sourceName = order.getSourceName();
        if (map.getShortestPathDistance(sourceName, destinationName) == Integer.MAX_VALUE) {
            return "There is no such a path from " +
                    sourceName + " to " + destinationName +
                    " owned by " + order.getPlayer().getName();
        } else {
            return null;
        }
    }
}
