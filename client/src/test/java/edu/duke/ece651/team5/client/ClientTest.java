package edu.duke.ece651.team5.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.duke.ece651.team5.shared.PlayerConnection;

public class ClientTest {

    private Client client;

    PlayerConnection playerConnection;

    @BeforeEach
    void setUp() throws UnknownHostException, IOException {

        this.playerConnection = mock(PlayerConnection.class);

        // BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        client = mock(Client.class);
        client.setPlayerConnection(playerConnection);
    }

    @Test
    public void confirmGameTest() throws ClassNotFoundException, IOException {

        String expectedMessage = "Game confirmed";
        when(playerConnection.readData()).thenReturn(expectedMessage + "\n");

        String actualMessage = client.confirmGame();

        // assertEquals(expectedMessage, actualMessage);
        verify(playerConnection, times(1)).readData();
    }

    @Test
    public void emptyfcn() {

    }
}
