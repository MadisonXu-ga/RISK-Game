package edu.duke.ece651.team5.shared;

import edu.duke.ece651.team5.shared.*;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.order.AttackOrder;
import edu.duke.ece651.team5.shared.order.MoveOrder;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class ActionTest {
  // @Test
  // void testGetAttackOrders() {
  // Action act = createAction();
  // ArrayList<Integer> expected = new ArrayList<>();
  // // expected.add(3);
  // // expected.add(4);
  // assertEquals(expected, act.getAttackOrders());
  // }

  // @Test
  // void testGetMoveOrders() {
  // Action act = createAction();
  // ArrayList<MoveOrder> expected = new ArrayList<>();
  // Map<Soldier, Integer> soldiers1 = new HashMap<>();
  // Player green = new Player("green");
  // expected.add(new MoveOrder("A", "B",new SoldierArmy(soldiers1) , green));
  // expected.add(new MoveOrder("C", "D", soldiers1, green));
  // assertEquals(expected, act.getMoveOrders());
  // }

  // private Action createAction() {
  // ArrayList<AttackOrder> attackOrder = new ArrayList<>();
  // ArrayList<MoveOrder> moveOrder = new ArrayList<>();
  // Map<Soldier, Integer> soldiers1 = new HashMap<>();
  // Player green = new Player("green");
  // // attackOrder.add(3);
  // // attackOrder.add(4);
  // moveOrder.add(new MoveOrder("A", "B", soldiers1, green));
  // moveOrder.add(new MoveOrder("C", "D", soldiers1, green));
  // return new Action(attackOrder, moveOrder);

  // }
}
