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

        /**
         * constructor for all params
         * 
         * @param sourceName      name of the source territory
         * @param destinationName name of the dest territory
         * @param soldierToNumber soldier army to be manipulated on
         * @param player          the player who issued this order
         */
        public MoveOrder(String sourceName, String destinationName, SoldierArmy soldierToNumber, Player player) {
                super(sourceName, destinationName, soldierToNumber, player);
        }

        /**
         * The actual updates if an order is executed
         * move the entire soldier army from source to dest
         * 
         * @param map the map
         */
        @Override
        public void execute(RISKMap map) {
                Territory source = map.getTerritoryByName(sourceName);
                Territory destination = map.getTerritoryByName(destinationName);
                soldierToNumber.getAllSoldiers()
                                .forEach((soldier, number) -> source.getSoldierArmy().removeSoldier(soldier, number));
                soldierToNumber.getAllSoldiers()
                                .forEach((soldier, number) -> destination.getSoldierArmy().addSoldier(soldier, number));
                // consume resource
                int distance = map.getShortestPathDistance(sourceName, destinationName, true);

                Integer foodAmntInt = player.getResourceCount(new Resource(ResourceType.FOOD));
                // System.out.println("before executing food resources: " + foodAmntInt);
                player.consumeResource(new Resource(ResourceType.FOOD),
                                Constants.C * distance * soldierToNumber.getTotalCountSolider());
                Integer foodAmntInt2 = player.getResourceCount(new Resource(ResourceType.FOOD));
                // System.out.println("after executing food resources: " + foodAmntInt2);
        }

        public void execute(RISKMap map, Player targetPlayer) {
                Territory source = map.getTerritoryByName(sourceName);
                Territory destination = map.getTerritoryByName(destinationName);
                soldierToNumber.getAllSoldiers()
                                .forEach((soldier, number) -> source.getSoldierArmy().removeSoldier(soldier, number));
                soldierToNumber.getAllSoldiers()
                                .forEach((soldier, number) -> destination.getSoldierArmy().addSoldier(soldier, number));
                // consume resource
                int distance = map.getShortestPathDistance(sourceName, destinationName, true);
                targetPlayer.consumeResource(new Resource(ResourceType.FOOD),
                                Constants.C * distance * soldierToNumber.getTotalCountSolider());
        }
}
