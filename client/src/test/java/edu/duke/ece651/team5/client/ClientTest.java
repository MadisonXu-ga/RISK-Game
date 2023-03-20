package edu.duke.ece651.team5.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import edu.duke.ece651.team5.shared.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ClientTest {
  ServerSocket serverSocket;

  private void initServer(int type) throws IOException {
    serverSocket = new ServerSocket(12345);
    new Thread(() -> {
      while (true) {
        try {
          Socket socket = serverSocket.accept();
          new Thread(() -> {
            try {
              handleClient(socket, type);
            } catch (IOException e) {
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
    RISKMap map = new RISKMap();
    boolean yes = true;
    boolean no = false;
    String name = "Green";
    if (type == 0) {
      outputObj.writeObject(map);
    } else if (type == 1) {
      outputObj.writeObject(yes);
    } else if (type == 2) {
      outputObj.writeObject(no);
    } else if (type == 3) {
      outputObj.writeObject(name);
    }

  }

  private void closeServer() throws IOException {
    serverSocket.close();
  }

  @Disabled
  @Test
  void testInitClient() throws IOException, InterruptedException {
    Thread.sleep(1000);
    initServer(0);
    Client client = new Client("localhost", 12345);
    String res = client.initClient();
    assertEquals("Initiate client successfully", res);
    client.closeClientSocket();
    closeServer();
  }

  @Disabled
  @Test
  void testInitClientErr() throws IOException, InterruptedException {
    Thread.sleep(1000);
    Client client = new Client();
    String res = client.initClient();
    assertEquals("Cannot init Client", res);
  }

  @Test
  void testRecvMap() throws IOException, ClassNotFoundException {
    initServer(0);
    Client client = new Client();
    client.initClient();
    // boolean res = client.recvMapFromServer();
    RISKMap expected = new RISKMap();
    RISKMap res = client.recvMap();
    assertEquals(expected.getTerritories(), res.getTerritories());
    client.closeClientSocket();
    closeServer();
  }

  @Test
  void testRecvFirstPlayerSignal() throws IOException, ClassNotFoundException {
    initServer(1);
    Client client = new Client();
    client.initClient();
    boolean res = client.recvFirstPlayerSignal();
    assertEquals(true, res);
    client.closeClientSocket();
    closeServer();
  }

  @Test
  void testRecvPlayerName() throws IOException, ClassNotFoundException {
    initServer(3);
    Client client = new Client();
    client.initClient();
    String name = client.recvString();
    assertEquals("Green", name);
    client.closeClientSocket();
    closeServer();
  }


  @Test
  void testMain() throws IOException {
    initServer(0);
    Client.main(null);
    closeServer();
  }

  @Test
  void testMainError() {
    Client.main(null);

  }

  // @Test
  // void testCommunicate() {
  // int port = 12345;
  // try {
  // ServerSocket server = new ServerSocket (port) ;
  // Socket server_socket = server.accept();
  // ObjectOutputStream os = new
  // ObjectOutputStream(server_socket.getOutputStream());
  // ObjectInputStream is = new ObjectInputStream(new
  // BufferedInputStream(server_socket.getInputStream()));
  // String received = (String) is.readObject();
  // Client client = new Client();
  // assertEquals("hello from client\n", received);
  // os.writeObject("hello from server\n");
  // os.flush();
  // // generate a map
  // RISKMap myMap = generateMap();
  // os.writeObject(myMap);
  // os.flush();
  // os.writeObject(1) ;
  // os.flush();
  // } catch (Exception e) {
  // }
  // Client client = new Client();
  // assertEquals("localhost/127.0.0.1:12345",
  // client.getSocket().getRemoteSocketAddress().toString());

  // }

}
