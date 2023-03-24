package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerConnectionTest {

  @Test
  void testReadAndWrite() throws IOException, ClassNotFoundException {
      Socket mockSocket = mock(Socket.class);
      String data = "data";
      ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
      ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
      objOut.writeObject(data);
      objOut.flush();
      when(mockSocket.getOutputStream()).thenReturn(bytesOut);
  
      ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesOut.toByteArray());
      when(mockSocket.getInputStream()).thenReturn(bytesIn);
  
      PlayerConnection playerConnection = new PlayerConnection(mockSocket);
      playerConnection.writeData(data);
      assertEquals(data, playerConnection.readData());
      playerConnection.close();
  }

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
