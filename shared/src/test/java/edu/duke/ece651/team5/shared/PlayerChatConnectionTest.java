package edu.duke.ece651.team5.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.jupiter.api.Test;

public class PlayerChatConnectionTest {
    @Test
    void testReadAndWriteString() throws IOException {
        Socket mockSocket = mock(Socket.class);
        String data = "data";
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        PrintWriter objOut = new PrintWriter(bytesOut);
        objOut.println(data);
        objOut.flush();
        when(mockSocket.getOutputStream()).thenReturn(bytesOut);

        ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesOut.toByteArray());
        when(mockSocket.getInputStream()).thenReturn(bytesIn);

        PlayerChatConnection playerConnection = new PlayerChatConnection(mockSocket);
        playerConnection.writeString(data);
        assertEquals(data, playerConnection.readString());
        playerConnection.close();
    }
}
