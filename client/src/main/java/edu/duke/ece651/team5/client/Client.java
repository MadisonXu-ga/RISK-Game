package edu.duke.ece651.team5.client;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import edu.duke.ece651.team5.shared.*;

/**
 * Client is responsible to handle socket connection with server and to interact with user to play the Game
 */

public class Client {
  //player to play the game
  protected TextPlayer textPlayer;
  //connection to handle socket and object streams
  protected PlayerConnection playerConnection;

  //input and output
  private final BufferedReader inputReader;
  private final PrintStream out;

  private int port;
  private String host;

  protected boolean isLose;

  /**
   * Default constructor to set host and port as predefined
   * @param br the input reader 
   * @param out the output printStream
   */
  public Client(BufferedReader br, PrintStream out){
    this("localhost", 57809, br, out);
  }

  /**
   * Constructor with specified host and port number
   * @param host the host want to connect with 
   * @param port the number of port
   * @param br the input reader 
   * @param out the output printStream
   */
  public Client(String host, int port, BufferedReader br, PrintStream out){
    this.host = host;
    this.port = port;
    this.inputReader = br;
    this.out = out;
    this.isLose = false;
  }

  /**
   * create playerConnection and textPlayer
   * @throws UnknownHostException if unknown host failure
   * @throws IOException if any IO failure
   */
  public void createPlayer() throws UnknownHostException, IOException{
    this.playerConnection = new PlayerConnection(new Socket(host, port));
    this.textPlayer = new TextPlayer(inputReader, out);
  }

  /**
   * receive player name from server and assign it to current player
   * @throws IOException if any IO failure
   * @throws ClassNotFoundException if unknown host failure
   */
  public void handlePlayerName() throws IOException, ClassNotFoundException{
    out.println("\nWaiting to receive your player name...\n");
    String msg = (String) playerConnection.readData();
    //if identified as first player, ask user choice for the number of player for this game
    if(msg.equals("First")){
      int numPlayer = textPlayer.selectNumPlayer();
      out.println("\nSending your choice..\n");
      playerConnection.writeData(numPlayer);
      //get player name for first user
      msg = (String) playerConnection.readData();
    }
    //set player name
    out.println("\nWe got your player name!\n");
    textPlayer.setPlayerName(msg);
  }

  /**
   * handle user choice for their unit placement, send the choices to server
   * and continue to ask user choice if not approved by server
   * Once got approval, display successful message to user
   * @throws IOException if any IO failure
   * @throws ClassNotFoundException if unknown host failure
   */
  public void handlePlacement() throws IOException, ClassNotFoundException{
    boolean complete = false;
    //gather placeInfo from textPlayer
    RISKMap map = recvMap();
    out.println("\nNow you need to decide where to put your territories...\nThink Carefully!\n");
    do{
      HashMap<String, Integer> placeInfo = textPlayer.unitPlacement(map);
      out.println("\nWe got all your choices, sending your choices...\n");
      //write info to server
      playerConnection.writeData(placeInfo);
      //receive approval or not from server
      complete = isValidFromServer();
      //display result accordingly
      textPlayer.printPlacementResult(complete);
    }while(!complete);
  }

  /**
   * if current player is lose, only display map to user
   * else, handle user choice for each playing turn, send user choices(orders) to server
   * and continue to ask user choice if not approved by server
   * Once got approval, display successful message to user
   * @throws IOException if any IO failure
   * @throws ClassNotFoundException if unknown host failure
   */
  public void playOneTurn() throws IOException, ClassNotFoundException{
    if(isLose){
      out.println("\n------Spectator Mode------\n");
      textPlayer.displayMap(recvMap());
      return;
    }
    out.println("\nNow it's time to play the game!\n");
    boolean complete = false;
    RISKMap map = recvMap();
    do{
      //gather actions(orders) from textPlayer
      Action actions = textPlayer.playOneTurn(map);
      out.println("\nWe got all your orders, sending your orders...\n");
      //send info to server
      playerConnection.writeData(actions);
      //receive approval or not from server
      complete = isValidFromServer();
      String errMsg = (String) playerConnection.readData();
      System.out.println("why receive string: " +  errMsg);
       //display result accordingly
      textPlayer.printCommitResult(complete, errMsg);
    }while(!complete);
  }

  /**
   * receive each turn's result from server, if there's winner display the message and end the game
   * if current player lose, ask user choice to continue watch the game or exit
   * and send the message to server
   * @return if there's an winner for current turn
   * @throws IOException if any IO failure
   * @throws ClassNotFoundException if unknown host failure
   */
  @SuppressWarnings("unchecked")
  public String checkResult() throws IOException,ClassNotFoundException{
    //receive current turn's result
    //add attackResult and print the attack result
    ArrayList<AttackOrder> attackResult = (isLose) ? null: (ArrayList<AttackOrder>) playerConnection.readData();
    textPlayer.printAttackResult(attackResult);
    //receive current turn's result
    HashMap<String, Boolean> result = (HashMap<String, Boolean>)playerConnection.readData();
    //check if there's a winner
    out.println("\nNow let's check the result of this round...\n");
    String msg = textPlayer.checkWinner(result);
    //if no winner
    if(msg.isEmpty() && !isLose){
      //check if current player lose
      String res = textPlayer.checkIfILose(result);
      //if lose and user choose to exit, return message to close the game
      if(res.equals("Disconnect")) { msg = res; }
      //send response to server
      if(!res.isEmpty()){
        isLose = true;
        playerConnection.writeData(res);
      }else{
        out.println("No winner for this round, let's start a new one!");
      }
    }
    return msg;
  }

  /**
   * handle playing game control
   */
  public void play(){
    try{
      createPlayer();
      handlePlayerName();
      handlePlacement();
      String winner = "";
      while(winner.isEmpty()){
       playOneTurn();
       winner = checkResult();
      }
      out.println("\nSee you next time!\n");
      playerConnection.close();
    }catch(Exception e){
      out.println("Something went wrong... " + e.getMessage());
    }
  }


  /**
   * handle map receiving from server
   * @return the map
   * @throws IOException if any IO failure
   * @throws ClassNotFoundException if unknown host failure
   */
  protected RISKMap recvMap() throws IOException, ClassNotFoundException{
    out.println("\nReceiving RISK map...");
    RISKMap map = (RISKMap)playerConnection.readData();
    out.println("\nReceived Map\n");
    return map;
  }

  /**
   * helper method to receive boolean from server
   * @return the booelan
   * @throws IOException if any IO failure
   * @throws ClassNotFoundException if unknown host failure
   */
  private boolean isValidFromServer() throws IOException, ClassNotFoundException{
    boolean isValid = (Boolean) playerConnection.readData();
    return isValid;
  }

}
