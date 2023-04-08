package edu.duke.ece651.team5.shared.order;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import edu.duke.ece651.team5.shared.game.*;
import edu.duke.ece651.team5.shared.unit.Soldier;

public class AttackOrder extends BasicOrder implements Comparable<AttackOrder>, Serializable{


    public AttackOrder(String sourceName, String destinationName, Map<Soldier, Integer> soldiers, Player player) {
        super(sourceName, destinationName, soldiers, player);
    }

    @Override
    public int compareTo(AttackOrder other) {
        return player.getName().compareTo(other.player.getName());
    }

    @Override
    public void execute(RISKMap map) {
        Territory source = map.getTerritoryByName(sourceName);
        for (Map.Entry<Soldier, Integer> entry : soldierToNumber.entrySet()) {
            Soldier soldier = entry.getKey();
            int number = entry.getValue();
            source.getSoldierArmy().removeSoldier(soldier, number);
        }
        // soldierToNumber.forEach((soldier, number) -> source.getSoldierArmy().removeSoldier(soldier, number));
        //todo: function to cost food unit
        
    }

    public void mergeWith(AttackOrder other) {
        other.getSoldierToNumber().entrySet().stream()
        .filter(entry -> this.getDestinationName().equals(other.getDestinationName()))
        .forEach(entry -> soldierToNumber.merge(entry.getKey(), entry.getValue(), Integer::sum));
    }

    




    
    
}
