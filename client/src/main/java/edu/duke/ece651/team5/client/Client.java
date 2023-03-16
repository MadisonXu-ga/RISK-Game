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
  //private RISKMap map;

  private int port;
  private String host;
  private String color;
  // private HashMap<String, ArrayList<Territories>> MapView;

  /* Default Constructor */
  public Client(String color){
    this.port = 12345;
    this.host = "localhost";
    this.color = color;
  }

  /* Constructor with host and port number */
  public Client(String host, int port){
    this.host = host;
    this.port = port;
  }

  public Socket getSocket(){
    return clientSocket;
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
      res = "Initiate client successfully";
      System.out.println("Initiate client successfully");
    }catch(IOException e){
      clientSocket = null;
      res = "Cannot init Client";
    }
    return res;
    
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

  /**
   * Doing the whole communication work with server
   */
  public void communicate(){
    try{
      initClient();
      recvFromServer();
      closeResource();
    }catch(Exception e){
      // e.printStackTrace();
    }finally{
      closeClientSocket();
    }
  }

  // public void getViewMap{
  //   for(Map map: RISKmap.getTerritories()){
  //     MapView.push("player", ArrayList<Territory>);
  //   }
  // }






}
