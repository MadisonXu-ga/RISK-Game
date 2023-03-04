package edu.duke.ece651.team5.client;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Client {
  private Socket clientSocket;
  private ObjectOutputStream objOutStream;
  private ObjectInputStream objInStream;
  //private RISKMap map;

  private int port;
  private String host;
  // private HashMap<String, ArrayList<Territories>> MapView;

  /* Default Constructor */
  public Client(){
    this.port = 12345;
    this.host = "localhost";
  }

  /* Constructor with host and port number */
  public Client(String host, int port){
    this.host = host;
    this.port = port;
  }

  /**
   * create a client socket connection with 
   * @throws IOException
  */
  public void initClient() throws IOException{
    clientSocket = new Socket(host, port);
    objOutStream = new ObjectOutputStream(clientSocket.getOutputStream());
    objInStream = new ObjectInputStream(clientSocket.getInputStream());
    System.out.println("Connect to Server");
  }

  // write to server
  // public void writeToServer(){
  //   objOutStream.writeObject();
  // }
  
  /**
   * read object from server
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public void recvFromServer() throws IOException, ClassNotFoundException{
    //Map = (RISKMap)objInStream.readObject();
    objInStream.readObject();
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
  public void closeClientSocket() throws IOException{
    clientSocket.close();
  }

  /**
   * Doing the whole communication work with server
   */
  public void communicate(){
    try{
      initClient();
      recvFromServer();
      closeResource();
    }catch(Exception e){
      e.printStackTrace();
    }finally{
      try{
        closeClientSocket();
      }catch(IOException e){
        clientSocket = null;
        System.out.println("Error to close Client Socket");
      }
      
    }
  }

  // public void getViewMap{
  //   for(Map map: RISKmap.getTerritories()){
  //     MapView.push("player", ArrayList<Territory>);
  //   }
  // }

  public void displayMap(){
    StringBuilder view = new StringBuilder();
    //for each player in mapView
      view.append(printPlayerTerry());
      view.append("\n");
    // }

  }

  private String printPlayerTerry(){
    StringBuilder playerInfo = new StringBuilder();
    playerInfo.append(printPlayer());
    playerInfo.append("-".repeat(13));
    // for(Mapview view: mapView){
    playerInfo.append(printTerryInfo());

    return playerInfo.toString();
  }

  private String printPlayer(){
    return "";
  }

  private String printTerryInfo(){
    return "";
  }




}
