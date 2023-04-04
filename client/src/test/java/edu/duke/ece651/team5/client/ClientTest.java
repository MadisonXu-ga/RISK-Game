package edu.duke.ece651.team5.client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

import edu.duke.ece651.team5.shared.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;

public class ClientTest {
  public static Client createPlayer(PlayerConnection test,BufferedReader input,PrintStream output) throws UnknownHostException, IOException{
    Client client = new Client(input, output){
      @Override
      public void createPlayer(){
          try{
            this.playerConnection = test;
            this.textPlayer = new TextPlayer(input, output);
          }
          catch(Exception e) {
          }
      }
    };
    client.createPlayer();
    return client;
  }
  



  @Test
  void testRecvMap() throws IOException, ClassNotFoundException{
    // RISKMap map = mock(RISKMap.class);
    Game game = mock(Game.class);
    PlayerConnection test = mock(PlayerConnection.class);
    when(test.readData()).thenReturn(game);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedReader input = new BufferedReader(new StringReader("1"));
    PrintStream output = new PrintStream(bytes, true);
    Client client = createPlayer(test, input, output);
    
    assertEquals(game, client.recvGame());
  }


  @Test
  void testHandlePlayerNameFirst() throws IOException, ClassNotFoundException{
    PlayerConnection test = mock(PlayerConnection.class);
    when(test.readData()).thenReturn("First", "Red");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedReader input = new BufferedReader(new StringReader("4"));
    PrintStream output = new PrintStream(bytes, true);
    Client client = createPlayer(test, input, output);

    client.handlePlayerName();
    String expected = "\nWaiting to receive your player name...\n" + 
                      "\nSeems like you are the first player in the game!\n" + 
                      "Please first enter how many players you want to play with(from 2 to 4 inclusive)\n\n" +
                      "\nSending your choice..\n\n" + 
                      "\nWe got your player name!\n" + 
                      "\nThis is your player name for this game: Red\n";
    assertEquals(expected, bytes.toString());
  }

  @Test
  void testHandlePlayerName() throws IOException, ClassNotFoundException, InterruptedException{
    PlayerConnection test = mock(PlayerConnection.class);
    when(test.readData()).thenReturn("Green");

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedReader input = new BufferedReader(new StringReader("4"));
    PrintStream output = new PrintStream(bytes, true);

    Client client = createPlayer(test, input, output);
    client.handlePlayerName();
    String expected = "\nWaiting to receive your player name...\n\n" + 
                      "\nWe got your player name!\n" + 
                      "\nThis is your player name for this game: Green\n";
    assertEquals(expected, bytes.toString());
  }

  @Test
  void testHandlePlacement() throws IOException, ClassNotFoundException, InterruptedException{
    RISKMap map = createRISKMap();
    PlayerConnection test = mock(PlayerConnection.class);
    when(test.readData()).thenReturn(map, false, true);

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedReader input = new BufferedReader(new StringReader("10\n40\n15\n10\n40\n15\n"));
    PrintStream output = new PrintStream(bytes, true);

    Client client = createPlayer(test, input, output);
    Player green = new Player("Green");
    client.textPlayer.setPlayer(green);
    client.handlePlacement();
    String expected = 
    "This is your player name for this game: Green\n" + 
    "\nReceiving RISK map...\n" + 
    "\nReceived Map\n\n" + 
    "\nNow you need to decide where to put your territories...\n" + 
    "Think Carefully!\n" + 
    "\nHow many unit you want to place in your Elantris\n" + 
    "How many unit you want to place in your Narnia\n" + 
    "Number input out of range. Please try again.\n" + 
    "How many unit you want to place in your Narnia\n" + 
    "\nWe got all your choices, sending your choices...\n" + 
    "\nSorry your unit placement is not successful, please give another try." + 
    "\nHow many unit you want to place in your Elantris\n" + 
    "How many unit you want to place in your Narnia\n" + 
    "Number input out of range. Please try again.\n" + 
    "How many unit you want to place in your Narnia\n" + 
    "\nWe got all your choices, sending your choices...\n" + 
    "\nGreat! All your placement choices get approved!\n";
    assertEquals(expected, bytes.toString());
  }

  @Test
  void testPlayOneTurnIfLose() throws IOException, ClassNotFoundException{
    RISKMap map = createRISKMap();
    PlayerConnection test = mock(PlayerConnection.class);
    when(test.readData()).thenReturn(map, false, true);

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedReader input = new BufferedReader(new StringReader("M\n3-Elantris-Narnia\nD\nM\n3-Elantris-Narnia\nD\n"));
    PrintStream output = new PrintStream(bytes, true);

    Client client = createPlayer(test, input, output);
    Player green = new Player("Green");
    client.textPlayer.setPlayer(green);
    client.isLose = true;
    client.playOneTurn();
    String expected = 
    "This is your player name for this game: Green\n\n" +
    "------Spectator Mode------\n\n" + 
    "\nReceiving RISK map...\n" + 
    "\nReceived Map\n" + 
    "\nGreen player:\n" + 
    "-------------\n" + 
    "0 units: in Elantris (next to: Narnia, Scadrial, Midkemia, Roshar)\n" +
    "0 units: in Narnia (next to: Elantris, Midkemia)\n" +
    "0 units: in Oz (next to: Mordor, Scadrial, Midkemia, Gondor)\n" +
      
    "\nRed player:\n" + 
    "-------------\n" + 
    "0 units: in Midkemia (next to: Elantris, Narnia, Scadrial, Oz)\n" +
    "0 units: in Roshar (next to: Elantris, Hogwarts, Scadrial)\n" +
    "0 units: in Gondor (next to: Mordor, Oz)\n\n\n";
    assertEquals(expected, bytes.toString());
  }

  @Test
  void testPlayOneTurn() throws IOException, ClassNotFoundException{
    RISKMap map = createRISKMap();
    PlayerConnection test = mock(PlayerConnection.class);
    when(test.readData()).thenReturn(map, false, "Incorrect", true, "Correct");

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedReader input = new BufferedReader(new StringReader("M\n3-Elantris-Narnia\nD\nM\n3-Elantris-Narnia\nD\n"));
    PrintStream output = new PrintStream(bytes, true);

    Client client = createPlayer(test, input, output);
    Player green = new Player("Green");
    client.textPlayer.setPlayer(green);
    client.playOneTurn();
    String expected =
    "This is your player name for this game: Green\n" +
    "\nNow it's time to play the game!\n\n" + 


    "\nReceiving RISK map...\n" + 
      
    "\nReceived Map\n" + 
      
    "\nGreen player:\n" + 
    "-------------\n" + 
    "0 units: in Elantris (next to: Narnia, Scadrial, Midkemia, Roshar)\n" +
    "0 units: in Narnia (next to: Elantris, Midkemia)\n" +
    "0 units: in Oz (next to: Mordor, Scadrial, Midkemia, Gondor)\n" +
      
    "\nRed player:\n" + 
    "-------------\n" + 
    "0 units: in Midkemia (next to: Elantris, Narnia, Scadrial, Oz)\n" +
    "0 units: in Roshar (next to: Elantris, Hogwarts, Scadrial)\n" +
    "0 units: in Gondor (next to: Mordor, Oz)\n\n" +
      
      
    "\nWhat would you like to do?\n" + 
    "(M)ove\n" + 
    "(A)ttack\n" + 
    "(D)one\n\n" + 
      
    "Please enter your unit number of units to M, the source territory, and the destination territory.\n" + 
    "Please separate them by dash(-). For example: 3-TerritoryA-TerritoryB\n\n" + 
      
    "What would you like to do?\n" + 
    "(M)ove\n" + 
    "(A)ttack\n" + 
    "(D)one\n\n" + 
    
      
    "\nWe got all your orders, sending your orders...\n" + 
      
    "\nSorry your commit is not successful because: Incorrect" + 
      
      
    "\nGreen player:\n" + 
    "-------------\n" + 
    "0 units: in Elantris (next to: Narnia, Scadrial, Midkemia, Roshar)\n" +
    "0 units: in Narnia (next to: Elantris, Midkemia)\n" +
    "0 units: in Oz (next to: Mordor, Scadrial, Midkemia, Gondor)\n" +
      
    "\nRed player:\n" + 
    "-------------\n" + 
    "0 units: in Midkemia (next to: Elantris, Narnia, Scadrial, Oz)\n" +
    "0 units: in Roshar (next to: Elantris, Hogwarts, Scadrial)\n" +
    "0 units: in Gondor (next to: Mordor, Oz)\n\n" +
      
      
    "\nWhat would you like to do?\n" + 
    "(M)ove\n" + 
    "(A)ttack\n" + 
    "(D)one\n\n" + 
      
    "Please enter your unit number of units to M, the source territory, and the destination territory.\n" + 
    "Please separate them by dash(-). For example: 3-TerritoryA-TerritoryB\n" + 
      
    "\nWhat would you like to do?\n" + 
    "(M)ove\n" + 
    "(A)ttack\n" + 
    "(D)one\n\n" + 
      

    "\nWe got all your orders, sending your orders...\n" + 
      
    "\nYou successfully commit all your orders!\n";
    
    assertEquals(expected, bytes.toString());
  }

  @Test
  void testCheckResult() throws IOException, ClassNotFoundException, InterruptedException{
    HashMap<String, Boolean> res = new HashMap<>();
    res.put("Green", false);
    res.put("Red", false);
    ArrayList<AttackOrder> attRes = createAttRes(0);
    PlayerConnection test = mock(PlayerConnection.class);
    when(test.readData()).thenReturn(attRes, res);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedReader input = new BufferedReader(new StringReader("1\nDisconnect"));
    PrintStream output = new PrintStream(bytes, true);

    Client client = createPlayer(test, input, output);
    Player green = new Player("Green");
    client.textPlayer.setPlayer(green);
    String message = client.checkResult();
    String expected = 
    "This is your player name for this game: Green\n" +
    "Your attack order to attack Territory B was lose in last round.\n\n" + 
    "Now let's check the result of this round...\n" +
    "\nSorry Player Green, you lose for this Game.\n" + 
    "Now you have two options:\n" + 
    "1. Continue to watch the game\n" + 
    "2. Quit the game\n" + 
    "Please enter 1 or 2\n\n";
    assertEquals(expected, bytes.toString());
    assertEquals("", message);

  }

  @Test
  void testCheckResult2() throws IOException, ClassNotFoundException{
    HashMap<String, Boolean> res = new HashMap<>();
    res.put("Green", null);
    res.put("Red", null);
    ArrayList<AttackOrder> attRes = createAttRes(1);
		PlayerConnection test = mock(PlayerConnection.class);
    when(test.readData()).thenReturn(attRes, res);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedReader input = new BufferedReader(new StringReader("1"));
    PrintStream output = new PrintStream(bytes, true);

    Client client = createPlayer(test, input, output);
    Player green = new Player("Green");
    client.textPlayer.setPlayer(green);
    String message = client.checkResult();
    String expected = 
    "This is your player name for this game: Green\n" +
    "Congratulations! You successfully own the Territory B\n\n" + 
    "Now let's check the result of this round...\n" +
    "\nNo winner for this round, let's start a new one!\n";
    assertEquals(expected, bytes.toString());
    assertEquals("", message);
  }

  @Test
  void testCheckResultIfLose() throws IOException, ClassNotFoundException, InterruptedException{
    HashMap<String, Boolean> res = new HashMap<>();
    res.put("Orange", true);
    res.put("Red", null);

    PlayerConnection test = mock(PlayerConnection.class);
    when(test.readData()).thenReturn(res);

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedReader input = new BufferedReader(new StringReader("M\n3-Elantris-Narnia\nD\nM\n3-Elantris-Narnia\nD\n"));
    PrintStream output = new PrintStream(bytes, true);

    Client client = createPlayer(test, input, output);
    Player green = new Player("Green");
    client.textPlayer.setPlayer(green);
    client.isLose = true;
    client.checkResult();
    String expected = 
    "This is your player name for this game: Green\n\n" +
    "Now let's check the result of this round...\n" +
    "\nPlayer Orange wins!\n" +
    "Game End NOW\n";
    assertEquals(expected, bytes.toString());

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

  private ArrayList<AttackOrder> createAttRes(int type){
    ArrayList<AttackOrder> attRes = new ArrayList<>();
    if(type == 0){
      attRes.add(new AttackOrder("A", "B", 0, UnitType.SOLDIER, "Green"));
    }else{
      attRes.add(new AttackOrder("A", "B", 3, UnitType.SOLDIER, "Green"));
    }
    return attRes;
    }
  }
