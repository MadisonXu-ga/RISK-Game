package edu.duke.ece651.team5.client;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import edu.duke.ece651.team5.shared.*;

public class Client {
  private Socket clientSocket;
  private ObjectOutputStream objOutStream;
  private ObjectInputStream objInStream;
  private TextPlayer textPlayer;
  private RISKMap map;


  private int port;
  private String host;



  /* Default Constructor */
  public Client(){
    this("localhost", 12345);
  }

  /* Constructor with host and port number */
  public Client(String host, int port){
    this.host = host;
    this.port = port;
  }

  public boolean recvMapFromServer() throws IOException, ClassNotFoundException{
   this.map = (RISKMap)objInStream.readObject();
    // objInStream.readObject();
    if(map != null){
      return true;
    }else{
      return false;
    }
  }


  /**
   * create a client socket connection with 
   * @throws IOException
  */
  public String initClient() throws IOException{
    String res = "";
    try{
      clientSocket = new Socket(host, port);
      this.objOutStream = new ObjectOutputStream(clientSocket.getOutputStream());
      this.objInStream = new ObjectInputStream(clientSocket.getInputStream());
      this.textPlayer = new TextPlayer(new BufferedReader(new InputStreamReader(System.in)), System.out);
      res = "Initiate client successfully";
    }catch(IOException e){
      clientSocket = null;
      res = "Cannot init Client";
    }
    return res;
    
  }

  /**
   * close objectInputStream and objectOutputStream
   * @throws IOException
   */
  public void closeResource() throws IOException{
    objInStream.close();
    objOutStream.close();
  }

  /**
   * Clost client socket
   * @throws IOException
   */
  public void closeClientSocket(){
    if(clientSocket != null){
      try{
        clientSocket.close();
      }catch(IOException e){
        // clientSocket = null;
        // System.out.println("Failed to close client socket");
      }
    }
    
  }


  public RISKMap recvMap() throws IOException, ClassNotFoundException{
    RISKMap map = (RISKMap)objInStream.readObject();
    return map;
  }

  public boolean recvFirstPlayerSignal() throws IOException, ClassNotFoundException{
    return isValidFromServer();
  }

  public void setPlayerName() throws IOException, ClassNotFoundException{
    textPlayer.setPlayerName(recvString());
  }

  public void sendInteger(int data) throws IOException{
    objOutStream.writeObject(data);
  }

  public void doPlacement() throws IOException, ClassNotFoundException{
    boolean complete = false;
    do{
      objOutStream.writeObject(textPlayer.unitPlacement(recvMap()));
      complete = isValidFromServer();
      textPlayer.printPlacementResult(complete);
    }while(!complete);
  }

  public void playOneTurn() throws IOException,ClassNotFoundException{
    boolean complete = false;
    do{
      objOutStream.writeObject(textPlayer.playOneTurn(recvMap()));
      complete = isValidFromServer();
      textPlayer.printCommitResult(complete);
    }while(!complete);
  }

  /**
   * Doing the whole communication work with server
   */
  public void play(){
    try{
      initClient();
      recvMapFromServer();
       //todo change to continue to receive until lose
       //check if first player to choose total number of player in the game
      //  if(recvFirstPlayerSignal()){
      //   //if so choose number of player
      //   sendInteger(textPlayer.selectNumPlayer());
      //  }
      //  //receive player name and set it as player name
      //  setPlayerName();
      //  //do unit placment
      //  doPlacement();
      //  //while(not win){
      //   playOneTurn();
          //receive map
          // textPlayer.displayMap();
          // //do one turn
          // textPlayer.playOneTurn();
       //}
      closeResource();
    }catch(Exception e){
      // e.printStackTrace();
    }finally{
      closeClientSocket();
    }
  }

  private boolean isValidFromServer() throws IOException, ClassNotFoundException{
    boolean isValid = (Boolean)objInStream.readObject();
    return isValid;
  }

  public String recvString() throws IOException, ClassNotFoundException{
    String recvStr= (String)objInStream.readObject();
    return recvStr;
  }



  public static void main(String[] args) {
    Client client = new Client();
    client.play();
  }








}
