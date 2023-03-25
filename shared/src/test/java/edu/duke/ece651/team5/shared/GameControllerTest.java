package edu.duke.ece651.team5.shared;

import edu.duke.ece651.team5.shared.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class GameControllerTest {
    @Test
    public void testResolveWinnerForThisRound(){
        Player blue =  new Player("Blue");
        Player green = new Player("Green");
        AttackOrder winOrder = new AttackOrder("Narnia", "Elantris", 4, UnitType.SOLDIER, "Green");
        Territory fightTerri = new Territory("Elantris");
        fightTerri.setOwner(blue);
        blue.addTerritory(fightTerri);


        GameController gc = new GameController();
        gc.riskMap.getTerritoryByName("Narnia").setOwner(green);
        gc.riskMap.getTerritoryByName("Elantris").setOwner(blue);
        gc.resolveWinnerForThisRound(winOrder, fightTerri);
        assertEquals("Blue", gc.riskMap.getTerritoryByName("Elantris").getOwner().getName());
    }

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
                        new Territory("Narnia"), new Territory("Scadrial"),
                        new Territory("Gondor"), new Territory("Thalassia"),
                        new Territory("Sylvaria"), new Territory("Celestia"),
                        new Territory("Ironcliff"), new Territory("Draconia")
    ));

    ArrayList<Territory> blueExpected = new ArrayList<>(
            Arrays.asList(
                    new Territory("Elantris"), new Territory("Oz"),
                    new Territory("Mordor"), new Territory("Arathia"),
                    new Territory("Kaelindor"), new Territory("Frosthold"),
                    new Territory("Stormhaven"), new Territory("Emberfall")
            ));

    ArrayList<Territory> redExpected = new ArrayList<>(
            Arrays.asList(
                    new Territory("Midkemia"), new Territory("Roshar"),
                    new Territory("Hogwarts"), new Territory("Eryndor"),
                    new Territory("Eterna"), new Territory("Shadowmire"),
                    new Territory("Mythosia"), new Territory("Verdantia")
            ));


    assertEquals(greenExpected, greenTerritories);
    assertEquals(redExpected, redTerritories);
    assertEquals(blueExpected, blueTerritories);
    
  }

  @Test
  void testGetPlayerName() {
        GameController gc = new GameController();
        assertEquals("Green", gc.getPlayerName(0));
  }

    @Test
    void testGetPlayerNumByName(){
        GameController gc = new GameController();
        assertEquals(-1, gc.getPlayerNumByName("abc"));
        assertEquals(0, gc.getPlayerNumByName("Green"));
    }


  @Test
  void testGetRiskMap() {
        GameController gc = new GameController();
        gc.assignTerritories(3);
        RISKMap map = gc.getRiskMap();
        assertEquals(8, map.getPlayers().get(0).getTerritories().size());
        assertEquals(24, map.getTerritories().size());
  }

  @Test
  void testResolveUnitPlacement() {
        HashMap<String, Integer> placeInfo = new HashMap<>();
        placeInfo.put("Narnia", 10);
        placeInfo.put("Elantris", 40);
        GameController gc = new GameController();
        gc.resolveUnitPlacement(placeInfo);
        assertEquals(10, gc.getRiskMap().getTerritoryByName("Narnia").getUnitNum(UnitType.SOLDIER));
        assertEquals(40, gc.getRiskMap().getTerritoryByName("Elantris").getUnitNum(UnitType.SOLDIER));

  }

  @Test
  void testExecuteAttackOrder(){
        GameController gc = new GameController();
        gc.assignTerritories(3);
        gc.getRiskMap().getTerritoryByName("Narnia").updateUnitCount(UnitType.SOLDIER, false, 10);
        ArrayList<AttackOrder> attackOrders = new ArrayList<>();
        attackOrders.add(new AttackOrder("Narnia", "Elantris", 3, UnitType.SOLDIER, "Green"));
        gc.executeAttackOrder(attackOrders);
        assertEquals(7, gc.getRiskMap().getTerritoryByName("Narnia").getUnitNum(UnitType.SOLDIER));

  }

  @Test
  void testMergeSamePlayers(){
        HashMap<String, ArrayList<AttackOrder>> orders = createOrders();
        GameController gc = new GameController();
        HashMap<String, ArrayList<AttackOrder>> mergeOrders =  gc.mergeSamePlayers(orders);
        assertEquals(2, mergeOrders.size());
        assertEquals(5, mergeOrders.get("Elantris").get(0).getNumber());

  }

//   @Test
//   void testResolveAllMoveOrders(){
//         ArrayList<MoveOrder> p0 = new ArrayList<>();
//         p0.add(new MoveOrder("Narnia", "Midkemia", 2, UnitType.SOLDIER, "Green"));
//         ArrayList<MoveOrder> p2 = new ArrayList<>();
//         p2.add( new MoveOrder("Scadrial", "Roshar", 3, UnitType.SOLDIER, "Blue"));
       
//         PlayHandler mockp0 = mock(PlayHandler.class);
//         PlayHandler mockp1 = mock(PlayHandler.class);
//         PlayHandler mockp2 = mock(PlayHandler.class);
//         PlayHandler mockp3 = mock(PlayHandler.class);
//         when(mockp0.getPlayerMoveOrders()).thenReturn(p0);
//         when(mockp2.getPlayerMoveOrders()).thenReturn(p2);
//         ArrayList<PlayHandler> test = new ArrayList<>();
//         test.add(mockp0);
//         test.add(mockp1);
//         test.add(mockp2);
//         test.add(mockp3);

//         GameController gc = new GameController();
//         gc.assignTerritories(3);
//         gc.getRiskMap().getTerritoryByName("Narnia").updateUnitCount(UnitType.SOLDIER, false, 10);
//         gc.getRiskMap().getTerritoryByName("Scadrial").updateUnitCount(UnitType.SOLDIER, false, 10);
//         gc.resolveAllMoveOrders(4, createPlayerConnectionStatus(), test);
//         assertEquals(8, gc.getRiskMap().getTerritoryByName("Narnia").getUnitNum(UnitType.SOLDIER));
//         assertEquals(7, gc.getRiskMap().getTerritoryByName("Scadrial").getUnitNum(UnitType.SOLDIER));
//   }


//   @Test
//   void testResolveAllAttackOrders(){
//         ArrayList<AttackOrder> p0 = new ArrayList<>();
//         p0.add(new AttackOrder("Narnia", "Midkemia", 2, UnitType.SOLDIER, "Green"));
//         ArrayList<AttackOrder> p2 = new ArrayList<>();
//         p2.add(new AttackOrder("Scadrial", "Roshar", 3, UnitType.SOLDIER, "Blue"));
       
//         PlayHandler mockp0 = mock(PlayHandler.class);
//         PlayHandler mockp1 = mock(PlayHandler.class);
//         PlayHandler mockp2 = mock(PlayHandler.class);
//         PlayHandler mockp3 = mock(PlayHandler.class);
//         when(mockp0.getPlayerAttackOrders()).thenReturn(p0);
//         when(mockp2.getPlayerAttackOrders()).thenReturn(p2);
//         ArrayList<PlayHandler> test = new ArrayList<>();
//         test.add(mockp0);
//         test.add(mockp1);
//         test.add(mockp2);
//         test.add(mockp3);

//         GameController gc = new GameController();
//         gc.assignTerritories(3);
//         gc.getRiskMap().getTerritoryByName("Narnia").updateUnitCount(UnitType.SOLDIER, false, 10);
//         gc.getRiskMap().getTerritoryByName("Scadrial").updateUnitCount(UnitType.SOLDIER, false, 10);
//         gc.resolveAllAttackOrder(4, createPlayerConnectionStatus(), test);
//         assertEquals(8, gc.getRiskMap().getTerritoryByName("Narnia").getUnitNum(UnitType.SOLDIER));
//         assertEquals(7, gc.getRiskMap().getTerritoryByName("Scadrial").getUnitNum(UnitType.SOLDIER));
//   }


//   @Test
//   void testGroupAttackOrdersByPlayers(){
//         HashMap<String, ArrayList<AttackOrder>> orders = createOrders();
//         GameController gc = new GameController();
//         HashMap<String, ArrayList<AttackOrder>> mergeOrders =  gc.groupAttackOrdersByPlayers(orders);
//         assertEquals(2, mergeOrders.size());
//         assertEquals(5, mergeOrders.get("Elantris").get(0).getNumber());

//   }


  private HashMap<String, ArrayList<AttackOrder>> createOrders(){
        HashMap<String, ArrayList<AttackOrder>> orders = new HashMap<>();
        ArrayList<AttackOrder> attGreen = new ArrayList<>();
        attGreen.add(new AttackOrder("Narnia", "Elantris", 2, UnitType.SOLDIER, "Green"));
        attGreen.add(new AttackOrder("Midkemia", "Elantris", 3, UnitType.SOLDIER, "Green"));
        
        ArrayList<AttackOrder> attBlue = new ArrayList<>();
        attBlue.add(new AttackOrder( "Elantris", "Narnia", 2, UnitType.SOLDIER, "Blue"));
       
        orders.put("Elantris", attGreen);
        orders.put("Narnia", attBlue);

        return orders;
}

  @Test
  void testAddOneUnitToTerritories() {
      GameController gameController = new GameController();
      RISKMap riskMap = gameController.getRiskMap();
      Territory oz = riskMap.getTerritoryByName("Oz");
      gameController.addOneUnitToTerrirories();
      assertEquals(1, oz.getUnitNum(UnitType.SOLDIER));
  }

    @Test
    void testGroupAttackOrdersByPlayers(){
        HashMap<String, ArrayList<AttackOrder>> orders = createOrders();
        GameController gc = new GameController();
        HashMap<Integer, ArrayList<AttackOrder>> mergeOrders =  gc.groupAttackOrdersByPlayers(orders);
        assertEquals(2, mergeOrders.size());

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
  
  @Test
    void testCheckWin() {
      GameController gameController = new GameController();
      ArrayList<Boolean> list = new ArrayList<>();
      list.add(true);
      list.add(true);
      list.add(false);
      boolean b = gameController.checkWin(list);
      assertFalse(b);
  }

    @Test
    void rollDice() {
      GameController gameController = new GameController();
      boolean b = gameController.rollDice();
      assertTrue(b);
    }
}
