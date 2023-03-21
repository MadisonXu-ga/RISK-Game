package edu.duke.ece651.team5.client;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import edu.duke.ece651.team5.shared.*;

public class Client {
  // private Socket clientSocket;
  // private ObjectOutputStream objOutStream;
  // private ObjectInputStream objInStream;
  protected TextPlayer textPlayer;
  protected PlayerConnection playerConnection;

  private final BufferedReader inputReader;
  private final PrintStream out;


  private int port;
  private String host;


  public Client(BufferedReader br, PrintStream out){
    this("localhost", 57809, br, out);
  }

  /* Constructor with host and port number */
  public Client(String host, int port, BufferedReader br, PrintStream out){
    this.host = host;
    this.port = port;
    this.inputReader = br;
    this.out = out;
  }

  public void createPlayer() throws UnknownHostException, IOException{
    this.playerConnection = new PlayerConnection(new Socket(host, port));
    this.textPlayer = new TextPlayer(inputReader, out);
  }


  // public boolean recvMapFromServer() throws IOException, ClassNotFoundException{
  //  this.map = (RISKMap)objInStream.readObject();
  //   objInStream.readObject();
  //   if(map != null){
  //     return true;
  //   }else{
  //     return false;
  //   }
  // }


  /**
   * create a client socket connection with 
   * @throws IOException
  */
  // public void initClient() throws IOException{
  //   try{
  //     // clientSocket = new Socket(host, port);
  //     // this.objOutStream = new ObjectOutputStream(clientSocket.getOutputStream());
  //     // this.objInStream = new ObjectInputStream(clientSocket.getInputStream());
      
  //     out.println("\nInitiate client successfully");
  //   }catch(IOException e){
  //     clientSocket = null;
  //     out.println("Cannot init Client");
  //   }  
  // }


  public void handlePlayerName() throws IOException, ClassNotFoundException{
    out.println("\nWaiting to receive your player name...\n");
    // String msg = (String) objInStream.readObject();
    String msg = (String) playerConnection.readData();
    if(msg.equals("First")){
      int numPlayer = textPlayer.selectNumPlayer();
      out.println("\nSending your choice..\n");
      // objOutStream.reset();
      // objOutStream.writeObject(numPlayer);
      playerConnection.writeData(numPlayer);
      // msg = (String)objInStream.readObject();
      msg = (String) playerConnection.readData();
    }
    out.println("\nWe got your player name!\n");
    textPlayer.setPlayerName(msg);
  }

  public void handlePlacement() throws IOException, ClassNotFoundException{
    out.println("\nNow you need to decide where to put your territories...\nThink Carefully!\n");
    boolean complete = false;
    do{
      HashMap<String, Integer> placeInfo = textPlayer.unitPlacement(recvMap());
      out.println("\nWe got all your choices, sending your choices...\n");
      // objOutStream.reset();
      // objOutStream.writeObject(placeInfo);
      playerConnection.writeData(placeInfo);
      complete = isValidFromServer();
      textPlayer.printPlacementResult(complete);
    }while(!complete);
  }

  public void playOneTurn() throws IOException, ClassNotFoundException{
    out.println("\nNow it's time to play the game!\n");
    boolean complete = false;
    do{
      Action actions = textPlayer.playOneTurn(recvMap());
      out.println("\nWe got all your orders, sending your orders...\n");
      // objOutStream.writeObject(actions);
      playerConnection.writeData(actions);
      complete = isValidFromServer();
      textPlayer.printCommitResult(complete);
    }while(!complete);
  }


  public RISKMap recvMap() throws IOException, ClassNotFoundException{
    out.println("\nReceiving RISK map...");
    // RISKMap map = (RISKMap)objInStream.readObject();
    RISKMap map = (RISKMap)playerConnection.readData();
    out.println("\nReceived Map\n");
    return map;
  }


  @SuppressWarnings("unchecked")
  public String checkResult() throws IOException,ClassNotFoundException{
    out.println("\nSeems like everyone finishes their turn.\nNow let's check the result of this round...\n");
    // HashMap<String, Boolean> result = (HashMap<String, Boolean>)objInStream.readObject();
    HashMap<String, Boolean> result = (HashMap<String, Boolean>)playerConnection.readData();
    String winner = textPlayer.checkWinner(result);
    if(winner.isEmpty()){
      String response = textPlayer.checkIfILose(result);
      if(response.equals("Close")) { winner = response; }
      if(!response.isEmpty()){
        // objOutStream.reset();
        // objOutStream.writeObject(response);
        playerConnection.writeData(response);
      }
    }
    return winner;
  }

    /**
   * close objectInputStream and objectOutputStream
   * @throws IOException
   */
  // public void closeResource() throws IOException{
  //   objInStream.close();
  //   objOutStream.close();
  // }

  /**
   * Clost client socket
   * @throws IOException
   */
  // public void closeClientSocket(){
  //   if(clientSocket != null){
  //     try{
  //       clientSocket.close();
  //     }catch(IOException e){
  //       // clientSocket = null;
  //       // System.out.println("Failed to close client socket");
  //     }
  //   }
  // }

  public void play(){
    try{
      // initClient();
      handlePlayerName();
      handlePlacement();
      String winner = "";
      while(winner.isEmpty()){
       playOneTurn();
       winner = checkResult();
      }
      out.println("\nSee you next time!\n");
      // closeResource();
      playerConnection.close();
    }catch(Exception e){
      // e.printStackTrace();
    }
  }

  private boolean isValidFromServer() throws IOException, ClassNotFoundException{
    // boolean isValid = (Boolean)objInStream.readObject();
    boolean isValid = (Boolean) playerConnection.readData();
    return isValid;
  }


  public static void main(String[] args) {

    // Client client = new Client();
    // client.play();
  }


}
