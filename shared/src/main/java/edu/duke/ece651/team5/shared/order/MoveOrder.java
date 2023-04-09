package edu.duke.ece651.team5.shared.order;

import java.io.Serializable;

import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.resource.*;
import edu.duke.ece651.team5.shared.game.*;
import edu.duke.ece651.team5.shared.unit.*;
import edu.duke.ece651.team5.shared.constant.Constants;

public class MoveOrder extends BasicOrder implements Serializable {

    // @Serial
    private static final long serialVersionUID = -5458702819007392881L;

    public MoveOrder(String sourceName, String destinationName, SoldierArmy soldierToNumber, Player player) {
        super(sourceName, destinationName, soldierToNumber, player);
    }

    /**
     * The actual updates if an order is executed
     * move #number unit from source to dest
     * @param map the map
     */
    @Override
    public void execute(RISKMap map) {
        Territory source = map.getTerritoryByName(sourceName);
        Territory destination = map.getTerritoryByName(destinationName);
        soldierToNumber.getAllSoldiers().forEach((soldier, number) -> source.getSoldierArmy().removeSoldier(soldier, number));
        soldierToNumber.getAllSoldiers().forEach((soldier, number) -> destination.getSoldierArmy().addSoldier(soldier, number));
        // consume resource
        int distance = map.getShortestPathDistance(sourceName, destinationName);
        player.consumeResource(new Resource(ResourceType.FOOD), Constants.C * distance * soldierToNumber.getTotalCountSolider());
    }
}



