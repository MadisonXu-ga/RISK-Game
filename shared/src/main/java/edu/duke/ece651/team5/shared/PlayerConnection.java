package edu.duke.ece651.team5.shared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 * PlayerConnection handle socket and ObjectInputStream and ObjectOutputStream for Client
 */
public class PlayerConnection {
  private final Socket playerSocket;
  //write data from client to server
  private final ObjectOutputStream output;
  //read data from server to client
  private final ObjectInputStream input;

  /**
   * Initialize socket and objectInputStream and ObjectOutStream
   * @param playerSocket the client socket 
   * @throws IOException for any IO failure
   */
  public PlayerConnection(Socket playerSocket) throws IOException {
    this.playerSocket = playerSocket;
    output = new ObjectOutputStream(this.playerSocket.getOutputStream());
    input = new ObjectInputStream(this.playerSocket.getInputStream());
  }

  /**
   * handle writing data
   * @param data the data send to server
   * @throws IOException for any IO failure
   */
  public void writeData(Object data) throws IOException {
    output.reset();
    output.writeObject(data);
  }

  /**
   * handle reading data
   * @return the object it read from server
   * @throws IOException for any IO failure
   * @throws ClassNotFoundException for class not found
   */
  public Object readData() throws IOException, ClassNotFoundException {
    return input.readObject();
  }

  /**
   * handle closing socket and object stream
   * @throws IOException for any IO failure
   */
  public void close() throws IOException {
    output.close();
    input.close();
    playerSocket.close();
}




}
