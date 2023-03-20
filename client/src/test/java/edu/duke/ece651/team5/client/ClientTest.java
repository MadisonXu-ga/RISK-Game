package edu.duke.ece651.team5.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

import edu.duke.ece651.team5.shared.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ClientTest {
  ServerSocket serverSocket;

  private void initServer(int type) throws IOException, ClassNotFoundException{
    serverSocket = new ServerSocket(57809);
    new Thread(() -> {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                      handleClient(socket, type);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }).start();
  }

  private void handleClient(Socket socket, int type) throws IOException {
      ObjectOutputStream outputObj = new ObjectOutputStream(socket.getOutputStream());
      RISKMap map = createRISKMap();
      boolean yes = true;
      boolean no = false;
      HashMap<String, Boolean> res = new HashMap<>();
      res.put("Green", false);
      res.put("Red", false);
      HashMap<String, Boolean> res2 = new HashMap<>();
      res2.put("Green", true);
      res2.put("Red", null);
      String name = "Green";
      if(type == 0){
        outputObj.reset();
        outputObj.writeObject(map);
      }else if(type == 1){
        outputObj.reset();
        outputObj.writeObject(res);
      }else if(type == 2){
        outputObj.reset();
        outputObj.writeObject(map);
        outputObj.reset();
        outputObj.writeObject(no);
        outputObj.reset();
        outputObj.writeObject(map);
        outputObj.reset();
        outputObj.writeObject(yes);
      }else if(type == 3){
        outputObj.reset();
        outputObj.writeObject(name);
      }else if(type == 4){
        outputObj.reset();
        outputObj.writeObject("First");
        outputObj.reset();
        outputObj.writeObject("Red");
      }else if(type == 5){
        outputObj.reset();
        outputObj.writeObject(map);
        outputObj.reset();
        outputObj.writeObject(no);
        outputObj.reset();
        outputObj.writeObject(map);
        outputObj.reset();
        outputObj.writeObject(yes);
      }else if(type == 6){
        outputObj.reset();
        outputObj.writeObject(res2);
      }
  }

  private void recvMessage(Socket socket) throws IOException, ClassNotFoundException{
    ObjectInputStream inputObj = new ObjectInputStream(socket.getInputStream());
    inputObj.readObject();
  }

  private void closeServer() throws IOException {
      serverSocket.close();
  }

  @Test
  void testInitClient() throws IOException, ClassNotFoundException, InterruptedException{
    Thread.sleep(100);
    initServer(0);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Client client = createNewClient("1", bytes);
    client.initClient();
    assertEquals("\nInitiate client successfully\n", bytes.toString());
    client.closeClientSocket();
    closeServer();
  }

  @Test
  void testInitClientErr() throws IOException, InterruptedException{
    Thread.sleep(1000);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Client client = createNewClient("1", bytes);
    client.initClient();
    assertEquals("Cannot init Client\n", bytes.toString());
  }

  @Test
  void testRecvMap() throws IOException, ClassNotFoundException, InterruptedException{
    Thread.sleep(100);
    initServer(0);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Client client = createNewClient("1", bytes);
    client.initClient();
    // boolean res = client.recvMapFromServer();
    RISKMap expected = new RISKMap();
    RISKMap res = client.recvMap();
    assertEquals(expected.getTerritories(), res.getTerritories());
    client.closeClientSocket();
    closeServer();
  }


  @Test
  void testHandlePlayerNameFirst() throws IOException, ClassNotFoundException, InterruptedException{
    Thread.sleep(100);
    initServer(4);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Client client = createNewClient("4", bytes);
    client.initClient();
    client.handlePlayerName();
    String expected = "\nInitiate client successfully\n" + 
                      "\nWaiting to receive your player name...\n" + 
                      "\nSeems like you are the first player in the game!\n" + 
                      "Please first enter how many players you want to play with(from 2 to 4 inclusive)\n\n" +
                      "\nSending your choice..\n\n" + 
                      "\nWe got your player name!\n" + 
                      "\nThis is your player name for this game: Red\n";
    assertEquals(expected, bytes.toString());
    client.closeClientSocket();
    closeServer();
  }

  @Test
  void testHandlePlayerName() throws IOException, ClassNotFoundException, InterruptedException{
    Thread.sleep(100);
    initServer(3);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Client client = createNewClient("3", bytes);
    client.initClient();
    client.handlePlayerName();
    String expected = "\nInitiate client successfully\n" + 
                      "\nWaiting to receive your player name...\n\n" + 
                      "\nWe got your player name!\n" + 
                      "\nThis is your player name for this game: Green\n";
    assertEquals(expected, bytes.toString());
    client.closeClientSocket();
    closeServer();
  }

  @Test
  void testHandlePlacement() throws IOException, ClassNotFoundException, InterruptedException{
    Thread.sleep(100);
    initServer(2);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Client client = createNewClient("10\n40\n15\n10\n40\n15\n", bytes);
    client.initClient();
    client.setPlayer();
    client.handlePlacement();
    String expected = 
    "\nInitiate client successfully\n" +
    "This is your player name for this game: Green\n" + 
    "\nNow you need to decide where to put your territories...\n" + 
    "Think Carefully!\n\n" + 
    "\nReceiving RISK map...\n" + 
    "\nReceived Map\n" + 
    "\nHow many unit you want to place in your Elantris\n" + 
    "How many unit you want to place in your Narnia\n" + 
    "\nWe got all your choices, sending your choices...\n" + 
    "\nSorry your unit placement is not successful, please give another try.\n" + 
    "\nReceiving RISK map...\n" + 
    "\nReceived Map\n" + 
    "\nHow many unit you want to place in your Elantris\n" + 
    "How many unit you want to place in your Narnia\n" + 
    "How many unit you want to place in your Oz\n" + 
    "Number input out of range. Please try again.\n" + 
    "How many unit you want to place in your Oz\n" + 
    "\nWe got all your choices, sending your choices...\n" + 
    "\nGreat! All your placement choices get approved!\n";
    assertEquals(expected, bytes.toString());
    client.closeClientSocket();
    closeServer();
  }

  // @Test
  // void testPlayOneTurn() throws IOException, ClassNotFoundException{
  //   initServer(5);
  //   ByteArrayOutputStream bytes = new ByteArrayOutputStream();
  //   Client client = createNewClient("M\n3-Elantris-Narnia\nD\nM\n3-Elantris-Narnia\nD\n", bytes);
  //   client.initClient();
  //   client.setPlayer();
  //   client.playOneTurn();
  //   String expected = "";
  //   assertEquals(expected, bytes.toString());
  //   client.closeClientSocket();
  //   closeServer();
  // }

  // @Test
  // void testDisplay()throws IOException, ClassNotFoundException{
  //   initServer(0);
  //   ByteArrayOutputStream bytes = new ByteArrayOutputStream();
  //   Client client = createNewClient("M", bytes);
  //   client.initClient();
  //   client.setPlayer();
  //   RISKMap map = client.recvMap();
  //   MapTextView view = new MapTextView(map);
  //   assertEquals("test", view.displayMap());
  //   client.closeClientSocket();
  //   closeServer();
  // }

  @Test
  void testCheckResult() throws IOException, ClassNotFoundException, InterruptedException{
    Thread.sleep(100);
    initServer(1);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Client client = createNewClient("1", bytes);
    client.initClient();
    client.setPlayer();
    client.checkResult();
    String expected = 
    "\nInitiate client successfully\n" +
    "This is your player name for this game: Green\n" +
    "\nSeems like everyone finishes thier turn.\n" +
    "Now let's check the result of this round...\n" +
    "\nSorry Player Green, you lose for this Game.\n" + 
    "Now you have two options:\n" + 
    "1. Continue to watch the game\n" + 
    "2. Quit the game\n" + 
    "Please enter 1 or 2\n\n";
    assertEquals(expected, bytes.toString());
    client.closeClientSocket();
    closeServer();
  }

  @Test
  void testCheckResult2() throws IOException, ClassNotFoundException, InterruptedException{
    Thread.sleep(100);
    initServer(6);
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Client client = createNewClient("1", bytes);
    client.initClient();
    client.setPlayer();
    client.checkResult();
    String expected = 
    "\nInitiate client successfully\n" +
    "This is your player name for this game: Green\n" +
    "\nSeems like everyone finishes thier turn.\n" +
    "Now let's check the result of this round...\n" +
    "\nPlayer Green wins!\n" +
    "Game End NOW\n";
    assertEquals(expected, bytes.toString());
    client.closeClientSocket();
    closeServer();
  }



  @Test
  void testMain() throws IOException, ClassNotFoundException, InterruptedException{
    Thread.sleep(100);
    initServer(0);
    Client.main(null);
    closeServer();
  }

  @Test
  void testMainError(){
    Client.main(null);

  }

  private Client createNewClient(String inputData, OutputStream bytes){
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    return new Client(input, output);
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




  // @Test
  // void testCommunicate() {
  //   int port = 12345;
  //   try {
  //     ServerSocket server = new ServerSocket (port) ;
  //     Socket server_socket = server.accept();
  //     ObjectOutputStream os = new ObjectOutputStream(server_socket.getOutputStream());
  //     ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(server_socket.getInputStream()));
  //     String received = (String) is.readObject();
  //     Client client = new Client();
  //     assertEquals("hello from client\n", received);
  //     os.writeObject("hello from server\n");
  //     os.flush();
  //     // generate a map
  //     RISKMap myMap = generateMap();
  //     os.writeObject(myMap);
  //     os.flush();
  //     os.writeObject(1) ;
  //     os.flush();
  //     } catch (Exception e) {
  //   }
  //   Client client = new Client();
  //   assertEquals("localhost/127.0.0.1:12345", client.getSocket().getRemoteSocketAddress().toString());

  // }

}
