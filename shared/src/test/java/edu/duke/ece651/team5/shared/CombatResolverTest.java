package edu.duke.ece651.team5.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.duke.ece651.team5.shared.game.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.order.AttackOrder;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;

public class CombatResolverTest {
    private AttackOrder order1;
    private AttackOrder order2;
    private AttackOrder order3;
    private AttackOrder order4;
    private CombatResolver resolver = new CombatResolver();

    private Territory territory;
    private Game game;

    @BeforeEach
    private void setUp() {
        Map<String, Territory> territories = new HashMap<>();
        territories.put("Territory 1", new Territory(1, "Territory 1"));
        territories.put("Territory 2", new Territory(2, "Territory 2"));
        territories.put("Territory 3", new Territory(3, "Territory 3"));
        territories.put("Territory 4", new Territory(4, "Territory 4"));

        HashMap<Integer, List<RISKMap.Edge>> connections = new HashMap<>();
        connections.put(1, Arrays.asList(new RISKMap.Edge(1, 2, 5),
                new RISKMap.Edge(1, 4, 1)));
        connections.put(2, Arrays.asList(new RISKMap.Edge(2, 1, 5),
                new RISKMap.Edge(2, 4, 3)));
        connections.put(3, Arrays.asList(new RISKMap.Edge(3, 4, 4)));
        connections.put(4, Arrays.asList(new RISKMap.Edge(4, 1, 1),
                new RISKMap.Edge(4, 2, 3),
                new RISKMap.Edge(4, 3, 4)));

        RISKMap map = new RISKMap(territories, connections);
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        List<Player> players = new ArrayList<>(Arrays.asList(player1, player2));
        // RISKMap map = new RISKMap("test_map.txt");
        map.printMap();
        game = new Game(players, map);
        territory = map.getTerritoryById(1);
        territory.setOwner(player1);
        territory.getSoldierArmy().addSoldier(new Soldier(SoldierLevel.CAVALRY), 2);
        player1.addTerritory(territory);

        map.getTerritoryById(2).setOwner(player1);
        player1.addTerritory(map.getTerritoryById(2));

        Map<Soldier, Integer> soldiers1 = new HashMap<>();
        soldiers1.put(new Soldier(SoldierLevel.INFANTRY), 5);
        soldiers1.put(new Soldier(SoldierLevel.CAVALRY), 3);
        SoldierArmy army1 = new SoldierArmy(soldiers1);
        order1 = new AttackOrder("source1", "destination1", army1, new Player("Player 1"));
        order2 = new AttackOrder("source1", "destination1", army1, new Player("Player 1"));
        order3 = new AttackOrder("source1", "destination1", army1, new Player("Player 2"));
        order4 = new AttackOrder("source1", map.getTerritoryById(1).getName(), army1, new Player("Player 2"));

    }

    @Test
    void testResolveAttack() {
        ArrayList<AttackOrder> attackOrders = new ArrayList<>();
        HashMap<String, List<AttackOrder>> resolve = new HashMap<>();
        resolve.put("destination1", attackOrders);
        resolver.resolveAttackOrder(resolve, game);
        assertEquals("Player 1", territory.getOwner().getName());
    }

    @Test
    void testBeginFight() {
        List<AttackOrder> attackOrders = new ArrayList<>(Arrays.asList(order4));
        resolver.beginFight(territory, attackOrders, game);
        assertEquals("Player 2", territory.getOwner().getName());
        assertEquals(1, game.getPlayeryByName("Player 1").getTerritories().size());
        assertEquals(1, game.getPlayeryByName("Player 2").getTerritories().size());
    }

    @Test
    void testMergeOrderByTerriForOnePlayer() {
        List<AttackOrder> attackOrders = new ArrayList<>(Arrays.asList(order1, order2, order3, order4));
        List<AttackOrder> mergedOrders = resolver.mergeOrderByTerriForOnePlayer(attackOrders);

        assertEquals(2, mergedOrders.size());
        assertTrue(mergedOrders.contains(order1));
        assertTrue(mergedOrders.contains(order3));
    }

    @Test
    void testMergeOrderByTerritory() {
        List<AttackOrder> attackOrders = new ArrayList<>(Arrays.asList(order1, order3));
        Map<String, List<AttackOrder>> mergedOrders = resolver.mergeOrderByTerritory(attackOrders);
        assertEquals(1, mergedOrders.size());
        assertTrue(mergedOrders.containsKey("destination1"));
    }

}
