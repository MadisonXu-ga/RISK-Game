package edu.duke.ece651.team5.server;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.PlayerConnection;

public class UserHandlerTest {
    @Test
    void testHandleLogin() throws ClassNotFoundException, IOException {
        PlayerConnection mockPlayerConnection = mock(PlayerConnection.class);
        UserManager userManager = new UserManager();
        UserHandler userHandler = new UserHandler(mockPlayerConnection, userManager);

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
        UserHandler userHandler = new UserHandler(mockPlayerConnection, userManager);

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
    void testHandleUserOperation() {

    }

    @Test
    void testRun() {

    }
}
