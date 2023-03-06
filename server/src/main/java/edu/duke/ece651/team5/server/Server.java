package edu.duke.ece651.team5.server;

import java.net.*;
import java.util.ArrayList;

import edu.duke.ece651.team5.shared.RISKMap;
import edu.duke.ece651.team5.shared.Territory;

import java.io.*;

public class Server {
    private int port;
    private ServerSocket serverSocket;
    RISKMap riskMap;

    public Server(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + this.port);
        } catch (IOException e) {
            System.out.println("Failed to start server: " + e.getMessage());
        }

        // Game info initialization. maybe need to move to another function
        // Territoried need to hardcode or something?
        ArrayList<Territory> territories = new ArrayList<>();
        territories.add(new Territory("Hogwarts"));
        riskMap = new RISKMap(territories);
    }

    /**
     * Start to accept clients.
     * Q: Should I throw the exception out or just handle it inside the server
     * class?
     */
    public void start() {
        try {
            Socket clientSocket = this.serverSocket.accept();
            System.out.println("Accepted connection from client " + clientSocket.getInetAddress());

        } catch (IOException e) {
            System.out.println("Failed to accept client connection: " + e.getMessage());
        }
    }

    /*
     * Send the Map to a client.
     * Need to refactor latter. Using threads or sth. else.
     */
    public void sendMapToOneClient(Socket clientSocket) {
        System.out.println("sendMapToOneClient");
        try {
            OutputStream out = clientSocket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            // objectOutputStream.writeObject(riskMap);
            System.out.println("1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Stop to close all the sockets and other resources.
     */
    public void stop() {
        try {
            this.serverSocket.close();
            System.out.println("Server stopped");
        } catch (IOException e) {
            System.out.println("Failed to stop server: " + e.getMessage());
        }
    }
}