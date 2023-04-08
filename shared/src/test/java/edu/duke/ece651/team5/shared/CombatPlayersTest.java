package edu.duke.ece651.team5.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.game.CombatPlayers;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.order.AttackOrder;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;


public class CombatPlayersTest {
    private CombatPlayers combatPlayers;
    private AttackOrder attackOrder1;
    private AttackOrder attackOrder2;
    private AttackOrder attackOrder3;

    @BeforeEach
    public void setUp() {
        // Create test data
        Map<Soldier, Integer> soldiers1 = new HashMap<>();
        soldiers1.put(new Soldier(SoldierLevel.INFANTRY), 5);
        soldiers1.put(new Soldier(SoldierLevel.CAVALRY), 3);
        attackOrder1 = new AttackOrder("source1", "destination1", soldiers1, new Player("Player 1"));

        Map<Soldier, Integer> soldiers2 = new HashMap<>();
        soldiers2.put(new Soldier(SoldierLevel.INFANTRY), 1);
        soldiers2.put(new Soldier(SoldierLevel.ARTILLERY), 2);
        attackOrder2 = new AttackOrder("source2", "destination1", soldiers2, new Player("Player 2"));

        Map<Soldier, Integer> soldiers3 = new HashMap<>();
        soldiers3.put(new Soldier(SoldierLevel.INFANTRY), 1);
        attackOrder3 = new AttackOrder("source3", "destination1", soldiers3, new Player("Player 3"));
    
        List<AttackOrder> attackOrders = Arrays.asList(attackOrder1, attackOrder2, attackOrder3);
        combatPlayers = new CombatPlayers(attackOrders);
    }
    
    @Test
    public void testConvertToSoldier() {
        Player winner = new Player("Player 1");
        Map<Soldier, Integer> result = combatPlayers.convertToSoldier(winner);
        // Assert expected result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(attackOrder1.getSoldierToNumber(), result);
    }


    @Test
    public void testGetPlayerToBonusSoldier() {
        List<Integer> player1Bonus = new ArrayList<>(Arrays.asList(1,1,1,0,0,0,0,0));
        assertEquals(player1Bonus, combatPlayers.getPlayerToBonusSoldier().get(new Player("Player 1")));
    }

    @Test
    public void testResolveLosePlayer() {
        // Create test data
        Player loser = new Player("Player 2");
        boolean isAttack = false;
        // Execute the test
        combatPlayers.resolveLosePlayer(loser, isAttack);
        // Assert expected result
        assertEquals(2, combatPlayers.getPlayerToBonusSoldier().get(loser).size());
        assertEquals(3, combatPlayers.getPlayerToBonusSoldier().get(loser).get(1));


        Player loser2 = new Player("Player 3");
        combatPlayers.resolveLosePlayer(loser2, !isAttack);
        assertTrue(combatPlayers.isPlayerLose(loser2));
        assertEquals(2, combatPlayers.getPlayerToBonusSoldier().size());
    }
}
