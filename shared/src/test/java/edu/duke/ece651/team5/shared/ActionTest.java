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
    expected.add(3);
    expected.add(4);
    assertEquals(expected, act.getAttackOrders());
  }

  @Test
  void testGetMoveOrders() {
    Action act = createAction();
    ArrayList<MoveOrder> expected = new ArrayList<>();
    expected.add(new MoveOrder("A", "B", 3, UnitType.SOLDIER));
    expected.add(new MoveOrder("C", "D", 5, UnitType.SOLDIER));
    assertEquals(expected, act.getMoveOrders());
  }

  private Action createAction(){
    ArrayList<Integer> attackOrder = new ArrayList<>();
    ArrayList<MoveOrder> moveOrder = new ArrayList<>();
    attackOrder.add(3);
    attackOrder.add(4);
    moveOrder.add(new MoveOrder("A", "B", 3, UnitType.SOLDIER));
    moveOrder.add(new MoveOrder("C", "D", 5, UnitType.SOLDIER));
    return new Action(attackOrder, moveOrder);

  }
}
