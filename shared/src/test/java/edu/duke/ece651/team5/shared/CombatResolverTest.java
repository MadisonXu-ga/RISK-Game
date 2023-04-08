package edu.duke.ece651.team5.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.game.CombatResolver;
import edu.duke.ece651.team5.shared.game.Game;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.order.AttackOrder;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;

public class CombatResolverTest {
    private AttackOrder order1;
    private AttackOrder order2;
    private AttackOrder order3;
    private AttackOrder order4;
    private CombatResolver resolver = new CombatResolver();

    private Territory territory;
    private Game game;
    private Player player1 = new Player("Player 1");
    private Player player2 = new Player("Player 2");
    


    @BeforeEach
    private void setUp(){
        
        List<Player> players = new ArrayList<>(Arrays.asList(player1, player2));
        RISKMap map = new RISKMap("test_map.txt");
        map.printMap();
        game = new Game(players, map);
        territory = map.getTerritoryById(1);
        territory.setOwner(player1);
        territory.getSoldierArmy().addSoldier(new Soldier(SoldierLevel.CAVALRY), 2);

        Map<Soldier, Integer> soldiers1 = new HashMap<>();
        soldiers1.put(new Soldier(SoldierLevel.INFANTRY), 5);
        soldiers1.put(new Soldier(SoldierLevel.CAVALRY), 3);
        order1 = new AttackOrder("source1", "destination1", soldiers1, new Player("Player 1"));
        order2 = new AttackOrder("source1", "destination1", soldiers1, new Player("Player 1"));
        order3 = new AttackOrder("source1", "destination1", soldiers1, new Player("Player 2"));
        order4 = new AttackOrder("source1", map.getTerritoryById(1).getName(), soldiers1, new Player("Player 2"));

       
    }


    @Test
    void testBeginFight() {
        List<AttackOrder> attackOrders = new ArrayList<>(Arrays.asList(order4));
        resolver.beginFight(territory, attackOrders, game);
        assertEquals(player2, territory.getOwner());
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
