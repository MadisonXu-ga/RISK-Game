package edu.duke.ece651.team5.client;
import java.io.*;
import java.net.*;
import java.security.cert.LDAPCertStoreParameters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import edu.duke.ece651.team5.shared.*;

public class Client {
  private Socket clientSocket;
  private ObjectOutputStream objOutStream;
  private ObjectInputStream objInStream;
  private RISKMap map;
  private MapTextView mapView;

  private int port;
  private String host;
  private String color;
  
  // private HashMap<String, ArrayList<Territories>> MapView;

  /* Default Constructor */
  public Client(){
    this.port = 12345;
    this.host = "localhost";
    //temp
    this.color = "green";
  }

  /* Constructor with host and port number */
  public Client(String host, int port){
    this.host = host;
    this.port = port;
    //temp
    this.color = "green";
  }


  public String getColor(){
    return color;
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
  public boolean recvFromServer() throws IOException, ClassNotFoundException{
    map = (RISKMap)objInStream.readObject();
    mapView = new MapTextView(map);
    // objInStream.readObject();
    if(map != null){
      return true;
    }else{
      return false;
    }
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

  public String displayMapView(){
    return mapView.displayMap();
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

  public static void main(String[] args) {
    Client client = new Client();
    client.communicate();
  }








}
