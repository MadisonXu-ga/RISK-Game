package edu.duke.ece651.team5.shared.rulechecker;

import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.order.BasicOrder;
import edu.duke.ece651.team5.shared.game.*;

public class AttackOwnershipRuleChecker extends OrderRuleChecker{
    public AttackOwnershipRuleChecker(OrderRuleChecker next) {
    super(next);
}

/**
 *  Check if the order meets the rule that src must belong to the player issued this order
 *  and dest must not
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
    if (destination.getOwner().equals(order.getPlayer())) {
        return "You cannot attack at your own territories";
    } else if (!source.getOwner().equals(order.getPlayer()) ) {
        return "You cannot attack from territory that does not belong to you";
    } else {
        return null;
    }
}

}
