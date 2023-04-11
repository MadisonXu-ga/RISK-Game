package edu.duke.ece651.team5.shared;

import edu.duke.ece651.team5.shared.datastructure.Pair;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.order.AttackOrder;
import edu.duke.ece651.team5.shared.order.MoveOrder;
import edu.duke.ece651.team5.shared.order.ResearchOrder;
import edu.duke.ece651.team5.shared.order.UpgradeOrder;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import edu.duke.ece651.team5.shared.utils.ResourceConsumeCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceConsumeCalculatorTest {
    RISKMap map;
    Territory territory1;
    Territory territory4;
    Territory territory2;
    Player player;
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
        connections.put(4, Arrays.asList(new RISKMap.Edge(4, 1, 1), new RISKMap.Edge(4, 2, 3), new RISKMap.Edge(4, 3, 4)));

        map = new RISKMap(territories, connections);

        territory1 = map.getTerritoryByName("Territory 1");
        territory2 = map.getTerritoryByName("Territory 2");
        territory4 = map.getTerritoryByName("Territory 4");
        player = territory1.getOwner();
    }
    @Test
    void computeFoodConsumeForMove() {
        SoldierArmy soldierArmy = territory1.getSoldierArmy();
        soldierArmy.addSoldier(new Soldier(SoldierLevel.INFANTRY), 3);  // total 3
        SoldierArmy toMove = new SoldierArmy();
        toMove.addSoldier(new Soldier(SoldierLevel.INFANTRY), 2);  // total 2
        MoveOrder move = new MoveOrder("Territory 1", "Territory 4", toMove, territory1.getOwner());
        assertEquals(10, ResourceConsumeCalculator.computeFoodConsumeForMove(move, map));
    }

    @Test
    void computeFoodConsumeForAttack() {
        SoldierArmy soldierArmy = territory1.getSoldierArmy();
        soldierArmy.addSoldier(new Soldier(SoldierLevel.INFANTRY), 3);  // total 3
        SoldierArmy toAttack = new SoldierArmy();
        toAttack.addSoldier(new Soldier(SoldierLevel.INFANTRY), 2);  // total 2
        AttackOrder attack = new AttackOrder("Territory 1", "Territory 2", toAttack, territory1.getOwner());
        assertEquals(40, ResourceConsumeCalculator.computeFoodConsumeForAttack(attack, map));
    }

    @Test
    void computeTechConsumeForResearch() {
        ResearchOrder research = new ResearchOrder(player);
        assertEquals(20, ResourceConsumeCalculator.computeTechConsumeForResearch(research));
    }

    @Test
    void computeTechConsumeForUpgrade() {
        SoldierArmy soldierArmy = territory1.getSoldierArmy();
        soldierArmy.addSoldier(new Soldier(SoldierLevel.INFANTRY), 3); // total 4
        Map<Pair<Soldier, Integer>, SoldierLevel> soldierToUpgrade = new HashMap<>();
        soldierToUpgrade.put(new Pair<>(new Soldier(SoldierLevel.INFANTRY), 1), SoldierLevel.CAVALRY);
        soldierToUpgrade.put(new Pair<>(new Soldier(SoldierLevel.INFANTRY), 2) ,SoldierLevel.ARTILLERY);
        player = territory1.getOwner();
        UpgradeOrder upgrade = new UpgradeOrder("Territory 1", soldierToUpgrade, player);
        assertEquals(25, ResourceConsumeCalculator.computeTechConsumeForUpgrade(upgrade));
    }
}
