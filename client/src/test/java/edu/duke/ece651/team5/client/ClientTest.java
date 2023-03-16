package edu.duke.ece651.team5.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import edu.duke.ece651.team5.shared.*;

import org.junit.jupiter.api.Test;

public class ClientTest {
  ServerSocket serverSocket;

  private void initServer() throws IOException {
    serverSocket = new ServerSocket(12345);
    new Thread(() -> {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        handleClient(socket);
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

  private void handleClient(Socket socket) throws IOException {
      ObjectOutputStream outputObj = new ObjectOutputStream(socket.getOutputStream());
      outputObj.writeObject("test");

  }

  private void closeServer() throws IOException {
      serverSocket.close();
  }

  @Test
  void testCommunicate() throws IOException{
    initServer();
    Client client = new Client();
    String res = client.initClient();
    assertEquals("Initiate client successfully", res);
    client.closeClientSocket();
    closeServer();
  }

  @Test
  void testInitClientErr() throws IOException{
    Client client = new Client();
    String res = client.initClient();
    assertEquals("Cannot init Client", res);
  }

  @Test
  void testColor(){
    Client client = new Client();
    assertEquals("green", client.getColor());
  }

//   @Test
//   void testMain() throws IOException{
//     initServer();
//     Client client = new Client();
//     client.communicate();
//     closeServer();
//   }

  @Test
  void testMain() throws IOException{
    initServer();
    Client.main(null);
    closeServer();
  }

  @Test
  void testMainError() throws IOException{
    Client.main(null);

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

  // private RISKMap generateMap(){
  //   ArrayList<Territory> test = new ArrayList<>();
  //   test.add(new Territory("Hogwarts"));
  //   test.add(new Territory("test1"));
  //   test.add(new Territory("test2"));
  //   test.add(new Territory("test3"));
  //   RISKMap myMap = new RISKMap(test);

  //   return myMap;
  // }
}
