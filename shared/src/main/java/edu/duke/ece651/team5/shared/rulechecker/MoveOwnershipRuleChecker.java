package edu.duke.ece651.team5.shared.rulechecker;

import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.order.BasicOrder;
import edu.duke.ece651.team5.shared.game.*;

public class MoveOwnershipRuleChecker extends OrderRuleChecker{
    public MoveOwnershipRuleChecker(OrderRuleChecker next) {
        super(next);
    }

    /**
     *  Check if the order meets the rule that src and dest
     *  must belong to the same player
     * @param order move order
     * @param map the map
     * @return error message if it does not meet, null if it does
     */
    @Override
    protected String checkMyRule(BasicOrder order, RISKMap map) {
        String destinationName = order.getDestinationName();
        String sourceName = order.getSourceName();
        Territory source = map.getTerritoryByName(sourceName);
        Territory destination = map.getTerritoryByName(destinationName);
        if (!source.getOwner().equals(order.getPlayer()) || !destination.getOwner().equals(order.getPlayer())) {
            return "You cannot move between territories that do not belong to you";
        } else {
            return null;
        }
    }
}
