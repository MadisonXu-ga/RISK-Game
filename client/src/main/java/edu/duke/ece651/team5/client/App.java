/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.team5.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import edu.duke.ece651.team5.shared.MyName;


public class App {
  public static void main(String[] args) {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    Client client = new Client(input, System.out);
    client.play();
  }
}
