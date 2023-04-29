/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.team5.server;

import java.io.IOException;
import java.net.SocketException;

// import edu.duke.ece651.team5.shared.MyName;

public class App {
  // public String getMessage() {
  // return "Hello from the server for "+ MyName.getName();
  // }
  public static void main(String[] args)
      throws SocketException, IOException, NumberFormatException, ClassNotFoundException, InterruptedException {
    // App a = new App();
    // System.out.println(a.getMessage());
    Server server = new Server(31011, System.out);
    server.start();
    server.stop();
  }
}
