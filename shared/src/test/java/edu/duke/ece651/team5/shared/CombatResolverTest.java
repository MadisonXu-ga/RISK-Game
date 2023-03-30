package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

public class CombatResolverTest {
    private List<AttackOrder> attackOrders;
    private CombatResolver combatResolver;

    @BeforeEach
    public void setUp() {
        combatResolver = new CombatResolver();
        // Create some sample AttackOrders for testing
        Map<Soldier, Integer> soldiers1 = new HashMap<>();
        soldiers1.put(new Soldier(SoldierType.INFANTRY, 1), 5);
        soldiers1.put(new Soldier(SoldierType.CAVALRY, 1), 3);
        soldiers1.put(new Soldier(SoldierType.ARTILLERY, 1), 2);
        AttackOrder order1 = new AttackOrder("Territory1", "Territory2", soldiers1, "Player1");

        Map<Soldier, Integer> soldiers2 = new HashMap<>();
        soldiers2.put(new Soldier(SoldierType.INFANTRY, 1), 3);
        soldiers2.put(new Soldier(SoldierType.CAVALRY, 1), 2);
        soldiers2.put(new Soldier(SoldierType.ARTILLERY, 1), 1);
        AttackOrder order2 = new AttackOrder("Territory3", "Territory4", soldiers2, "Player2");

        Map<Soldier, Integer> soldiers3 = new HashMap<>();
        soldiers3.put(new Soldier(SoldierType.INFANTRY, 1), 10);
        soldiers3.put(new Soldier(SoldierType.CAVALRY, 2), 6);
        soldiers3.put(new Soldier(SoldierType.ARTILLERY, 1), 4);
        AttackOrder order3 = new AttackOrder("Territory5", "Territory2", soldiers3, "Player1");

        Map<Soldier, Integer> soldiers4 = new HashMap<>();
        soldiers4.put(new Soldier(SoldierType.INFANTRY, 1), 4);
        soldiers4.put(new Soldier(SoldierType.CAVALRY, 1), 4);
        soldiers4.put(new Soldier(SoldierType.ARTILLERY, 1), 4);
        AttackOrder order4 = new AttackOrder("Territory7", "Territory8", soldiers4, "Player3");

        // Add the AttackOrders to the list
        attackOrders = new ArrayList<>();
        attackOrders.add(order1);
        attackOrders.add(order2);
        attackOrders.add(order3);
        attackOrders.add(order4);
    }

    @Test
    void testMergeOrderForOnePlayer() {
        List<AttackOrder> afterMerge = combatResolver.mergeOrderByTerriForOnePlayer(attackOrders);
        assertEquals(3, afterMerge.size());
        // System.out.println(afterMerge.get(1).getSoldierToNumber());
        assertEquals(4, afterMerge.get(1).getSoldierToNumber().size());
        assertEquals(15, afterMerge.get(1).getSoldierToNumber().get(new Soldier(SoldierType.INFANTRY, 1)));
        assertEquals(3, afterMerge.get(1).getSoldierToNumber().get(new Soldier(SoldierType.CAVALRY, 1)));
        assertEquals(6, afterMerge.get(1).getSoldierToNumber().get(new Soldier(SoldierType.ARTILLERY, 1)));
    }
}
