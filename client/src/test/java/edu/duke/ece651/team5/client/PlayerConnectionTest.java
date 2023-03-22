package edu.duke.ece651.team5.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.Socket;

public class PlayerConnectionTest {
  @Test
  void testCloseException() throws IOException {
    PlayerConnection playerConnection = mock(PlayerConnection.class);
    doThrow(new IOException()).when(playerConnection).close();
    assertThrows(IOException.class, () -> playerConnection.close());
  }

  @Test
  void testReadDataException() throws IOException {
    Socket playerSocket = mock(Socket.class);
    when(playerSocket.getOutputStream()).thenThrow(new IOException());
    assertThrows(IOException.class, () -> new PlayerConnection(playerSocket));
  }

  @Test
  void testWriteDataException() throws IOException {
    PlayerConnection playerConnection = mock(PlayerConnection.class);
    doThrow(new IOException()).when(playerConnection).writeData("data");
    assertThrows(IOException.class, () -> playerConnection.writeData("data"));
  }
}
