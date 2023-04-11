package edu.duke.ece651.team5.shared.rulechecker;

import edu.duke.ece651.team5.shared.constant.Constants;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.order.BasicOrder;
import edu.duke.ece651.team5.shared.resource.*;

public class MoveResourceChecker extends OrderRuleChecker {

    public MoveResourceChecker(OrderRuleChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(BasicOrder order, RISKMap map) {
        Player player = order.getPlayer();
        int distance = map.getShortestPathDistance(order.getSourceName(), order.getDestinationName(), true);
        int need = Constants.C * distance * order.getSoldierToNumber().getTotalCountSolider();
        int actual = player.getResourceCount(new Resource(ResourceType.FOOD));
        if(need > actual){
            return "You do not have enough food resource for this move order.";
        }
        return null;
    }
    
    
}
