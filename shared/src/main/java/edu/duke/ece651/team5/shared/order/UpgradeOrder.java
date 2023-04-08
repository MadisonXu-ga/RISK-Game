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
    public final static ArrayList<Integer> researchConsumeCost =
            new ArrayList<>(Arrays.asList(0, 3, 11, 30, 55, 90, 140));
    String territoryName;
    // soldier -> int / target level
    protected Map<Pair<Soldier, Integer>, SoldierLevel> soldierToUpgrade;
    Player player;

    public UpgradeOrder(String territoryName,
                        Map<Pair<Soldier, Integer>, SoldierLevel> soldierToUpgrade,
                        Player player) {
        this.territoryName = territoryName;
        this.soldierToUpgrade = soldierToUpgrade;
        this.player = player;
    }

    public Player getPlayer(){
        return player;
    }

    public Map<Pair<Soldier, Integer>, SoldierLevel> getSoldierToUpgrade(){
        return soldierToUpgrade;
    }

    @Override
    public void execute(RISKMap map) {
        // todo: check enough resource
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
                            researchConsumeCost.get(targetLevel.ordinal() - currLevel.ordinal() * count)
                    );
                });
    }
}
