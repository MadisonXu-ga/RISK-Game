package edu.duke.ece651.team5.shared;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MoveOrder extends BasicOrder implements Serializable {

    // @Serial
    private static final long serialVersionUID = -5458702819007392881L;

    public MoveOrder(String sourceName, String destinationName, Map<Soldier, Integer> soldiers, String playerName) {
        super(sourceName, destinationName, soldiers, playerName);
    }

    // @Override
    // public boolean equals(Object o) {
    //     if (this == o) return true;
    //     if (o == null || getClass() != o.getClass()) return false;
    //     MoveOrder m = (MoveOrder) o;
    //     return sourceName.equals(m.sourceName) && destinationName.equals(m.destinationName) && number == m.number;
    // }

    /**
     * The actual updates if an order is executed
     * move #number unit from source to dest
     * @param map the map
     */
    @Override
    public void execute(RISKMap map) {
        Territory source = map.getTerritoryByName(sourceName);
        Territory destination = map.getTerritoryByName(destinationName);
        soldierToNumber.forEach((soldier, number) -> source.getSoldierArmy().removeSoldier(soldier, number));
        soldierToNumber.forEach((soldier, number) -> destination.getSoldierArmy().addSoldier(soldier, number));
    }
}



