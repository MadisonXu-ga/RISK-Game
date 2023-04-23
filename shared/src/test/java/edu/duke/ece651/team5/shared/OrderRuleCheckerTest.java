package edu.duke.ece651.team5.shared;

import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.order.MoveOrder;
import edu.duke.ece651.team5.shared.rulechecker.MoveOwnershipRuleChecker;
import edu.duke.ece651.team5.shared.rulechecker.MovePathWithSameOwnerRuleChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OrderRuleCheckerTest {
    RISKMap map;
    MoveOrder move1;
    MoveOrder move2;
    MoveOrder move3;
    @BeforeEach
    public void setUp() {
        Map<String, Territory> territories = new HashMap<>();
        territories.put("Territory 1", new Territory(1, "Territory 1", new Player("Player 1")));
        territories.put("Territory 2", new Territory(2, "Territory 2", new Player("Player 2")));
        territories.put("Territory 3", new Territory(3, "Territory 3", new Player("Player 2")));
        territories.put("Territory 4", new Territory(4, "Territory 4", new Player("Player 1")));

        HashMap<Integer, List<RISKMap.Edge>> connections = new HashMap<>();
        connections.put(1, Arrays.asList(new RISKMap.Edge(1, 2, 5),
                new RISKMap.Edge(1, 4, 1)));
        connections.put(2, Arrays.asList(new RISKMap.Edge(2, 1, 5),
                new RISKMap.Edge(2, 4, 3)));
        connections.put(3, Arrays.asList(new RISKMap.Edge(3, 4, 4)));
        connections.put(4, Arrays.asList(new RISKMap.Edge(4, 1, 1),
                new RISKMap.Edge(4, 2, 3),
                new RISKMap.Edge(4, 3, 4)));

        map = new RISKMap(territories, connections);
        Territory territory1 = map.getTerritoryByName("Territory 1");

        move1 = new MoveOrder("Territory 1", "Territory 2", null, territory1.getOwner());
        move2 = new MoveOrder("Territory 1", "Territory 4", null, territory1.getOwner());
        move3 = new MoveOrder("Territory 3", "Territory 2", null, territory1.getOwner());
    }

    @Test
    public void checkMyRule() {
        MoveOwnershipRuleChecker moveOwnershipRuleChecker = new MoveOwnershipRuleChecker(null);
        MovePathWithSameOwnerRuleChecker movePathWithSameOwnerRuleChecker = new MovePathWithSameOwnerRuleChecker(moveOwnershipRuleChecker);
        assertNull(movePathWithSameOwnerRuleChecker.checkOrder(move2, map));
        assertNull(moveOwnershipRuleChecker.checkOrder(move2, map));
        assertEquals("You cannot move between territories that do not belong to you or your alliance", moveOwnershipRuleChecker.checkOrder(move1, map));
        assertEquals("You cannot move between territories that do not belong to you or your alliance", moveOwnershipRuleChecker.checkOrder(move3, map));
    }
}
