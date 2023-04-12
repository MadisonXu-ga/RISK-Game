package edu.duke.ece651.team5.shared.utils;

import edu.duke.ece651.team5.shared.datastructure.Pair;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.order.AttackOrder;
import edu.duke.ece651.team5.shared.order.MoveOrder;
import edu.duke.ece651.team5.shared.order.ResearchOrder;
import edu.duke.ece651.team5.shared.order.UpgradeOrder;
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
        int distance = map.getShortestPathDistance(sourceName, destinationName, true);
        System.out.println(distance);
        System.out.println(totalNumberOfSoldier);
        return C * distance * totalNumberOfSoldier;
    }

    /**
     * To compute the food consume for attack order
     * @param attackOrder attack order
     * @param map the map
     * @return the food consume
     */
    public static int computeFoodConsumeForAttack(AttackOrder attackOrder, RISKMap map) {
        String sourceName = attackOrder.getSourceName();
        String destinationName = attackOrder.getDestinationName();
        SoldierArmy soldierToNumber = attackOrder.getSoldierToNumber();
        int totalNumberOfSoldier = soldierToNumber.getTotalCountSolider();
        int distance = map.getShortestPathDistance(sourceName, destinationName, false);
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
    public static int computeTechConsumeForUpgrade(UpgradeOrder upgradeOrder) {
        Map<Pair<Soldier, Integer>, SoldierLevel> soldierToUpgrade = upgradeOrder.getSoldierToUpgrade();
        return soldierToUpgrade.entrySet().stream()
                .mapToInt(entry -> {
                    Soldier soldier = entry.getKey().getFirst();
                    int count = entry.getKey().getSecond();
                    SoldierLevel currLevel = soldier.getLevel();
                    SoldierLevel targetLevel = entry.getValue();
                    return UpgradeOrder.upgradeConsumeCost.get(targetLevel.ordinal() - currLevel.ordinal()) * count;
                })
                .sum();
    }
}
