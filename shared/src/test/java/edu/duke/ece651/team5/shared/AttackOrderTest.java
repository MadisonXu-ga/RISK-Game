package edu.duke.ece651.team5.shared;



import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.order.AttackOrder;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
        attackOrder1 = new AttackOrder("source1", "destination1", soldiers1, new Player("Player 1"));

        Map<Soldier, Integer> soldiers2 = new HashMap<>();
        soldiers2.put(new Soldier(SoldierLevel.INFANTRY), 2);
        soldiers2.put(new Soldier(SoldierLevel.ARTILLERY), 1);
        attackOrder2 = new AttackOrder("source2", "destination1", soldiers2, new Player("Player 1"));

        attackOrder3 = new AttackOrder("source2", "destination1", soldiers1, new Player("Player 2"));
    }

    @Test
    void testMergeWith() {
        attackOrder1.mergeWith(attackOrder2);
        Map<Soldier, Integer> expectedSoldiers = new HashMap<>();
        expectedSoldiers.put(new Soldier(SoldierLevel.INFANTRY), 7);
        expectedSoldiers.put(new Soldier(SoldierLevel.CAVALRY), 3);
        expectedSoldiers.put(new Soldier(SoldierLevel.ARTILLERY), 1);
        Assertions.assertEquals(expectedSoldiers, attackOrder1.getSoldierToNumber());
    }

    @Test
    public void testCompareTo() {
  
        // test comparison of orders with different players
        assertTrue(attackOrder1.compareTo(attackOrder3) < 0); // Alice < Bob
        assertTrue(attackOrder3.compareTo(attackOrder1) > 0); // Bob > Alice
        
        // test comparison of orders with the same player
        assertTrue(attackOrder1.compareTo(attackOrder2) > 0); // 7 > 3
        assertTrue(attackOrder2.compareTo(attackOrder1) < 0); // 3 < 7
        
    }
}
