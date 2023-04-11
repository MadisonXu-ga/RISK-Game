package edu.duke.ece651.team5.shared.utils;

import edu.duke.ece651.team5.shared.datastructure.Pair;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.order.AttackOrder;
import edu.duke.ece651.team5.shared.order.MoveOrder;
import edu.duke.ece651.team5.shared.order.ResearchOrder;
import edu.duke.ece651.team5.shared.order.UpgradeOrder;
import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;

import java.util.Map;

import static edu.duke.ece651.team5.shared.constant.Constants.C;

public class ResourceConsumeCalculator {
    /**
     * To compute the food consume for move order
     * @param moveOrder move order
     * @param map the map
     * @return the food consume
     */
    public static int computeFoodConsumeForMove(MoveOrder moveOrder, RISKMap map) {
        String sourceName = moveOrder.getSourceName();
        String destinationName = moveOrder.getDestinationName();
        SoldierArmy soldierToNumber = moveOrder.getSoldierToNumber();
        int totalNumberOfSoldier = soldierToNumber.getTotalCountSolider();
        int distance = map.getShortestPathDistance(sourceName, destinationName);
        return C * distance * totalNumberOfSoldier;
    }

    /**
     * To compute the food consume for move order
     * @param attackOrder attack order
     * @param map the map
     * @return the food consume
     */
    public static int computeFoodConsumeForAttack(AttackOrder attackOrder, RISKMap map) {
        String sourceName = attackOrder.getSourceName();
        String destinationName = attackOrder.getDestinationName();
        SoldierArmy soldierToNumber = attackOrder.getSoldierToNumber();
        int totalNumberOfSoldier = soldierToNumber.getTotalCountSolider();
        int distance = map.getShortestPathDistance(sourceName, destinationName);
        System.out.println(distance);
        return C * distance * totalNumberOfSoldier;
    }

    /**
     * To compute the tech consume for research order
     * @param researchOrder research order
     * @return the tech consume
     */
    public static int computeTechConsumeForResearch(ResearchOrder researchOrder) {
        Player player = researchOrder.getPlayer();
        int currTechnologyLevel = player.getCurrTechnologyLevel();
        return ResearchOrder.researchConsumeCost.get(currTechnologyLevel);
    }

    /**
     * To compute the tech consume for upgrade order
     * @param upgradeOrder research order
     * @return the tech consume
     */
    public static int computeTechConsumeForUpgrade(UpgradeOrder upgradeOrder, RISKMap map) {
        String territoryName = upgradeOrder.getTerritoryName();
        Territory territory = map.getTerritoryByName(territoryName);
        Map<Pair<Soldier, Integer>, SoldierLevel> soldierToUpgrade = upgradeOrder.getSoldierToUpgrade();
        int cost = soldierToUpgrade.entrySet().stream()
                .mapToInt(entry -> {
                    Soldier soldier = entry.getKey().getFirst();
                    int count = entry.getKey().getSecond();
                    SoldierLevel currLevel = soldier.getLevel();
                    SoldierLevel targetLevel = entry.getValue();
                    int resourceCost = UpgradeOrder.upgradeConsumeCost.get(targetLevel.ordinal() - currLevel.ordinal() * count);
                    territory.getOwner().consumeResource(new Resource(ResourceType.TECHNOLOGY), resourceCost);
                    return resourceCost;
                })
                .sum();
        return cost;
    }
}
