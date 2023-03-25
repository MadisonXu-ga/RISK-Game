package edu.duke.ece651.team5.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import edu.duke.ece651.team5.shared.*;

// @ExtendWith(MockitoExtension.class)
public class PlayHandlerTest {
    @Mock
    private ObjectOutputStream mockOos;
    @Mock
    private ObjectInputStream mockOis;
    @Mock
    private GameController mockGameController;

    @Disabled
    @Test
    void testRun() throws SocketException, IOException, ClassNotFoundException {
        // Create test data
        String playerName = "testPlayer";
        Boolean playerConnectionStatus = true;
        Action mockAction = mock(Action.class);
        ArrayList<Action> actionList = new ArrayList<Action>();
        actionList.add(mockAction);
        RISKMap mockRiskMap = mock(RISKMap.class);
        PlayHandler playHandler = new PlayHandler(mockOos,
                mockOis, mockGameController, true, "Green");
        when(mockGameController.getRiskMap()).thenReturn(mockRiskMap);
        // when(mockRiskMap.getPlayerNameList()).thenReturn(Collections.singletonList(playerName));

        // Mock calls to send/receive objects
        when(mockOis.readObject())
                .thenReturn(mockRiskMap) // for first sendObject call
                .thenReturn(mockAction) // for action receive loop
                .thenThrow(new ClassNotFoundException()); // to exit the loop
        when(mockOis.readBoolean()).thenReturn(true); // for action validation loop
        when(mockOis.readUTF()).thenReturn("Correct"); // for action validation loop

        // Run the method being tested
        playHandler.run();

        // Verify that the expected objects were sent/received
        verify(mockOos).writeObject(mockRiskMap);
        verify(mockOos, times(2)).writeObject(anyBoolean());
        verify(mockOos, times(2)).writeObject(anyString());

        // Verify that the expected method calls were made on mock objects
        verify(mockGameController).getRiskMap();
        // verify(mockRiskMap).getPlayerNameList();
    }

    @Test
    void testCheckMoveValid() throws IOException {

        // Create mock objects
        OrderRuleChecker moveActionCheckerMock = mock(OrderRuleChecker.class);
        UnitValidRuleChecker moveActionUnitCheckerMock = mock(UnitValidRuleChecker.class);
        GameController gameControllerMock = mock(GameController.class);
        RISKMap riskMapMock = mock(RISKMap.class);
        MoveOrder moveOrderMock = mock(MoveOrder.class);
        HashMap<String, Integer> oldTerriUnitNumMock = mock(HashMap.class);
        Player playerMock = mock(Player.class);

        // Set up mock object interactions
        when(gameControllerMock.getRiskMap()).thenReturn(riskMapMock);
        when(riskMapMock.getPlayerByName(anyString())).thenReturn(playerMock);
        when(moveActionCheckerMock.checkOrder(any(MoveOrder.class), any(Player.class),
                any(RISKMap.class))).thenReturn(null);
        doNothing().when(moveOrderMock).execute(riskMapMock);

        // Create an instance of the class under test and inject the mock GameController
        // object
        // PlayerConnection playerConnection = mock(PlayerConnection.class);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        PlayHandler playHandler = new PlayHandler(oos,
                ois, gameControllerMock, true, "Green");

        // Call the method under test
        ArrayList<MoveOrder> mos = new ArrayList<>(Arrays.asList(moveOrderMock));
        String result = playHandler.checkMoveValid(mos, moveActionCheckerMock,
                moveActionUnitCheckerMock, oldTerriUnitNumMock);

        // Verify expected method calls on the mocked objects
        verify(gameControllerMock, times(3)).getRiskMap();
        verify(riskMapMock).getPlayerByName(anyString());
        verify(moveActionCheckerMock).checkOrder(any(MoveOrder.class), any(Player.class),
                any(RISKMap.class));
        verify(moveOrderMock).execute(any(RISKMap.class));

        // Verify expected return value
        assertNull(result);
    }

    @Test
    void testCheckAttackValid() {
        // Create mock objects
        OrderRuleChecker attackActionCheckerMock = mock(OrderRuleChecker.class);
        UnitValidRuleChecker attackActionUnitCheckerMock = mock(UnitValidRuleChecker.class);
        GameController gameControllerMock = mock(GameController.class);
        RISKMap riskMapMock = mock(RISKMap.class);
        AttackOrder attackOrderMock = mock(AttackOrder.class);
        HashMap<String, Integer> oldTerriUnitNumMock = mock(HashMap.class);
        Player playerMock = mock(Player.class);

        // Set up mock object interactions
        when(gameControllerMock.getRiskMap()).thenReturn(riskMapMock);
        when(riskMapMock.getPlayerByName(anyString())).thenReturn(playerMock);
        when(attackActionCheckerMock.checkOrder(any(AttackOrder.class), any(Player.class), any(RISKMap.class)))
                .thenReturn(null);

        ArrayList<AttackOrder> mos = new ArrayList<>(Arrays.asList(attackOrderMock));
        when(attackActionUnitCheckerMock.checkAttackOrderUnitValid(eq(riskMapMock), eq(mos)))
                .thenReturn(null);

        // Create an instance of the class under test and inject the mock GameController
        // object
        PlayerConnection playerConnection = mock(PlayerConnection.class);
        PlayHandler playHandler = new PlayHandler(playerConnection.getObjectOutputStream(),
                playerConnection.getObjectInputStream(), gameControllerMock, true, "Green");

        // Call the method under test
        ArrayList<AttackOrder> aos = new ArrayList<>(Arrays.asList(attackOrderMock));
        String result = playHandler.checkAttackValid(aos, attackActionCheckerMock,
                attackActionUnitCheckerMock, oldTerriUnitNumMock);

        // Verify expected method calls on the mocked objects
        verify(gameControllerMock, times(4)).getRiskMap();
        verify(riskMapMock).getPlayerByName(anyString());
        verify(attackActionCheckerMock).checkOrder(any(AttackOrder.class), any(Player.class), any(RISKMap.class));
        when(attackActionUnitCheckerMock.checkAttackOrderUnitValid(any(RISKMap.class), eq(mos)))
                .thenReturn(null);
        // Verify expected return value
        assertNull(result);
    }

    @Test
    void testGetPlayerMoveOrders() {
        // Create mock objects
        Action actionMock = mock(Action.class);
        ArrayList<MoveOrder> moveOrdersMock = mock(ArrayList.class);

        // Set up mock object interactions
        when(actionMock.getMoveOrders()).thenReturn(moveOrdersMock);

        // Create an instance of the class under test and inject the mock Action object
        GameController gameControllerMock = mock(GameController.class);
        PlayerConnection playerConnection = mock(PlayerConnection.class);
        PlayHandler playHandler = new PlayHandler(playerConnection.getObjectOutputStream(),
                playerConnection.getObjectInputStream(), gameControllerMock, true, "Green");
        playHandler.setAction(actionMock);

        // Call the method under test
        ArrayList<MoveOrder> result = playHandler.getPlayerMoveOrders();

        // Verify expected method calls on the mocked objects
        verify(actionMock).getMoveOrders();

        // Verify expected return value
        assertEquals(moveOrdersMock, result);
    }

    @Test
    void testGetPlayerAttackOrders() {
        // Create mock objects
        Action actionMock = mock(Action.class);
        ArrayList<AttackOrder> attackOrdersMock = mock(ArrayList.class);

        // Set up mock object interactions
        when(actionMock.getAttackOrders()).thenReturn(attackOrdersMock);

        // Create an instance of the class under test and inject the mock Action object
        GameController gameControllerMock = mock(GameController.class);
        PlayerConnection playerConnection = mock(PlayerConnection.class);
        PlayHandler playHandler = new PlayHandler(playerConnection.getObjectOutputStream(),
                playerConnection.getObjectInputStream(), gameControllerMock, true, "Green");
        playHandler.setAction(actionMock);

        // Call the method under test
        ArrayList<AttackOrder> result = playHandler.getPlayerAttackOrders();

        // Verify expected method calls on the mocked objects
        verify(actionMock).getAttackOrders();

        // Verify expected return value
        assertEquals(attackOrdersMock, result);
    }
}
