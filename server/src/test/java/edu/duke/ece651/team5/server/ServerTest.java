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
import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.*;

public class ServerTest {
    private Socket socket1;
    private Socket socket2;
    private Socket socket3;

    private void createClient(Socket s, HashMap<String, Integer> unitplacement) {
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

                oos.writeObject(unitplacement);

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
    @Test
    void testMultipleClients() throws SocketException, IOException, InterruptedException, ClassNotFoundException {
        int port = 9999;
        Server server = new Server(port, System.out);
        this.socket1 = new Socket("localhost", port);
        this.socket2 = new Socket("localhost", port);
        this.socket3 = new Socket("localhost", port);

        HashMap<String, Integer> unitplacement1 = new HashMap<>();
        unitplacement1.put("Narnia", 10);
        unitplacement1.put("Scadrial", 5);
        unitplacement1.put("Gondor", 5);
        unitplacement1.put("Thalassia", 5);
        unitplacement1.put("Sylvaria", 5);
        unitplacement1.put("Celestia", 5);
        unitplacement1.put("Ironcliff", 5);
        unitplacement1.put("Draconia", 10);

        HashMap<String, Integer> unitplacement2 = new HashMap<>();
        unitplacement2.put("Elantris", 10);
        unitplacement2.put("Oz", 5);
        unitplacement2.put("Mordor", 5);
        unitplacement2.put("Arathia", 5);
        unitplacement2.put("Kaelindor", 5);
        unitplacement2.put("Frosthold", 5);
        unitplacement2.put("Stormhaven", 5);
        unitplacement2.put("Emberfall", 10);

        HashMap<String, Integer> unitplacement3 = new HashMap<>();
        unitplacement3.put("Midkemia", 10);
        unitplacement3.put("Roshar", 5);
        unitplacement3.put("Hogwarts", 5);
        unitplacement3.put("Eryndor", 5);
        unitplacement3.put("Eterna", 5);
        unitplacement3.put("Shadowmire", 5);
        unitplacement3.put("Mythosia", 5);
        unitplacement3.put("Verdantia", 10);

        

        createClient(socket1, unitplacement1);
        createClient(socket2, unitplacement2);
        createClient(socket3, unitplacement3);

        server.acceptClients();
        server.initGame();

        socket1.close();
        socket2.close();
        socket3.close();

        server.stop();
    }

    @Test
    void testResolveAllMoveOrders() throws SocketException, IOException {
        ArrayList<MoveOrder> p0 = new ArrayList<>();
        p0.add(new MoveOrder("Narnia", "Midkemia", 2, UnitType.SOLDIER, "Green"));
        ArrayList<MoveOrder> p2 = new ArrayList<>();
        p2.add(new MoveOrder("Scadrial", "Roshar", 3, UnitType.SOLDIER, "Blue"));

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

        // GameController gc = new GameController();
        Server server = new Server(12345, System.out);
        server.gameController.assignTerritories(3);
        server.gameController.getRiskMap().getTerritoryByName("Narnia").updateUnitCount(UnitType.SOLDIER, false, 10);
        server.gameController.getRiskMap().getTerritoryByName("Scadrial").updateUnitCount(UnitType.SOLDIER, false, 10);

        server.resolveAllMoveOrders(4, createPlayerConnectionStatus(), test);
        assertEquals(8, server.gameController.getRiskMap().getTerritoryByName("Narnia").getUnitNum(UnitType.SOLDIER));
        assertEquals(7, server.gameController.getRiskMap().getTerritoryByName("Scadrial").getUnitNum(UnitType.SOLDIER));
    }

    @Test
    void testResolveAllAttackOrders() throws SocketException, IOException {
        ArrayList<AttackOrder> p0 = new ArrayList<>();
        p0.add(new AttackOrder("Narnia", "Midkemia", 2, UnitType.SOLDIER, "Green"));
        ArrayList<AttackOrder> p1 = new ArrayList<>();
        p1.add(new AttackOrder("Elantris", "Midkemia", 3, UnitType.SOLDIER, "Blue"));

        PlayHandler mockp0 = mock(PlayHandler.class);
        PlayHandler mockp1 = mock(PlayHandler.class);
        PlayHandler mockp2 = mock(PlayHandler.class);
        PlayHandler mockp3 = mock(PlayHandler.class);
        PlayHandler mockp4 = mock(PlayHandler.class);
        when(mockp0.getPlayerAttackOrders()).thenReturn(p0);
        when(mockp1.getPlayerAttackOrders()).thenReturn(p1);
        ArrayList<PlayHandler> test = new ArrayList<>();
        test.add(mockp0);
        test.add(mockp1);
        test.add(mockp2);
        test.add(mockp3);
        test.add(mockp4);

        // GameController gc = new GameController();
        Server server = new Server(22353, System.out);
        server.gameController.assignTerritories(3);
        server.gameController.assignTerritories(3);
        server.gameController.getRiskMap().getTerritoryByName("Narnia").updateUnitCount(UnitType.SOLDIER, false, 10);
        server.gameController.getRiskMap().getTerritoryByName("Elantris").updateUnitCount(UnitType.SOLDIER, false, 10);
        server.gameController.getRiskMap().getTerritoryByName("Midkemia").updateUnitCount(UnitType.SOLDIER, false, 10);

        server.resolveAllAttackOrder(4, createPlayerConnectionStatus(), test);
        assertEquals(8, server.gameController.getRiskMap().getTerritoryByName("Narnia").getUnitNum(UnitType.SOLDIER));
        assertEquals(7, server.gameController.getRiskMap().getTerritoryByName("Elantris").getUnitNum(UnitType.SOLDIER));
    }

    private HashMap<Integer, Boolean> createPlayerConnectionStatus() {
        HashMap<Integer, Boolean> pcs = new HashMap<>();
        pcs.put(0, true);
        pcs.put(1, true);
        pcs.put(2, true);
        pcs.put(3, false);
        pcs.put(4, null);
        return pcs;
    }
}
