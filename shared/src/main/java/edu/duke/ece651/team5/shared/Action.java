package edu.duke.ece651.team5.shared;

// import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

import edu.duke.ece651.team5.shared.order.AttackOrder;
import edu.duke.ece651.team5.shared.order.MoveOrder;

//todo change Integer to AttackOrder object and moveOrder object
public class Action implements Serializable {
  // @Serial
  private static final long serialVersionUID = -5398478298293768168L;
  ArrayList<AttackOrder> attackOrders;
  ArrayList<MoveOrder> moveOrders;

  /**
   * @param attackOrders ArrayList<AttackOrder>
   * @param moveOrders   ArrayList<MoveOrder>
   */
  public Action(ArrayList<AttackOrder> attackOrders, ArrayList<MoveOrder> moveOrders) {
    this.attackOrders = attackOrders;
    this.moveOrders = moveOrders;
  }

  /**
   * @return ArrayList<AttackOrder> attackOrders
   */
  public ArrayList<AttackOrder> getAttackOrders() {
    return attackOrders;
  }

  /**
   * @return ArrayList<MoveOrder> moveOrders
   */
  public ArrayList<MoveOrder> getMoveOrders() {
    return moveOrders;
  }

}
