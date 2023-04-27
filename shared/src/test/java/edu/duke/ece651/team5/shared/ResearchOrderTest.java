package edu.duke.ece651.team5.shared;

import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;
import edu.duke.ece651.team5.shared.game.*;
import edu.duke.ece651.team5.shared.order.ResearchOrder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ResearchOrderTest {
    private ResearchOrder researchOrder;
    private Player player;
    private Game game;
    private RISKMap map;

    @BeforeEach
    public void setUp() {
        Territory territory = new Territory(1, "Territory 1", new Player("Player 1"));
        player = new Player("Player 1");
        List<Player> players = new ArrayList<>(Arrays.asList(player));

        Map<String, Territory> territories = new HashMap<>();
        territories.put("Territory 1", territory);
        map = new RISKMap(territories, null);
        Game game = new Game(players, map);

        territory.setOwner(player);
        territory.produceResource(new Resource(ResourceType.FOOD));
        territory.produceResource(new Resource(ResourceType.TECHNOLOGY));
        territory.produceResource(new Resource(ResourceType.TECHNOLOGY));
        territory.produceResource(new Resource(ResourceType.TECHNOLOGY));

        System.out.println(player.getResourceCount(new Resource(ResourceType.TECHNOLOGY)));

        researchOrder = new ResearchOrder(player, game);

    }

    @Test
    void getPlayer() {
        assertEquals(player, researchOrder.getPlayer());
    }

    @Test
    void execute() {
        researchOrder.execute(map);
        assertEquals(1, player.getCurrTechnologyLevel());
        assertEquals(15, player.getResourceCount(new Resource(ResourceType.TECHNOLOGY)));
    }

    
    @Test
    void execute2() {
        researchOrder.execute(map, player);
        assertEquals(1, player.getCurrTechnologyLevel());
        // assertEquals(10, player.getResourceCount(new
        // Resource(ResourceType.TECHNOLOGY)));
    }
}
