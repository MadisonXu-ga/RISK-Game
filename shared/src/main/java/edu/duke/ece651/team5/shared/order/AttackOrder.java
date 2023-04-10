package edu.duke.ece651.team5.shared.order;

import java.io.Serializable;
import java.util.Map;

import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.*;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;
import edu.duke.ece651.team5.shared.resource.*;
import edu.duke.ece651.team5.shared.constant.Constants;

public class AttackOrder extends BasicOrder implements Comparable<AttackOrder>, Serializable{


    public AttackOrder(String sourceName, String destinationName, SoldierArmy soldierToNumber, Player player) {
        super(sourceName, destinationName, soldierToNumber, player);
    }

    /**
     * compare two attack order according to the player name
     * @param other the object to be compared.
     * @return > 0 if this is greater, = 0 if equals, < 0 if smaller
     */
    @Override
    public int compareTo(AttackOrder other) {
        return player.getName().compareTo(other.player.getName());
    }

    /**
     * executed the move order
     * @param map the map
     */
    @Override
    public void execute(RISKMap map) {
        Territory source = map.getTerritoryByName(sourceName);
        for (Map.Entry<Soldier, Integer> entry : soldierToNumber.getAllSoldiers().entrySet()) {
            Soldier soldier = entry.getKey();
            int number = entry.getValue();
            source.getSoldierArmy().removeSoldier(soldier, number);
        }
        int distance = map.getShortestPathDistance(sourceName, destinationName);
        player.consumeResource(new Resource(ResourceType.FOOD), Constants.C * distance * soldierToNumber.getTotalCountSolider());
    }

    /**
     * merge two attack orders together to become one force
     * @param other the other attack order which should be merged with
     */
    public void mergeWith(AttackOrder other) {
        other.getSoldierToNumber().getAllSoldiers().entrySet().stream()
        .filter(entry -> this.getDestinationName().equals(other.getDestinationName()))
        .forEach(entry -> soldierToNumber.getAllSoldiers().merge(entry.getKey(), entry.getValue(), Integer::sum));
    }

    




    
    
}
