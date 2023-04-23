package edu.duke.ece651.team5.shared.rulechecker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.duke.ece651.team5.shared.datastructure.Pair;
import edu.duke.ece651.team5.shared.game.*;
import edu.duke.ece651.team5.shared.order.AllianceOrder;
import edu.duke.ece651.team5.shared.order.AttackOrder;

public class AllianceChecker {
        
    /**
     * check if there is alliance players in this turn
     * @param allianceOrders alliance orders
     * @return a list of pair of player that agree to form alliance. If no pairs of alliance, result is empty
     */
    public Set<Set<Player>> checkAlliance(List<AllianceOrder> allianceOrders){
        Set<Set<Player>> result = new HashSet<>();

        for (AllianceOrder order : allianceOrders) {
            Player player = order.getPlayer();
            Player targetPlayer = order.getTargetAlliancePlayer();
    
            if (allianceOrders.contains(new AllianceOrder(targetPlayer, player))) {
                result.add(new HashSet<Player>(Arrays.asList(player, targetPlayer)));
            }
        }
        return result;
    }

    public Set<Player> checkBreak(List<AttackOrder> attackOrders, RISKMap map) {
        Set<Player> result = new HashSet<>();
        for (AttackOrder order : attackOrders) {
            Player player = order.getPlayer();
            String destName = order.getDestinationName();
            Player targetPlayer = map.getTerritoryByName(destName).getOwner();
    
            if (player.hasAlliance()) {
                result.add(player);
                result.add(targetPlayer);
            }
            //todo remove all territores
        
    }
    return result;
}



}

