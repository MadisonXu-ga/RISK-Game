package edu.duke.ece651.team5.server;

import edu.duke.ece651.team5.shared.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class GameControllerTest {
  @Test
  void testAssignTerritories() {
    GameController gc = new GameController();
    assertEquals(0, gc.getRiskMap().getPlayerByName("Green").getTerritories().size());
    gc.assignTerritories(3);
    //assertEquals(3, gc.getRiskMap().getPlayerByName("Green").getTerritories().size());
    ArrayList<Territory> greenTerritories = gc.getRiskMap().getPlayerByName("Green").getTerritories();
    ArrayList<Territory> blueTerritories = gc.getRiskMap().getPlayerByName("Blue").getTerritories();
    ArrayList<Territory> redTerritories = gc.getRiskMap().getPlayerByName("Red").getTerritories();

    ArrayList<Territory> greenExpected = new ArrayList<>(
            Arrays.asList(
                        new Territory("Narnia"), new Territory("Elantris"),
                        new Territory("Midkemia"), new Territory("Scadrial"),
                        new Territory("Oz"), new Territory("Roshar"),
                        new Territory("Gondor"), new Territory("Mordor")
    ));

    ArrayList<Territory> blueExpected = new ArrayList<>(
            Arrays.asList(
                    new Territory("Hogwarts"), new Territory("Thalassia"),
                    new Territory("Arathia"), new Territory("Eryndor"),
                    new Territory("Sylvaria"), new Territory("Kaelindor"),
                    new Territory("Eterna"), new Territory("Celestia")
            ));

    ArrayList<Territory> redExpected = new ArrayList<>(
            Arrays.asList(
                    new Territory("Frosthold"), new Territory("Shadowmire"),
                    new Territory("Ironcliff"), new Territory("Stormhaven"),
                    new Territory("Mythosia"), new Territory("Draconia"),
                    new Territory("Emberfall"), new Territory("Verdantia")
            ));


    assertEquals(greenExpected, greenTerritories);
    assertEquals(redExpected, redTerritories);
    assertEquals(blueExpected, blueTerritories);
    
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
  @Disabled
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
