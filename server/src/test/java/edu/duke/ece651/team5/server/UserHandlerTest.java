package edu.duke.ece651.team5.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.server.MyEnum.GameStatus;
import edu.duke.ece651.team5.server.MyEnum.UserStatus;
import edu.duke.ece651.team5.shared.PlayerConnection;

public class UserHandlerTest {
    @Test
    void testHandleLogin() throws ClassNotFoundException, IOException {
        PlayerConnection mockPlayerConnection = mock(PlayerConnection.class);
        UserManager userManager = new UserManager();
        HashMap<Integer, GameController> mockAllGames = mock(HashMap.class);
        UserGameMap mockUserGameMap = mock(UserGameMap.class);
        HashMap<User, PlayerConnection> mockClients = mock(HashMap.class);
        UserHandler userHandler = new UserHandler(mockPlayerConnection, userManager, mockAllGames, mockUserGameMap,
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
        userHandler.handleLogin();
        verify(mockPlayerConnection).writeData("Login succeeded");

        // password is wrong
        ArrayList<String> inputInfo_wrong = new ArrayList<>();
        inputInfo_wrong.add("user1");
        inputInfo_wrong.add("123abc");
        when(mockPlayerConnection.readData()).thenReturn(inputInfo_wrong);
        userHandler.handleLogin();
        verify(mockPlayerConnection).writeData("Not match");
    }

    @Test
    void testHandleSignUp() throws ClassNotFoundException, IOException {
        // success
        PlayerConnection mockPlayerConnection = mock(PlayerConnection.class);
        UserManager userManager = new UserManager();
        HashMap<Integer, GameController> mockAllGames = mock(HashMap.class);
        UserGameMap mockUserGameMap = mock(UserGameMap.class);
        HashMap<User, PlayerConnection> mockClients = mock(HashMap.class);
        UserHandler userHandler = new UserHandler(mockPlayerConnection, userManager, mockAllGames, mockUserGameMap,
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
    void testHandleRetrieveActiveGames(){}

    @Test
    void testHandleUserOperation() {

    }

    @Test
    void testRun() {

    }
}
