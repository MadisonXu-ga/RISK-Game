package edu.duke.ece651.team5.shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * PlayerConnection handle socket and ObjectInputStream and ObjectOutputStream
 * for Client
 */
public class PlayerConnection {
  private final Socket playerSocket;
  // write data from client to server
  private final ObjectOutputStream output;
  // read data from server to client
  private final ObjectInputStream input;

  /**
   * Initialize socket and objectInputStream and ObjectOutStream
   * 
   * @param playerSocket the client socket
   * @throws IOException for any IO failure
   */
  public PlayerConnection(Socket playerSocket) throws IOException {
    System.out.println(1);
    this.playerSocket = playerSocket;
    System.out.println(2);
    output = new ObjectOutputStream(this.playerSocket.getOutputStream());
    System.out.println(3);
    input = new ObjectInputStream(this.playerSocket.getInputStream());
    System.out.println(4);
  }

  /**
   * handle writing data
   * 
   * @param data the data send to server
   * @throws IOException for any IO failure
   */
  public void writeData(Object data) throws IOException {
    output.reset();
    output.writeObject(data);
  }

  /**
   * handle reading data
   * 
   * @return the object it read from server
   * @throws IOException            for any IO failure
   * @throws ClassNotFoundException for class not found
   */
  public Object readData() throws IOException, ClassNotFoundException {
    return input.readObject();
  }

  /**
   * get object output stream.
   * 
   * @return the ObjectOutputStream
   */
  public ObjectOutputStream getObjectOutputStream() {
    return output;
  }

  /**
   * get object input stream
   * 
   * @return the ObjectInputStream
   */
  public ObjectInputStream getObjectInputStream() {
    return input;
  }

  /**
   * handle closing socket and object stream
   * 
   * @throws IOException for any IO failure
   */
  public void close() throws IOException {
    output.close();
    input.close();
    playerSocket.close();
  }

}
