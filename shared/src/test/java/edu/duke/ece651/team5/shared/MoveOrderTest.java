package edu.duke.ece651.team5.shared;

import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.order.MoveOrder;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoveOrderTest {
    RISKMap map;
    MoveOrder move;

    Territory territory1;
    Territory territory4;
    @BeforeEach
    public void setUp() {
        Map<String, Territory> territories = new HashMap<>();
        territories.put("Territory 1", new Territory(1, "Territory 1", new Player("Player 1")));
        territories.put("Territory 2", new Territory(2, "Territory 2", new Player("Player 2")));
        territories.put("Territory 3", new Territory(3, "Territory 3", new Player("Player 2")));
        territories.put("Territory 4", new Territory(4, "Territory 4", new Player("Player 1")));

        HashMap<Integer, List<RISKMap.Edge>> connections = new HashMap<>();
        connections.put(1, Arrays.asList(new RISKMap.Edge(1, 2, 5), new RISKMap.Edge(1, 4, 1)));
        connections.put(2, Arrays.asList(new RISKMap.Edge(2, 1, 5), new RISKMap.Edge(2, 4, 3)));
        connections.put(3, Arrays.asList(new RISKMap.Edge(3, 4, 4)));
        connections.put(4, Arrays.asList(new RISKMap.Edge(4, 1, 1), new RISKMap.Edge(4, 2, 3), new RISKMap.Edge(4, 3, 4)));

        map = new RISKMap(territories, connections);

        territory1 = map.getTerritoryByName("Territory 1");
        territory4 = map.getTerritoryByName("Territory 4");
        SoldierArmy soldierArmy = territory1.getSoldierArmy();
        soldierArmy.addSoldier(new Soldier(SoldierLevel.INFANTRY), 3);  // total 3
        SoldierArmy toMove = new SoldierArmy();
        toMove.addSoldier(new Soldier(SoldierLevel.INFANTRY), 2);  // total 2

        move = new MoveOrder("Territory 1", "Territory 4", toMove, territory1.getOwner());
    }

    @Test
    public void execute() {
        move.execute(map);
        assertEquals(1, territory1.getSoldierArmy().getSoldierCount(new Soldier(SoldierLevel.INFANTRY)));
        assertEquals(2, territory4.getSoldierArmy().getSoldierCount(new Soldier(SoldierLevel.INFANTRY)));
    }

}
