package edu.duke.ece651.team5.shared;

// import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;


//todo change Integer to AttackOrder object and moveOrder object
public class Action implements Serializable {
  // @Serial
  private static final long serialVersionUID = -5398478298293768168L;
  ArrayList<AttackOrder> attackOrders;
  ArrayList<MoveOrder> moveOrders;

  public Action(ArrayList<AttackOrder> attackOrders, ArrayList<MoveOrder> moveOrders){
    this.attackOrders = attackOrders;
    this.moveOrders = moveOrders;
  }

  public ArrayList<AttackOrder> getAttackOrders(){
    return attackOrders;
  }

  public ArrayList<MoveOrder> getMoveOrders(){
    return moveOrders;
  }

}
