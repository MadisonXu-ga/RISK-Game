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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.duke.ece651.team5.shared.*;

public class ServerTest {
    private Socket socket1;
    private Socket socket2;
    private Socket socket3;

    @Mock
    ArrayList<PlayerConnection> mockClientIOs;

    @Mock
    HashMap<Integer, Boolean> mockPlayerConnectionStatus;

    @Mock
    HashMap<String, Boolean> mockPlayerStatus;

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

    @Test
    void testReceiveChoicesFromLostPlayers() throws SocketException, IOException, ClassNotFoundException {
        MockitoAnnotations.openMocks(this);

        ArrayList<Player> mockPlayers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mockPlayers.add(mock(Player.class));
        }

        for (int i = 0; i < 10; ++i) {
            when(mockPlayers.get(i).getName()).thenReturn("player" + (i + 1));
        }

        HashMap<Integer, Boolean> playerConnectionStatus = new HashMap<>();
        playerConnectionStatus.put(0, null);
        playerConnectionStatus.put(1, false);
        playerConnectionStatus.put(2, true);
        playerConnectionStatus.put(3, null);
        playerConnectionStatus.put(4, false);
        playerConnectionStatus.put(5, true);
        playerConnectionStatus.put(6, null);
        playerConnectionStatus.put(7, false);
        playerConnectionStatus.put(8, true);
        playerConnectionStatus.put(9, true);

        when(mockPlayerStatus.get("player1")).thenReturn(null);
        when(mockPlayerStatus.get("player2")).thenReturn(null);
        when(mockPlayerStatus.get("player3")).thenReturn(null);
        when(mockPlayerStatus.get("player4")).thenReturn(true);
        when(mockPlayerStatus.get("player5")).thenReturn(true);
        when(mockPlayerStatus.get("player6")).thenReturn(true);
        when(mockPlayerStatus.get("player7")).thenReturn(false);
        when(mockPlayerStatus.get("player8")).thenReturn(false);
        when(mockPlayerStatus.get("player9")).thenReturn(false);
        when(mockPlayerStatus.get("player10")).thenReturn(false);

        ArrayList<Socket> mockClientSockets = mock(ArrayList.class);
        for(int i=0; i<10; ++i){
            Socket mockSocket = mock(Socket.class);
            doNothing().when(mockSocket).close();
            when(mockClientSockets.get(i)).thenReturn(mockSocket);
        }
        
        for(int i=0; i<10; ++i){
            PlayerConnection p = mock(PlayerConnection.class);
            when(mockClientIOs.get(i)).thenReturn(p);
        }

        when(mockClientIOs.get(8).readData()).thenReturn("Disconnect");
        when(mockClientIOs.get(9).readData()).thenReturn("Display");

        Server server = new Server(23457, System.out);
        server.receiveChoicesFromLostPlayers(mockPlayerStatus, playerConnectionStatus, mockPlayers, mockClientSockets,
                mockClientIOs);
        verify(mockClientSockets.get(8)).close();
        assertEquals(false, playerConnectionStatus.get(8));
        assertEquals(null, playerConnectionStatus.get(9));

        server.stop();
    }

    @Test
    void testSendAttackResultsToValidPlayers() throws SocketException, IOException {
        MockitoAnnotations.openMocks(this);

        Server server = new Server(23457, System.out);

        HashMap<Integer, ArrayList<AttackOrder>> mockAttackResults = new HashMap<>();

        ArrayList<AttackOrder> attackOrders = new ArrayList<>();
        AttackOrder mockAttackOrder1 = mock(AttackOrder.class);
        attackOrders.add(mockAttackOrder1);
        mockAttackResults.put(0, attackOrders);

        when(mockPlayerConnectionStatus.get(0)).thenReturn(true);
        when(mockPlayerConnectionStatus.get(1)).thenReturn(false);
        when(mockPlayerConnectionStatus.get(2)).thenReturn(null);
        when(mockPlayerConnectionStatus.get(3)).thenReturn(true);

        when(mockPlayerConnectionStatus.size()).thenReturn(4);

        PlayerConnection p1 = mock(PlayerConnection.class);
        PlayerConnection p2 = mock(PlayerConnection.class);
        PlayerConnection p3 = mock(PlayerConnection.class);
        PlayerConnection p4 = mock(PlayerConnection.class);

        when(mockClientIOs.get(0)).thenReturn(p1);
        when(mockClientIOs.get(1)).thenReturn(p2);
        when(mockClientIOs.get(2)).thenReturn(p3);
        when(mockClientIOs.get(3)).thenReturn(p4);

        server.sendAttackResultsToValidPlayers(mockAttackResults, mockPlayerConnectionStatus, mockClientIOs);
        verify(mockClientIOs.get(0)).writeData(mockAttackResults.get(0));
        verify(mockClientIOs.get(1), never()).writeData(new ArrayList<AttackOrder>());
        verify(mockClientIOs.get(2)).writeData(new ArrayList<AttackOrder>());
        verify(mockClientIOs.get(3)).writeData(new ArrayList<AttackOrder>());

        server.stop();
    }

    @Test
    void testSendTurnResultsToConnectedPlayers() throws IOException {
        HashMap<String, Boolean> playerStatus = new HashMap<>();
        HashMap<Integer, Boolean> playerConnectionStatus = new HashMap<>();
        ArrayList<PlayerConnection> clientIOs = new ArrayList<>();

        PlayerConnection clientIO1 = mock(PlayerConnection.class);
        PlayerConnection clientIO2 = mock(PlayerConnection.class);
        PlayerConnection clientIO3 = mock(PlayerConnection.class);

        clientIOs.add(clientIO1);
        clientIOs.add(clientIO2);
        clientIOs.add(clientIO3);

        // Set up the data
        playerStatus.put("player1", true);
        playerStatus.put("player2", false);
        playerStatus.put("player3", false);

        playerConnectionStatus.put(0, true);
        playerConnectionStatus.put(1, null);
        playerConnectionStatus.put(2, false);

        Server server = new Server(23457, System.out);

        // Call the method to test
        server.sendTurnResultsToConnectedPlayers(playerStatus, playerConnectionStatus, clientIOs);

        // Verify that the correct methods were called on the mocked objects
        verify(clientIO1).writeData(playerStatus);
        verify(clientIO2).writeData(playerStatus);
        verify(clientIO3, never()).writeData(playerStatus);

        server.stop();
    }
}
