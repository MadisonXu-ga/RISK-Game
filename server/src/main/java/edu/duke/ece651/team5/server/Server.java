package edu.duke.ece651.team5.server;

import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import edu.duke.ece651.team5.shared.RISKMap;

import java.io.*;

public class Server {
    private int port;
    private ServerSocket serverSocket;
    private ThreadPoolExecutor threadPool;
    private int playerNum; // maybe should get from game class?
    private ArrayList<Socket> clientSockets;

    // maybe to change later
    private ArrayList<String> playerColors;

    RISKMap riskMap;

    public Server(int port) throws IOException, SocketException {
        this.port = port;
        this.serverSocket = new ServerSocket(this.port);

        this.playerNum = 0;
        this.playerColors = new ArrayList<>(Arrays.asList("Green", "Blue", "Red"));

        this.clientSockets = new ArrayList<>();

        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(32);
        this.threadPool = new ThreadPoolExecutor(2, 4, 100, TimeUnit.SECONDS, workQueue);
        serverSocket.setSoTimeout(1000);

        // Game info initialization. maybe need to move to another function
        // Territoried need to hardcode or something?
        riskMap = new RISKMap();
    }

    /*
     * The method need to call when we want to start a game.
     */
    public void startRISCGame(int playerNum) throws IOException, InterruptedException {
        acceptClients(playerNum);
        initGame();
        playGame();
        stop();
    }

    /**
     * Start to accept clients.
     * Q: Should I throw the exception out or just handle it inside the server
     * class?
     */
    public void acceptClients(int playerNum) throws IOException {
        // accept connections
        while (true) {
            Socket clientSocket = this.serverSocket.accept();
            this.playerNum += 1;
            clientSockets.add(clientSocket);
            if (this.playerNum == playerNum) {
                break;
            }
        }
    }

    /*
     * Initialize the game, preparations for starting the game
     */
    public void initGame() throws IOException, InterruptedException {
        // for each player:
        // inform player who she is (send) (hardcode?)
        // need to send map to player? (send)
        // assign territory groups to player (send) (hardcode?)
        // need to send units number to player? (send)
        // receive a signal when this player finished placing (recv)

        for (int i = 0; i < playerNum; ++i) {
            ConnectionHandler c = new ConnectionHandler(clientSockets.get(i), playerColors.get(i));
            this.threadPool.execute(c);
        }

        // wait for all the tasks to complete
        while (threadPool.getActiveCount() > 0 || !threadPool.getQueue().isEmpty()) {
            Thread.sleep(1000); // wait for 1 second
        }

        System.out.println("Initial part finished!");
    }

    /*
     * Start to play the game
     */
    public void playGame() {
        // tell every player that placing stage is over, let's start the game! (send)
        // until end, later need to change
        while (true) {
            // for each player:
            // display players' units and territories info (send)
            // point who you are and provide actions (chooseAction) (send)
            // record player's action list (receive)
            // may need to check valid
            // apply all the actions
            // check win or lose
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
            objectOutputStream.writeObject(riskMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Stop to close all the sockets and other resources.
     */
    public void stop() {
        try {
            for (Socket cSocket : clientSockets) {
                cSocket.close();
            }
            this.serverSocket.close();
            System.out.println("Server stopped");
        } catch (IOException e) {
            System.out.println("Failed to stop server: " + e.getMessage());
        }
    }
}