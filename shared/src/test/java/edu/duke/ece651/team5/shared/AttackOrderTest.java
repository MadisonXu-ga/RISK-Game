package edu.duke.ece651.team5.shared;



import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.game.*;
import edu.duke.ece651.team5.shared.order.AttackOrder;
import edu.duke.ece651.team5.shared.unit.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

public class AttackOrderTest {
    private AttackOrder attackOrder1;
    private AttackOrder attackOrder2;
    private AttackOrder attackOrder3;

    


    @BeforeEach
    void setUp() {
        Map<Soldier, Integer> soldiers1 = new HashMap<>();
        soldiers1.put(new Soldier(SoldierLevel.INFANTRY), 5);
        soldiers1.put(new Soldier(SoldierLevel.CAVALRY), 3);
        SoldierArmy army1 = new SoldierArmy(soldiers1);
        attackOrder1 = new AttackOrder("source1", "destination1", army1, new Player("Player 1"));

        Map<Soldier, Integer> soldiers2 = new HashMap<>();
        soldiers2.put(new Soldier(SoldierLevel.INFANTRY), 2);
        soldiers2.put(new Soldier(SoldierLevel.ARTILLERY), 1);
        SoldierArmy army2 = new SoldierArmy(soldiers2);
        attackOrder2 = new AttackOrder("source2", "destination1", army2, new Player("Player 1"));

        attackOrder3 = new AttackOrder("source2", "destination1", army1, new Player("Player 2"));
    }

    @Test
    void testMergeWith() {
        attackOrder1.mergeWith(attackOrder2);
        Map<Soldier, Integer> expectedSoldiers = new HashMap<>();
        expectedSoldiers.put(new Soldier(SoldierLevel.INFANTRY), 7);
        expectedSoldiers.put(new Soldier(SoldierLevel.CAVALRY), 3);
        expectedSoldiers.put(new Soldier(SoldierLevel.ARTILLERY), 1);
        Assertions.assertEquals(expectedSoldiers, attackOrder1.getSoldierToNumber().getAllSoldiers());
    }


    @Test
    void testCompareTo() {
        assertTrue(attackOrder1.compareTo(attackOrder2) == 0);
    }

    @Test
    void testExecute() {
        String sourceName = "Source Territory";
        String destinationName = "Destination Territory";
        Map<Soldier, Integer> soldiers = new HashMap<>();
        soldiers.put(new Soldier(SoldierLevel.INFANTRY), 1);
        SoldierArmy army = new SoldierArmy(soldiers);
        Player player = new Player("Player1");

        AttackOrder attackOrder = new AttackOrder(sourceName, destinationName, army, player);

        Territory sourceTerritory = mock(Territory.class);

        RISKMap map = mock(RISKMap.class);
        when(map.getTerritoryByName(sourceName)).thenReturn(sourceTerritory);

        SoldierArmy soldierArmy = mock(SoldierArmy.class);
        when(sourceTerritory.getSoldierArmy()).thenReturn(soldierArmy);
       

        attackOrder.execute(map);
        verify(soldierArmy).removeSoldier(any(Soldier.class), anyInt());

    }

    @Test
    void testExecute2() {
        String sourceName = "Source Territory";
        String destinationName = "Destination Territory";
        Map<Soldier, Integer> soldiers = new HashMap<>();
        soldiers.put(new Soldier(SoldierLevel.INFANTRY), 1);
        SoldierArmy army = new SoldierArmy(soldiers);
        Player player = new Player("Player1");

        AttackOrder attackOrder = new AttackOrder(sourceName, destinationName, army, player);

        Territory sourceTerritory = mock(Territory.class);

        RISKMap map = mock(RISKMap.class);
        when(map.getTerritoryByName(sourceName)).thenReturn(sourceTerritory);

        SoldierArmy soldierArmy = mock(SoldierArmy.class);
        when(sourceTerritory.getSoldierArmy()).thenReturn(soldierArmy);
       

        attackOrder.execute(map, player);
        verify(soldierArmy).removeSoldier(any(Soldier.class), anyInt());

    }
}
