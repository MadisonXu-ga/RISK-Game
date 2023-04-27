package edu.duke.ece651.team5.shared;

import edu.duke.ece651.team5.shared.datastructure.Pair;
import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.order.UpgradeOrder;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpgradeOrderTest {
    private UpgradeOrder upgradeOrder;
    private Player player;
    private RISKMap map;

    @BeforeEach
    public void setUp() {
        Map<String, Territory> territories = new HashMap<>();
        territories.put("Territory 1", new Territory(1, "Territory 1", new Player("Player 1")));
        map = new RISKMap(territories, null);
        Territory territory = map.getTerritoryByName("Territory 1");
        territory.produceResource(new Resource(ResourceType.TECHNOLOGY));
        territory.produceResource(new Resource(ResourceType.TECHNOLOGY));
        territory.produceResource(new Resource(ResourceType.TECHNOLOGY));
        SoldierArmy soldierArmy = territory.getSoldierArmy();
        soldierArmy.addSoldier(new Soldier(SoldierLevel.INFANTRY), 3); // total 4
        Map<Pair<Soldier, Integer>, SoldierLevel> soldierToUpgrade = new HashMap<>();
        soldierToUpgrade.put(new Pair<>(new Soldier(SoldierLevel.INFANTRY), 1), SoldierLevel.CAVALRY);
        soldierToUpgrade.put(new Pair<>(new Soldier(SoldierLevel.INFANTRY), 2), SoldierLevel.ARTILLERY);
        player = territory.getOwner();
        upgradeOrder = new UpgradeOrder("Territory 1", soldierToUpgrade, player);

    }

    @Test
    public void testExecute() {
        upgradeOrder.execute(map);
        int resourceCount = player.getResourceCount(new Resource(ResourceType.TECHNOLOGY));
        Territory t = map.getTerritoryByName("Territory 1");
        SoldierArmy soldierArmy = t.getSoldierArmy();
        // assertEquals(5, resourceCount);

        assertEquals(0, soldierArmy.getSoldierCount(new Soldier(SoldierLevel.INFANTRY)));
        assertEquals(1, soldierArmy.getSoldierCount(new Soldier(SoldierLevel.CAVALRY)));
        assertEquals(2, soldierArmy.getSoldierCount(new Soldier(SoldierLevel.ARTILLERY)));

    }

    @Test
    public void testExecute2() {
        upgradeOrder.execute(map, player);
        int resourceCount = player.getResourceCount(new Resource(ResourceType.TECHNOLOGY));
        Territory t = map.getTerritoryByName("Territory 1");
        SoldierArmy soldierArmy = t.getSoldierArmy();
        assertEquals(10, resourceCount);

        assertEquals(0, soldierArmy.getSoldierCount(new Soldier(SoldierLevel.INFANTRY)));
        assertEquals(1, soldierArmy.getSoldierCount(new Soldier(SoldierLevel.CAVALRY)));
        assertEquals(2, soldierArmy.getSoldierCount(new Soldier(SoldierLevel.ARTILLERY)));

    }
}
