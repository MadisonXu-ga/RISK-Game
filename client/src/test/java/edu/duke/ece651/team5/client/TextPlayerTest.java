package edu.duke.ece651.team5.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

import edu.duke.ece651.team5.shared.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TextPlayerTest {
  @Test
  void testDisplayMap() throws IOException, ClassNotFoundException{
    RISKMap map = createRISKMap();
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer p = createTextPlayer("1", bytes);
    p.displayMap(map);
    String expected = "Green player:\n"+
    "-------------\n" +
    "6 units: in Elantris (next to: Narnia, Scadrial, Midkemia, Roshar)\n" + 
    "10 units: in Narnia (next to: Elantris, Midkemia)\n" + 
    "8 units: in Oz (next to: Mordor, Scadrial, Midkemia, Gondor)\n" + 
    "\n" +
    "Red player:\n" + 
    "-------------\n" + 
    "12 units: in Midkemia (next to: Elantris, Narnia, Scadrial, Oz)\n" + 
    "3 units: in Roshar (next to: Elantris, Hogwarts, Scadrial)\n" + 
    "13 units: in Gondor (next to: Mordor, Oz)\n\n\n"
    ;
    assertEquals(expected, bytes.toString());
  }

  @Test
  void testPlayOneTurn() {
    RISKMap map = createRISKMap();
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer p = createTextPlayer("M\n3-Elantris-Narnia\nA\n3-Elantris-Narnia\nD\n", bytes);
    p.setPlayerName("Green");
    Action res = p.playOneTurn(map);
    Action expected = createAction();
    assertEquals(expected.getMoveOrders(), res.getMoveOrders());
  }

  @Test
  void testPlayOneTurnErr() {
    RISKMap map = createRISKMap();
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer p = createTextPlayer("gdsd\nM\n\n-1-A-Narnia\n3-A-Narnia\n3-Elantris-Narnia\nD\n", bytes);
    p.setPlayerName("Green");
    Action res = p.playOneTurn(map);
    Action expected = createAction();
    assertEquals(expected.getMoveOrders(), res.getMoveOrders());
  }


  private Action createAction(){
    ArrayList<Integer> attackOrder = new ArrayList<>();
    ArrayList<MoveOrder> moveOrder = new ArrayList<>();
    attackOrder.add(3);
    attackOrder.add(4);
    moveOrder.add(new MoveOrder("Elantris", "Narnia", 3, UnitType.SOLDIER));
    return new Action(attackOrder, moveOrder);
  }


  @Test
  void testSelectNumPlayer() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer p = createTextPlayer("3", bytes);
    int choice = p.selectNumPlayer();
    assertEquals(3, choice);
  }


  @Test
  void testSelectNumPlayerErr2() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer p = createTextPlayer("\n3", bytes);
    int choice = p.selectNumPlayer();
    assertEquals(3, choice);
  }

  @Test
  void testSelectNumPlayerErr() {
    RISKMap map = createRISKMap();
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer p = createTextPlayer("1\n5\n4\n", bytes);
    int choice = p.selectNumPlayer();
    String expected = "Seems like you are the first player in the game!\n" 
                          + "Please first enter how many players you want to play with(from 2 to 4 inclusive)\n\n"
                          + "Number input out of range. Please try again.\n" 
                          + "Please first enter how many players you want to play with(from 2 to 4 inclusive)\n\n" 
                          + "Number input out of range. Please try again.\n" 
                          + "Please first enter how many players you want to play with(from 2 to 4 inclusive)\n\n";
    assertEquals(expected, bytes.toString());
    assertEquals(4, choice);
  }

  @Test
  void testSetPlayer() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer p = createTextPlayer("1", bytes);
    p.setPlayerName("Green");
    assertEquals("Green", p.getPlayerName());
  }



  @Test
  void testUnitPlacement() {
    RISKMap map = createRISKMap();
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer p = createTextPlayer("10\n40\n15\n", bytes);
   
    p.setPlayerName("Green");
    Player player = map.getPlayerByName("Green");
    assertEquals(p.getPlayerName(), player.getName());
    HashMap<String, Integer> placementInfo = p.unitPlacement(map);
    HashMap<String, Integer> expected = new HashMap<>();
    expected.put("Elantris", 10);
    expected.put("Narnia", 40);
    expected.put("Oz", 0);
    assertEquals(expected, placementInfo);

  }

  @Test
  void testInvalidUnitPlacement() {
    RISKMap map = createRISKMap();
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer p = createTextPlayer("10\n60\n15\n25\n", bytes);
    p.setPlayerName("Green");
    Player player = map.getPlayerByName("Green");
    assertEquals(p.getPlayerName(), player.getName());
    HashMap<String, Integer> placementInfo = p.unitPlacement(map);
    HashMap<String, Integer> expected = new HashMap<>();
    expected.put("Elantris", 10);
    expected.put("Narnia", 15);
    expected.put("Oz", 25);

    assertEquals(expected, placementInfo);

  }

  @Test
  void testPrintPlacementResultTrue() {
    boolean test = true;
    RISKMap map = createRISKMap();
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer p = createTextPlayer("10\n60\n15\n25\n", bytes);
   
    p.setPlayerName("Green");
    p.printPlacementResult(test);
    assertEquals("This is your player name for this game: Green\nGreat! All your placement choices get approved!\n",  bytes.toString());

  }


  @Test
  void testPrintPlacementResultFalse() {
    boolean test = true;
    RISKMap map = createRISKMap();
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer p = createTextPlayer("10\n60\n15\n25\n", bytes);
    p.setPlayerName("Green");
    p.printPlacementResult(!test);
    assertEquals("This is your player name for this game: Green\nSorry your unit placement is not successful, please give another try.\n",  bytes.toString());
  }

  @Test
  void testCommitPlacementResultTrue() {
    boolean test = true;
    RISKMap map = createRISKMap();
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer p = createTextPlayer("10\n60\n15\n25\n", bytes);
   
    p.setPlayerName("Green");
    p.printCommitResult(test);
    assertEquals("This is your player name for this game: Green\nYou successfully commit all your orders!\n",  bytes.toString());

  }


  @Test
  void testCommitPlacementResultFalse() {
    boolean test = true;
    RISKMap map = createRISKMap();
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer p = createTextPlayer("10\n60\n15\n25\n", bytes);
    p.setPlayerName("Green");
    p.printCommitResult(!test);
    assertEquals("This is your player name for this game: Green\nSorry your commit is not successful, please give another try.\n",  bytes.toString());
  }

  @Test
  void testCheckWinner(){
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer p = createTextPlayer("10\n60\n15\n25\n", bytes);
    p.setPlayerName("green");
    String res = p.checkWinner(createWinResult());
    assertEquals("green", res);

    String res2 = p.checkWinner(createNothingResult());
    assertEquals("", res2);
  }

  @Test
  void testCheckIfILose(){
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer p = createTextPlayer("xe\n3\n0\n2\n", bytes);
    p.setPlayerName("green");
    String res = p.checkIfILose(createLostResult());
    assertEquals("Close", res);
  }

  @Test
  void testCheckIfILose2(){
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer p = createTextPlayer("xe\n3\n0\n1\n", bytes);
    p.setPlayerName("green");
    String res = p.checkIfILose(createLostResult());
    assertEquals("Display", res);
  }

  @Test
  void testCheckIfILoseErr(){
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer p = createTextPlayer("xe\n3\n0\n1\n", bytes);
    p.setPlayerName("green");
    String res = p.checkIfILose(createNothingResult());
    assertEquals("", res);
  }

  private HashMap<String, Boolean> createWinResult(){
    HashMap<String, Boolean> res = new HashMap<>();
    res.put("green", true);
    res.put("red", null);
    return res;
  }

  private HashMap<String, Boolean> createLostResult(){
    HashMap<String, Boolean> res = new HashMap<>();
    res.put("green", false);
    res.put("red", null);
    return res;
  }

  private HashMap<String, Boolean> createNothingResult(){
    HashMap<String, Boolean> res = new HashMap<>();
    res.put("green", null);
    res.put("red", null);
    return res;
  }

  private RISKMap createRISKMap(){
    RISKMap map = new RISKMap();
    ArrayList<Player> players = new ArrayList<>();
    Player p = new Player("Green");
    p.addTerritory(map.getTerritoryByName("Elantris"));
    p.addTerritory(map.getTerritoryByName("Narnia"));
    p.addTerritory(map.getTerritoryByName("Oz"));
    Player p2 = new Player("Red");
    p2.addTerritory(map.getTerritoryByName("Midkemia"));
    p2.addTerritory(map.getTerritoryByName("Roshar"));
    p2.addTerritory(map.getTerritoryByName("Gondor"));
    players.add(p);
    players.add(p2);
    map.initPlayers(players);
    return map;
  }

  private TextPlayer createTextPlayer(String inputData, OutputStream bytes){
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    return new TextPlayer(input, output);
  }
}
