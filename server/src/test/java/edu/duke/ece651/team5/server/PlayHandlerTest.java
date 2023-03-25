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
                verify(attackActionCheckerMock).checkOrder(any(AttackOrder.class), any(Player.class),
                                any(RISKMap.class));
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

        @Test
        void testGetTerrUnitNum() {
                // Create mock objects
                ObjectOutputStream mockOos = mock(ObjectOutputStream.class);
                ObjectInputStream mockOis = mock(ObjectInputStream.class);
                GameController mockGameController = mock(GameController.class);
                RISKMap mockRiskMap = mock(RISKMap.class);
                Territory mockTerritory1 = mock(Territory.class);
                Territory mockTerritory2 = mock(Territory.class);
                ArrayList<Territory> territories = new ArrayList<>();
                territories.add(mockTerritory1);
                territories.add(mockTerritory2);

                // Configure mock objects
                when(mockGameController.getRiskMap()).thenReturn(mockRiskMap);
                when(mockRiskMap.getTerritories()).thenReturn(territories);
                when(mockTerritory1.getName()).thenReturn("Territory 1");
                when(mockTerritory1.getUnitNum(UnitType.SOLDIER)).thenReturn(3);
                when(mockTerritory2.getName()).thenReturn("Territory 2");
                when(mockTerritory2.getUnitNum(UnitType.SOLDIER)).thenReturn(5);

                // Call method being tested
                PlayHandler playHandler = new PlayHandler(mockOos, mockOis, mockGameController, true, "Green");
                HashMap<String, Integer> result = playHandler.getTerrUnitNum();

                // Verify the result
                HashMap<String, Integer> expected = new HashMap<>();
                expected.put("Territory 1", 3);
                expected.put("Territory 2", 5);
                assertEquals(expected, result);
        }

        @Test
        void testRevertTerrUnitChanges() {
                // Create mock objects
                RISKMap mockRiskMap = mock(RISKMap.class);
                Territory mockTerritory1 = mock(Territory.class);
                Territory mockTerritory2 = mock(Territory.class);
                ArrayList<Territory> territories = new ArrayList<>();
                territories.add(mockTerritory1);
                territories.add(mockTerritory2);

                // Configure mock objects
                GameController mockGameController = mock(GameController.class);
                when(mockGameController.getRiskMap()).thenReturn(mockRiskMap);
                when(mockRiskMap.getTerritories()).thenReturn(territories);
                when(mockTerritory1.getName()).thenReturn("Territory 1");
                when(mockTerritory2.getName()).thenReturn("Territory 2");

                // Set initial unit counts
                mockTerritory1.setUnitCount(UnitType.SOLDIER, 3);
                mockTerritory2.setUnitCount(UnitType.SOLDIER, 5);

                // Call method being tested
                PlayHandler playHandler = new PlayHandler(mockOos, mockOis, mockGameController, true, "Green");
                HashMap<String, Integer> oldTerriUnitNum = new HashMap<>();
                oldTerriUnitNum.put("Territory 1", 2);
                oldTerriUnitNum.put("Territory 2", 4);
                playHandler.revertTerrUnitChanges(oldTerriUnitNum);

                // Verify the result
                assertEquals(0, mockTerritory1.getUnitNum(UnitType.SOLDIER));
                assertEquals(0, mockTerritory2.getUnitNum(UnitType.SOLDIER));
        }

        @Test
        void testCheckActions() {
                // Create mock objects
                RISKMap mockRiskMap = mock(RISKMap.class);
                Player mockPlayer = mock(Player.class);
                Territory mockTerritory1 = mock(Territory.class);
                Territory mockTerritory2 = mock(Territory.class);
                Territory mockTerritory3 = mock(Territory.class);
                Territory mockTerritory4 = mock(Territory.class);
                ArrayList<Territory> territories = new ArrayList<>();
                territories.add(mockTerritory1);
                territories.add(mockTerritory2);
                territories.add(mockTerritory3);
                territories.add(mockTerritory4);
                MoveOrder mockMoveOrder = mock(MoveOrder.class);
                AttackOrder mockAttackOrder = mock(AttackOrder.class);
                ArrayList<MoveOrder> moveOrders = new ArrayList<>();
                ArrayList<AttackOrder> attackOrders = new ArrayList<>();
                moveOrders.add(mockMoveOrder);
                attackOrders.add(mockAttackOrder);
                Action mockAction = mock(Action.class);
                when(mockAction.getMoveOrders()).thenReturn(moveOrders);
                when(mockAction.getAttackOrders()).thenReturn(attackOrders);
                when(mockRiskMap.getPlayerByName(anyString())).thenReturn(mockPlayer);
                when(mockRiskMap.getTerritories()).thenReturn(territories);

                when(mockMoveOrder.getSourceName()).thenReturn("Territory 1");
                when(mockMoveOrder.getDestinationName()).thenReturn("Territory 2");
                when(mockAttackOrder.getSourceName()).thenReturn("Territory 3");
                when(mockAttackOrder.getDestinationName()).thenReturn("Territory 4");

                when(mockRiskMap.getTerritoryByName("Territory 1")).thenReturn(mockTerritory1);
                when(mockRiskMap.getTerritoryByName("Territory 2")).thenReturn(mockTerritory2);
                when(mockRiskMap.getTerritoryByName("Territory 3")).thenReturn(mockTerritory3);
                when(mockRiskMap.getTerritoryByName("Territory 4")).thenReturn(mockTerritory4);

                // Set initial unit counts
                when(mockTerritory1.getUnitNum(UnitType.SOLDIER)).thenReturn(3);
                when(mockTerritory2.getUnitNum(UnitType.SOLDIER)).thenReturn(5);
                when(mockTerritory3.getUnitNum(UnitType.SOLDIER)).thenReturn(4);
                when(mockTerritory4.getUnitNum(UnitType.SOLDIER)).thenReturn(2);

                // Configure mock objects
                OrderRuleChecker mockMoveActionChecker = mock(OrderRuleChecker.class);
                OrderRuleChecker mockAttackActionChecker = mock(OrderRuleChecker.class);
                UnitValidRuleChecker mockActionUnitChecker = mock(UnitValidRuleChecker.class);

                when(mockMoveActionChecker.checkOrder(any(MoveOrder.class), any(Player.class), any(RISKMap.class)))
                                .thenReturn(null);
                when(mockAttackActionChecker.checkOrder(any(AttackOrder.class), any(Player.class), any(RISKMap.class)))
                                .thenReturn(null);
                when(mockActionUnitChecker.checkAttackOrderUnitValid(any(RISKMap.class), any(ArrayList.class)))
                                .thenReturn(null);

                // Call method being tested
                GameController gameController = mock(GameController.class);
                when(gameController.getRiskMap()).thenReturn(mockRiskMap);
                Player me = mock(Player.class);
                Player enemy = mock(Player.class);
                when(mockTerritory1.getOwner()).thenReturn(me);
                when(mockTerritory2.getOwner()).thenReturn(me);
                when(mockTerritory3.getOwner()).thenReturn(me);
                when(mockTerritory4.getOwner()).thenReturn(me);

                PlayHandler playHandler = new PlayHandler(mockOos, mockOis, gameController, true, "Green");
                String result = playHandler.checkActions(mockAction);

                // Verify the result
                assertNotNull(result);
        }
}
