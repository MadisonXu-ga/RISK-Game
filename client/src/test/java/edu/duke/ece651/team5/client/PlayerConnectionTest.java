package edu.duke.ece651.team5.client;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class PlayerConnectionTest {

  @Mock
  private Socket mockSocket;

  @Mock
  private ObjectOutputStream mockOutput;

  @Mock
  private ObjectInputStream mockInput;

  private PlayerConnection playerConnection;

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

  @Test
  public void testWriteData() throws IOException {
      setup();
      Object mockObject = mock(Object.class);

      playerConnection.writeData(mockObject);

      verify(mockOutput, times(1)).reset();
      verify(mockOutput, times(1)).writeObject(mockObject);
  }


  @Test
  public void testReadData() throws IOException, ClassNotFoundException {
      setUp();
      String mockData = "test data";
  
      // create a byte array output stream and write the mock data to it
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
      objectOutputStream.writeObject(mockData);
  
      // create a byte array input stream from the written data
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
      ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
  
      // mock the socket and input stream to return the byte array input stream
      when(mockSocket.getOutputStream()).thenReturn(mockOutput);
      when(mockSocket.getInputStream()).thenReturn(objectInputStream);
  
      Object result = playerConnection.readData();
  
      assertEquals(mockData, result);
      verify(mockInput, never()).readObject(); // verify that the mock input stream was not used
  }
  
  

  @Test
  public void testClose() throws IOException {
      setup();

      playerConnection.close();

      verify(mockOutput, times(1)).close();
      verify(mockInput, times(1)).close();
      verify(mockSocket, times(1)).close();
  }


  public void setUp() throws IOException {
      mockSocket = mock(Socket.class);
      mockOutput = mock(ObjectOutputStream.class);
      mockInput = mock(ObjectInputStream.class);
  
      // mock the socket and input/output streams
      when(mockSocket.getOutputStream()).thenReturn(mockOutput);
      when(mockSocket.getInputStream()).thenReturn(mockInput);
  
      playerConnection = new PlayerConnection(mockSocket);
  }
  
}
