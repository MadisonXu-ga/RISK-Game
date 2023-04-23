package edu.duke.ece651.team5.shared.rulechecker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.datastructure.Pair;
import edu.duke.ece651.team5.shared.game.*;
import edu.duke.ece651.team5.shared.order.AllianceOrder;


public class AllianceCheckerTest {
    private List<AllianceOrder> orders;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private AllianceChecker checker;


    @BeforeEach
    private void setUp(){
        checker = new AllianceChecker();
        orders = new ArrayList<>();
        player1 = new Player("A");
        player2 = new Player("B");
        player3 = new Player("C");
        player4 = new Player("D");
    }


    @Test
    void testNoAlliance(){
        orders.add(new AllianceOrder(player1, player2));
        orders.add(new AllianceOrder(player2, player3));
        orders.add(new AllianceOrder(player4, player3));
        Set<Set<Player>> res = new HashSet<>();
        res = checker.checkAlliance(orders);
        Set<Set<Player>> expected = new HashSet<>();
        assertEquals(expected, res);
    }





    @Test
    void testAlliancePlayer() {
        orders.add(new AllianceOrder(player1, player2));
        orders.add(new AllianceOrder(player4, player2));
        orders.add(new AllianceOrder(player3, player1));
        orders.add(new AllianceOrder(player2, player1));
        Set<Set<Player>>  res = new HashSet<>();
        res = checker.checkAlliance(orders);
        Set<Set<Player>>  expected =new HashSet<>();
        expected.add(new HashSet<Player>(Arrays.asList(player1, player2)));
        assertEquals(expected, res);
    }
}
