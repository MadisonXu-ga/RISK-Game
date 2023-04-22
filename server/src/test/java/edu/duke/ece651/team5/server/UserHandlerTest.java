package edu.duke.ece651.team5.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import edu.duke.ece651.team5.server.MyEnum.GameStatus;
import edu.duke.ece651.team5.server.MyEnum.UserStatus;
import edu.duke.ece651.team5.shared.PlayerConnection;

@Disabled
public class UserHandlerTest {
        @Test
        void testHandleLogin() throws ClassNotFoundException, IOException {
                PlayerConnection mockPlayerConnection = mock(PlayerConnection.class);
                UserManager userManager = new UserManager();
                HashMap<Integer, GameController> mockAllGames = mock(HashMap.class);
                UserGameMap mockUserGameMap = mock(UserGameMap.class);
                HashMap<User, PlayerConnection> mockClients = mock(HashMap.class);
                UserHandler userHandler = new UserHandler(mockPlayerConnection, userManager, mockAllGames,
                                mockUserGameMap,
                                mockClients);

                ArrayList<String> inputInfo = new ArrayList<>();
                inputInfo.add("user1");
                inputInfo.add("abc123");

                // not exists
                when(mockPlayerConnection.readData()).thenReturn(inputInfo);
                userHandler.handleLogin();
                verify(mockPlayerConnection).writeData("Not exists");

                // success
                userHandler.handleSignUp();

                // password is wrong
                ArrayList<String> inputInfo_wrong = new ArrayList<>();
                inputInfo_wrong.add("user1");
                inputInfo_wrong.add("123abc");
                when(mockPlayerConnection.readData()).thenReturn(inputInfo_wrong);

                userHandler.handleLogin();
                verify(mockPlayerConnection).writeData("Not match");

                // success
                when(mockPlayerConnection.readData()).thenReturn(inputInfo);
                userHandler.handleLogin();
                verify(mockPlayerConnection).writeData("Login succeeded");

                // already logged in
                userHandler.handleLogin();
                verify(mockPlayerConnection).writeData("Already logged in");
        }

        @Test
        void testHandleSignUp() throws ClassNotFoundException, IOException {
                // success
                PlayerConnection mockPlayerConnection = mock(PlayerConnection.class);
                UserManager userManager = new UserManager();
                HashMap<Integer, GameController> mockAllGames = mock(HashMap.class);
                UserGameMap mockUserGameMap = mock(UserGameMap.class);
                HashMap<User, PlayerConnection> mockClients = mock(HashMap.class);
                UserHandler userHandler = new UserHandler(mockPlayerConnection, userManager, mockAllGames,
                                mockUserGameMap,
                                mockClients);

                ArrayList<String> inputInfo = new ArrayList<>();
                inputInfo.add("user1");
                inputInfo.add("abc123");

                when(mockPlayerConnection.readData()).thenReturn(inputInfo);

                // success
                userHandler.handleSignUp();
                verify(mockPlayerConnection).writeData("Sign up succeeded");

                // exists
                userHandler.handleSignUp();
                verify(mockPlayerConnection).writeData("User exists");
        }

        @Test
        void testHandleNewGame() throws ClassNotFoundException, IOException {
                HashMap<Integer, GameController> allGames = new HashMap<>();
                UserManager userManager = new UserManager();
                PlayerConnection mockPlayerConnection = mock(PlayerConnection.class);
                UserGameMap userGameMap = new UserGameMap();
                HashMap<User, PlayerConnection> mockClients = mock(HashMap.class);

                UserHandler userHandler = new UserHandler(mockPlayerConnection, userManager, allGames, userGameMap,
                                mockClients);

                ArrayList<String> inputInfo = new ArrayList<>(Arrays.asList("user1", "abc123"));
                when(mockPlayerConnection.readData()).thenReturn(inputInfo);
                userHandler.handleSignUp();
                userHandler.handleLogin();

                when(mockPlayerConnection.readData()).thenReturn(3);
                userHandler.handleNewGame();
                verify(mockPlayerConnection).writeData("Create successfully");
                User user = new User("user1", "abc123");

                GameController userGame = userGameMap.getUserGames(user).get(0);
                assertNotNull(userGame);
                assertEquals(user, userGameMap.getGameUsers(userGame).get(0));
                assertTrue(userGame.getStatus().equals(GameStatus.WAITING));
        }

        @Test
        void testHandleRetrieveActiveGames() throws ClassNotFoundException, IOException {
                HashMap<Integer, GameController> allGames = new HashMap<>();
                UserManager userManager = new UserManager();
                PlayerConnection mockPlayerConnection = mock(PlayerConnection.class);
                UserGameMap userGameMap = new UserGameMap();
                HashMap<User, PlayerConnection> mockClients = mock(HashMap.class);

                UserHandler userHandler = new UserHandler(mockPlayerConnection, userManager, allGames, userGameMap,
                                mockClients);

                ArrayList<String> inputInfo = new ArrayList<>(Arrays.asList("user1", "abc123"));
                when(mockPlayerConnection.readData()).thenReturn(inputInfo);
                userHandler.handleSignUp();
                userHandler.handleLogin();

                when(mockPlayerConnection.readData()).thenReturn(3);
                userHandler.handleNewGame();

                when(mockPlayerConnection.readData()).thenReturn(2);
                userHandler.handleNewGame();

                when(mockPlayerConnection.readData()).thenReturn(4);
                userHandler.handleNewGame();
                verify(mockPlayerConnection, times(3)).writeData("Create successfully");

                userHandler.handleRetrieveActiveGames();

                ArgumentCaptor<ArrayList<Integer>> captor = ArgumentCaptor.forClass(ArrayList.class);
                verify(mockPlayerConnection, times(12)).writeData(captor.capture());

                ArrayList<Integer> gameIDs = captor.getValue();
                assertEquals(3, gameIDs.size());
        }

        @Test
        void testHandleGetJoinableGames() throws ClassNotFoundException, IOException {
                HashMap<Integer, GameController> allGames = new HashMap<>();
                UserManager userManager = new UserManager();
                PlayerConnection mockPlayerConnection1 = mock(PlayerConnection.class);
                PlayerConnection mockPlayerConnection2 = mock(PlayerConnection.class);
                UserGameMap userGameMap = new UserGameMap();
                HashMap<User, PlayerConnection> mockClients = mock(HashMap.class);

                UserHandler userHandler1 = new UserHandler(mockPlayerConnection1, userManager, allGames, userGameMap,
                                mockClients);

                UserHandler userHandler2 = new UserHandler(mockPlayerConnection2, userManager, allGames, userGameMap,
                                mockClients);

                ArrayList<String> inputInfo1 = new ArrayList<>(Arrays.asList("user1", "abc123"));
                when(mockPlayerConnection1.readData()).thenReturn(inputInfo1);
                userHandler1.handleSignUp();
                userHandler1.handleLogin();

                ArrayList<String> inputInfo2 = new ArrayList<>(Arrays.asList("user2", "abc123"));
                when(mockPlayerConnection2.readData()).thenReturn(inputInfo2);
                userHandler2.handleSignUp();
                userHandler2.handleLogin();

                when(mockPlayerConnection1.readData()).thenReturn(3);
                userHandler1.handleNewGame();

                when(mockPlayerConnection2.readData()).thenReturn(2);
                userHandler2.handleNewGame();

                userHandler1.handleGetJoinableGames();
                ArgumentCaptor<ArrayList<Integer>> captor1 = ArgumentCaptor.forClass(ArrayList.class);
                verify(mockPlayerConnection1, times(6)).writeData(captor1.capture());
                assertEquals(1, captor1.getValue().size());

                userHandler2.handleGetJoinableGames();
                ArgumentCaptor<ArrayList<Integer>> captor2 = ArgumentCaptor.forClass(ArrayList.class);
                verify(mockPlayerConnection2, times(6)).writeData(captor2.capture());
                assertEquals(1, captor2.getValue().size());
        }

        @Test
        void testHandleJoinGame() throws ClassNotFoundException, IOException {
                HashMap<Integer, GameController> allGames = new HashMap<>();
                UserManager userManager = new UserManager();
                PlayerConnection mockPlayerConnection1 = mock(PlayerConnection.class);
                PlayerConnection mockPlayerConnection2 = mock(PlayerConnection.class);
                UserGameMap userGameMap = new UserGameMap();
                HashMap<User, PlayerConnection> clients = new HashMap<>();

                // create user handlers
                UserHandler userHandler1 = new UserHandler(mockPlayerConnection1, userManager, allGames, userGameMap,
                                clients);

                UserHandler userHandler2 = new UserHandler(mockPlayerConnection2, userManager, allGames, userGameMap,
                                clients);

                // sign up and login
                ArrayList<String> inputInfo1 = new ArrayList<>(Arrays.asList("user1", "abc123"));
                when(mockPlayerConnection1.readData()).thenReturn(inputInfo1);
                userHandler1.handleSignUp();
                userHandler1.handleLogin();

                ArrayList<String> inputInfo2 = new ArrayList<>(Arrays.asList("user2", "abc123"));
                when(mockPlayerConnection2.readData()).thenReturn(inputInfo2);
                userHandler2.handleSignUp();
                userHandler2.handleLogin();

                // make new games
                when(mockPlayerConnection1.readData()).thenReturn(3);
                userHandler1.handleNewGame();

                when(mockPlayerConnection2.readData()).thenReturn(2);
                userHandler2.handleNewGame();

                // try to join game
                int gameID2 = userGameMap.getUserGames(new User("user2", "abc123")).get(0).getID();
                int gameID1 = userGameMap.getUserGames(new User("user1", "abc123")).get(0).getID();

                when(mockPlayerConnection1.readData()).thenReturn(gameID2);
                userHandler1.handleJoinGame();
                // TODO: this is start, can verify sending map later
                verify(mockPlayerConnection1).writeData("Joined Success");
                assertEquals(GameStatus.INITIALIZING, allGames.get(gameID2).getStatus());

                when(mockPlayerConnection2.readData()).thenReturn(gameID1);
                userHandler2.handleJoinGame();
                verify(mockPlayerConnection2).writeData("Joined Success");
                assertEquals(GameStatus.WAITING, allGames.get(gameID1).getStatus());
        }

        @Test
        void testHandleLogOut() throws ClassNotFoundException, IOException {
                HashMap<Integer, GameController> allGames = new HashMap<>();
                UserManager userManager = new UserManager();
                PlayerConnection mockPlayerConnection1 = mock(PlayerConnection.class);
                // PlayerConnection mockPlayerConnection2 = mock(PlayerConnection.class);
                UserGameMap userGameMap = new UserGameMap();
                HashMap<User, PlayerConnection> clients = new HashMap<>();

                UserHandler userHandler1 = new UserHandler(mockPlayerConnection1, userManager, allGames, userGameMap,
                                clients);
                ArrayList<String> inputInfo1 = new ArrayList<>(Arrays.asList("user1", "abc123"));
                when(mockPlayerConnection1.readData()).thenReturn(inputInfo1);
                userHandler1.handleSignUp();
                userHandler1.handleLogin();

                when(mockPlayerConnection1.readData()).thenReturn(3);
                userHandler1.handleNewGame();

                userHandler1.handleLogOut();

                assertNull(userHandler1.getCurrentUser());
        }

        @Test
        void testHandleContinueGame() throws ClassNotFoundException, IOException {
                HashMap<Integer, GameController> allGames = new HashMap<>();
                UserManager userManager = new UserManager();
                PlayerConnection mockPlayerConnection1 = mock(PlayerConnection.class);
                PlayerConnection mockPlayerConnection2 = mock(PlayerConnection.class);
                UserGameMap userGameMap = new UserGameMap();
                HashMap<User, PlayerConnection> clients = new HashMap<>();

                UserHandler userHandler1 = new UserHandler(mockPlayerConnection1, userManager, allGames, userGameMap,
                                clients);
                UserHandler userHandler2 = new UserHandler(mockPlayerConnection2, userManager, allGames, userGameMap,
                                clients);

                ArrayList<String> inputInfo1 = new ArrayList<>(Arrays.asList("user1", "abc123"));
                when(mockPlayerConnection1.readData()).thenReturn(inputInfo1);
                userHandler1.handleSignUp();
                userHandler1.handleLogin();

                ArrayList<String> inputInfo2 = new ArrayList<>(Arrays.asList("user2", "abc123"));
                when(mockPlayerConnection2.readData()).thenReturn(inputInfo2);
                userHandler2.handleSignUp();
                userHandler2.handleLogin();

                when(mockPlayerConnection1.readData()).thenReturn(2);
                userHandler1.handleNewGame();

                when(mockPlayerConnection2.readData()).thenReturn(2);
                userHandler2.handleNewGame();

                int gameID2 = userGameMap.getUserGames(new User("user2", "abc123")).get(0).getID();
                int gameID1 = userGameMap.getUserGames(new User("user1", "abc123")).get(0).getID();

                when(mockPlayerConnection1.readData()).thenReturn(gameID2);
                userHandler1.handleJoinGame();

                when(mockPlayerConnection2.readData()).thenReturn(gameID1);
                userHandler2.handleJoinGame();

                // TODO: make up this after testing placement (Started)

                when(mockPlayerConnection1.readData()).thenReturn(gameID1);
                userHandler1.handleContinueGame();
                verify(mockPlayerConnection1).writeData("Initializing");
                // verify(mockPlayerConnection1).writeData(allGames.get(gameID1));
        }

        @Test
        void testHandleUnitPlacement() {
        }

        @Test
        void testHandleUserOperation() {

        }

        @Test
        void testRun() {

        }
}
