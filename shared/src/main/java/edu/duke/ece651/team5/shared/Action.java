package edu.duke.ece651.team5.shared;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

import edu.duke.ece651.team5.shared.order.*;

public class Action implements Serializable {
  @Serial
  private static final long serialVersionUID = -5398478298293768168L;
  ArrayList<AttackOrder> attackOrders;
  ArrayList<MoveOrder> moveOrders;
  ResearchOrder researchOrder;
  ArrayList<UpgradeOrder> upgradeOrders;

  public Action(ArrayList<AttackOrder> attackOrders, ArrayList<MoveOrder> moveOrders, ResearchOrder researchOrder,
      ArrayList<UpgradeOrder> upgradeOrders) {
    this.attackOrders = attackOrders;
    this.moveOrders = moveOrders;
    this.researchOrder = researchOrder;
    this.upgradeOrders = upgradeOrders;
  }

  public ArrayList<AttackOrder> getAttackOrders() {
    return attackOrders;
  }

  public ArrayList<MoveOrder> getMoveOrders() {
    return moveOrders;
  }

  public ResearchOrder getResearchOrder() {
    return researchOrder;
  }

  public ArrayList<UpgradeOrder> getUpgradeOrders() {
    return upgradeOrders;
  }
}
