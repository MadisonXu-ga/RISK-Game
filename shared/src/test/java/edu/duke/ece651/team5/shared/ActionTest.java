package edu.duke.ece651.team5.shared;

import edu.duke.ece651.team5.shared.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class ActionTest {
  @Test
  void testGetAttackOrders() {
    Action act = createAction();
    ArrayList<Integer> expected = new ArrayList<>();
    // expected.add(3);
    // expected.add(4);
    assertEquals(expected, act.getAttackOrders());
  }

  @Test
  void testGetMoveOrders() {
    Action act = createAction();
    ArrayList<MoveOrder> expected = new ArrayList<>();
    expected.add(new MoveOrder("A", "B", 3, UnitType.SOLDIER, "Green"));
    expected.add(new MoveOrder("C", "D", 5, UnitType.SOLDIER, "Green"));
    assertEquals(expected, act.getMoveOrders());
  }

  private Action createAction(){
    ArrayList<AttackOrder> attackOrder = new ArrayList<>();
    ArrayList<MoveOrder> moveOrder = new ArrayList<>();
    // attackOrder.add(3);
    // attackOrder.add(4);
    moveOrder.add(new MoveOrder("A", "B", 3, UnitType.SOLDIER, "Green"));
    moveOrder.add(new MoveOrder("C", "D", 5, UnitType.SOLDIER, "Green"));
    return new Action(attackOrder, moveOrder);

  }
}
