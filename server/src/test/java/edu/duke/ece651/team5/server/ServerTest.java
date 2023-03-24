package edu.duke.ece651.team5.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.*;

public class ServerTest {
    private Socket socket1;
    private Socket socket2;
    private Socket socket3;

    private void createClient(Socket s, int port) {
        Thread clientThread = new Thread(() -> {
            try {
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                String msg = (String) ois.readObject();
                System.out.println(msg);
                if (msg.equals("First")) {
                    oos.writeObject(3);
                    String msg2 = (String) ois.readObject();
                    System.out.println(msg2);
                }

                RISKMap r = (RISKMap) ois.readObject();
                assertNotNull(r);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        clientThread.start();
    }

    // TODO: Later need to update this when there is more functions in map
<<<<<<< HEAD
    @Disabled
    @Test
    void testMultipleClients() throws SocketException, IOException, InterruptedException, ClassNotFoundException {
        int port = 9999;
        Server server = new Server(port, System.out);
        this.socket1 = new Socket("localhost", port);
        this.socket2 = new Socket("localhost", port);
        this.socket3 = new Socket("localhost", port);
=======
    // @Disabled
    // @Test
    // void testMultipleClients() throws SocketException, IOException, InterruptedException, ClassNotFoundException {
    //     int port = 9999;
    //     ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    //     BufferedReader input = new BufferedReader(new StringReader("4"));
    //     PrintStream output = new PrintStream(bytes, true);
    //     Server server = new Server(port, output);
    //     this.socket1 = new Socket("localhost", port);
    //     this.socket2 = new Socket("localhost", port);
    //     this.socket3 = new Socket("localhost", port);
>>>>>>> c12bc844bd67163eeeb40b84500819e743cefa0e

    //     createClient(socket1, port);
    //     createClient(socket2, port);
    //     createClient(socket3, port);

    //     server.acceptClients();
    //     server.initGame();

    //     socket1.close();
    //     socket2.close();
    //     socket3.close();

<<<<<<< HEAD
        server.stop();
    }

    @Test
  void testResolveAllMoveOrders() throws SocketException, IOException{
        ArrayList<MoveOrder> p0 = new ArrayList<>();
        p0.add(new MoveOrder("Narnia", "Midkemia", 2, UnitType.SOLDIER, "Green"));
        ArrayList<MoveOrder> p2 = new ArrayList<>();
        p2.add( new MoveOrder("Scadrial", "Roshar", 3, UnitType.SOLDIER, "Blue"));
       
        PlayHandler mockp0 = mock(PlayHandler.class);
        PlayHandler mockp1 = mock(PlayHandler.class);
        PlayHandler mockp2 = mock(PlayHandler.class);
        PlayHandler mockp3 = mock(PlayHandler.class);
        when(mockp0.getPlayerMoveOrders()).thenReturn(p0);
        when(mockp2.getPlayerMoveOrders()).thenReturn(p2);
        ArrayList<PlayHandler> test = new ArrayList<>();
        test.add(mockp0);
        test.add(mockp1);
        test.add(mockp2);
        test.add(mockp3);

        GameController gc = new GameController();
        gc.assignTerritories(3);
        gc.getRiskMap().getTerritoryByName("Narnia").updateUnitCount(UnitType.SOLDIER, false, 10);
        gc.getRiskMap().getTerritoryByName("Scadrial").updateUnitCount(UnitType.SOLDIER, false, 10);
        Server server = new Server(12345, System.out);
        server.resolveAllMoveOrders(4, createPlayerConnectionStatus(), test);
        assertEquals(8, gc.getRiskMap().getTerritoryByName("Narnia").getUnitNum(UnitType.SOLDIER));
        assertEquals(7, gc.getRiskMap().getTerritoryByName("Scadrial").getUnitNum(UnitType.SOLDIER));
  }

  @Test
  void testResolveAllAttackOrders() throws SocketException, IOException{
        ArrayList<AttackOrder> p0 = new ArrayList<>();
        p0.add(new AttackOrder("Narnia", "Midkemia", 2, UnitType.SOLDIER, "Green"));
        ArrayList<AttackOrder> p2 = new ArrayList<>();
        p2.add(new AttackOrder("Scadrial", "Roshar", 3, UnitType.SOLDIER, "Blue"));
       
        PlayHandler mockp0 = mock(PlayHandler.class);
        PlayHandler mockp1 = mock(PlayHandler.class);
        PlayHandler mockp2 = mock(PlayHandler.class);
        PlayHandler mockp3 = mock(PlayHandler.class);
        when(mockp0.getPlayerAttackOrders()).thenReturn(p0);
        when(mockp2.getPlayerAttackOrders()).thenReturn(p2);
        ArrayList<PlayHandler> test = new ArrayList<>();
        test.add(mockp0);
        test.add(mockp1);
        test.add(mockp2);
        test.add(mockp3);

        GameController gc = new GameController();
        gc.assignTerritories(3);
        gc.getRiskMap().getTerritoryByName("Narnia").updateUnitCount(UnitType.SOLDIER, false, 10);
        gc.getRiskMap().getTerritoryByName("Scadrial").updateUnitCount(UnitType.SOLDIER, false, 10);
        Server server = new Server(12345, System.out);
        server.resolveAllAttackOrder(4, createPlayerConnectionStatus(), test);
        assertEquals(8, gc.getRiskMap().getTerritoryByName("Narnia").getUnitNum(UnitType.SOLDIER));
        assertEquals(7, gc.getRiskMap().getTerritoryByName("Scadrial").getUnitNum(UnitType.SOLDIER));
  }

  private HashMap<Integer, Boolean> createPlayerConnectionStatus(){
    HashMap<Integer, Boolean> pcs = new HashMap<>();
    pcs.put(0, true);
    pcs.put(1, false);
    pcs.put(2, true);
    pcs.put(3, null);
    return pcs;
}
=======
    //     server.stop();
    // }
>>>>>>> c12bc844bd67163eeeb40b84500819e743cefa0e
}
