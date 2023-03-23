package edu.duke.ece651.team5.server;

import edu.duke.ece651.team5.shared.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class GameControllerTest {
  @Test
  @Disabled
  void testAssignTerritories() {
    GameController gc = new GameController();
    assertEquals(0, gc.getRiskMap().getPlayerByName("Green").getTerritories().size());
    gc.assignTerritories(3);
    assertEquals(3, gc.getRiskMap().getPlayerByName("Green").getTerritories().size());
    ArrayList<Territory> terris = new ArrayList<>();
    
  }

  @Test
  void testGetPlayerName() {

  }

  @Test
  void testGetRiskMap() {

  }

  @Test
  void testResolveUnitPlacement() {

  }

  @Test
  void testBeginFight(){
    ArrayList<AttackOrder> attackOrders = new ArrayList<>();
    attackOrders.add(new AttackOrder("Scadrial", "Narnia", 3, UnitType.SOLDIER, "Blue"));
    attackOrders.add(new AttackOrder("Mordor", "Narnia", 2, UnitType.SOLDIER, "Red"));
    // attackOrders.add(new AttackOrder("C", "1", 5, UnitType.SOLDIER));

    RISKMap map = mock(RISKMap.class);
    Territory toFight = mock(Territory.class);

    when(toFight.getName()).thenReturn("Narnia");
    when(toFight.getUnitNum(UnitType.SOLDIER)).thenReturn(3);
    when(toFight.getOwner()).thenReturn(new Player("Green"));


    GameController gc = new GameController();
    gc.assignTerritories(3);
    gc.beginFight(toFight, attackOrders);


  }
}
