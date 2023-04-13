package edu.duke.ece651.team5.shared.order;

import edu.duke.ece651.team5.shared.resource.*;
import edu.duke.ece651.team5.shared.datastructure.Pair;
import edu.duke.ece651.team5.shared.game.*;
import edu.duke.ece651.team5.shared.unit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * For a certain territory
 */
public class UpgradeOrder implements Order {
    public final static ArrayList<Integer> upgradeConsumeCost =
            new ArrayList<>(Arrays.asList(0, 3, 11, 30, 55, 90, 140));
    // the territory name of the territory where is upgrade is about to happen
   private String territoryName;
    // (solider, how many of this soldier) -> target level
    protected Map<Pair<Soldier, Integer>, SoldierLevel> soldierToUpgrade;
    // the player issued this order
    private Player player;

    /**
     * constructor
     * @param territoryName  the territory name of the territory where is upgrade is about to happen
     * @param soldierToUpgrade soldiers to be upgrade (solider, how many of this soldier) -> target level
     * @param player  the player issued this order
     */
    public UpgradeOrder(String territoryName,
                        Map<Pair<Soldier, Integer>, SoldierLevel> soldierToUpgrade,
                        Player player) {
        this.territoryName = territoryName;
        this.soldierToUpgrade = soldierToUpgrade;
        this.player = player;
    }

    /**
     * Getter for the player
     * @return the player issued this order
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Getter for the solider to be upgraded
     * @return the solider to be upgraded
     */
    public Map<Pair<Soldier, Integer>, SoldierLevel> getSoldierToUpgrade(){
        return soldierToUpgrade;
    }

    /**
     * the actual execute the upgrade order
     * @param map the map
     */
    @Override
    public void execute(RISKMap map) {
        Territory territory = map.getTerritoryByName(territoryName);
        soldierToUpgrade.entrySet().stream()
                .forEach(entry -> {
                    Soldier soldier = entry.getKey().getFirst();
                    int count = entry.getKey().getSecond();
                    SoldierLevel currLevel = soldier.getLevel();
                    SoldierLevel targetLevel = entry.getValue();
                    territory.getSoldierArmy().upgradeSoldier(soldier, count, targetLevel);
                    territory.getOwner().consumeResource(
                            new Resource(ResourceType.TECHNOLOGY),
                            upgradeConsumeCost.get(targetLevel.ordinal() - currLevel.ordinal()) * count
                    );
                });
    }
}
