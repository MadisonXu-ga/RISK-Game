package edu.duke.ece651.team5.shared;

import java.io.Serializable;
import java.util.Map;

public class MoveOrder extends BasicOrder implements Serializable {

    // @Serial
    private static final long serialVersionUID = -5458702819007392881L;

    public MoveOrder(String sourceName, String destinationName, Map<Soldier, Integer> soldiers, Player playerName) {
        super(sourceName, destinationName, soldiers, playerName);
    }

    /**
     * The actual updates if an order is executed
     * move #number unit from source to dest
     * 
     * @param map the map
     */
    @Override
    public void execute(RISKMap map) {
        Territory source = map.getTerritoryByName(sourceName);
        Territory destination = map.getTerritoryByName(destinationName);
        soldierToNumber.forEach((soldier, number) -> source.getSoldierArmy().removeSoldier(soldier, number));
        soldierToNumber.forEach((soldier, number) -> destination.getSoldierArmy().addSoldier(soldier, number));
        // consume resource
        int distance = map.getShortestPathDistance(sourceName, destinationName);
        // player.consumeResource(new Resource(ResourceType.FOOD),
        // C * distance * );
    }
}
