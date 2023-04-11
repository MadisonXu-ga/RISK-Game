package edu.duke.ece651.team5.shared;

import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.order.ResearchOrder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ResearchOrderTest {
    private ResearchOrder researchOrder;
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
        player = territory.getOwner();
        researchOrder = new ResearchOrder(player);
        researchOrder.execute(map);
    }

    @Test
    void getPlayer() {
        assertEquals(player, researchOrder.getPlayer());
    }

    @Test
    void execute() {
        assertEquals(1, player.getCurrTechnologyLevel());
        assertEquals(10, player.getResourceCount(new Resource(ResourceType.TECHNOLOGY)));
    }
}
