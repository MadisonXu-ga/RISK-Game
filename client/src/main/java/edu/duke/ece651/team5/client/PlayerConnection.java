package edu.duke.ece651.team5.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class PlayerConnection {
  private final Socket playerSocket;
  private final ObjectOutputStream output;
  private final ObjectInputStream input;

  public PlayerConnection(Socket playerSocket) throws IOException {
    this.playerSocket = playerSocket;
    output = new ObjectOutputStream(this.playerSocket.getOutputStream());
    input = new ObjectInputStream(this.playerSocket.getInputStream());
  }

  public void writeData(Object data) throws IOException {
    output.reset();
    output.writeObject(data);
  }

  public Object readData() throws IOException, ClassNotFoundException {
    return input.readObject();
  }

  public void close() throws IOException {
    output.close();
    input.close();
    playerSocket.close();
  }
}
