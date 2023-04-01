package edu.duke.ece651.team5.shared;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class AttackOrderTest {


    private AttackOrder attackOrder1;
    private AttackOrder attackOrder2;

    @BeforeEach
    void setUp() {
        Map<Soldier, Integer> soldiers1 = new HashMap<>();
        soldiers1.put(new Soldier(SoldierType.INFANTRY, 1), 5);
        soldiers1.put(new Soldier(SoldierType.CAVALRY, 1), 3);
        attackOrder1 = new AttackOrder("source1", "destination1", soldiers1, "player1");

        Map<Soldier, Integer> soldiers2 = new HashMap<>();
        soldiers2.put(new Soldier(SoldierType.INFANTRY, 1), 2);
        soldiers2.put(new Soldier(SoldierType.ARTILLERY, 1), 1);
        attackOrder2 = new AttackOrder("source2", "destination1", soldiers2, "player1");
    }

    @Test
    void testMergeWith() {
        attackOrder1.mergeWith(attackOrder2);
        Map<Soldier, Integer> expectedSoldiers = new HashMap<>();
        expectedSoldiers.put(new Soldier(SoldierType.INFANTRY, 1), 7);
        expectedSoldiers.put(new Soldier(SoldierType.CAVALRY, 1), 3);
        expectedSoldiers.put(new Soldier(SoldierType.ARTILLERY, 1), 1);
        Assertions.assertEquals(expectedSoldiers, attackOrder1.getSoldierToNumber());
    }

    @Test
    void testCompareTo() {

    }

    @Test
    void testExecute() {

    }
}
