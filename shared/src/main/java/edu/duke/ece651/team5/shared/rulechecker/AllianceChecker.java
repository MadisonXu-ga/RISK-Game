package edu.duke.ece651.team5.shared.rulechecker;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.duke.ece651.team5.shared.datastructure.Pair;
import edu.duke.ece651.team5.shared.game.*;
import edu.duke.ece651.team5.shared.order.AllianceOrder;

public class AllianceChecker {
        
    /**
     * check if there is alliance players in this turn
     * @param allianceOrders alliance orders
     * @param game the game
     * @return a list of pair of player that agree to form alliance. If no pairs of alliance, result is empty
     */
    public List<Pair<Player, Player>> checkAlliance(List<AllianceOrder> allianceOrders, Game game){
        List<Pair<Player, Player>> result = new ArrayList<>();
        Set<Player> alliancePlayers = new HashSet<>();

        for (AllianceOrder order : allianceOrders) {
            Player player = order.getPlayer();
            Player targetPlayer = order.getTargetAlliancePlayer();
    
            if (!alliancePlayers.contains(player) && !alliancePlayers.contains(targetPlayer)) {
                if (allianceOrders.contains(new AllianceOrder(targetPlayer, player, game))) {
                    result.add(new Pair<Player,Player>(player, targetPlayer));
                    alliancePlayers.add(player);
                    alliancePlayers.add(targetPlayer);
                }
            }
        }
        return result;
    }


}

