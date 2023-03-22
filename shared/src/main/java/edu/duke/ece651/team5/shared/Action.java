package edu.duke.ece651.team5.shared;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import edu.duke.ece651.team5.shared.*;

//todo change Integer to AttackOrder object and moveOrder object
public class Action implements Serializable {
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
