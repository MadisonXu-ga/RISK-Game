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
    // should send one to change the map
    private int playerNum;
    private ArrayList<Socket> clientSockets;
    private ArrayList<ObjectInputStream> clientIns;
    private ArrayList<ObjectOutputStream> clientOuts;

    private GameController gameController;

    public Server(int port) throws IOException, SocketException {
        this.port = port;
        this.serverSocket = new ServerSocket(this.port);

        this.playerNum = 0;
        this.clientSockets = new ArrayList<>();
        this.clientOuts = new ArrayList<>();
        this.clientIns = new ArrayList<>();

        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(32);
        this.threadPool = new ThreadPoolExecutor(2, 4, 100, TimeUnit.SECONDS, workQueue);
        serverSocket.setSoTimeout(1000);

        // Game info initialization.
        gameController = new GameController();
    }

    /*
     * The method need to call when we want to start a game.
     */
    public void startRISCGame()
            throws IOException, InterruptedException, NumberFormatException, ClassNotFoundException {
        acceptClients();
        initGame();
        playGame();
        stop();
    }

    /*
     * Deal with the first connection.
     * Ask first player to give the total number of players of this game.
     */
    private void dealWithFirstClient() throws IOException, NumberFormatException, ClassNotFoundException {
        Socket firstClientSocket = this.serverSocket.accept();
        System.out.println("Successfully accept the first player!");

        ObjectOutputStream oos = new ObjectOutputStream(firstClientSocket.getOutputStream());
        clientOuts.add(oos);
        oos.writeObject("First");
        oos.flush();

        ObjectInputStream ois = new ObjectInputStream(firstClientSocket.getInputStream());
        // TODO: check playerNum?
        this.playerNum = (int) ois.readObject();
        clientIns.add(ois);

        System.out.println(
                "Successfully get the player num! This game is going to be played by " + this.playerNum + " players.");

        clientSockets.add(firstClientSocket);
    }

    /**
     * Start to accept clients.
     * Q: Should I throw the exception out or just handle it inside the server
     * class?
     * 
     * @throws ClassNotFoundException
     * @throws NumberFormatException
     */
    public void acceptClients() throws IOException, NumberFormatException, ClassNotFoundException {
        // deal with the first one
        dealWithFirstClient();

        System.out.println("Start to accept remaining clients...");

        // accept remaining connections
        int acceptNum = 1;
        while (true) {
            Socket clientSocket = this.serverSocket.accept();
            acceptNum += 1;
            clientSockets.add(clientSocket);
            clientOuts.add(new ObjectOutputStream(clientSocket.getOutputStream()));
            clientIns.add(new ObjectInputStream(clientSocket.getInputStream()));
            System.out.println("Successfully accept player " + acceptNum);
            if (acceptNum == this.playerNum) {
                break;
            }
        }
    }

    /*
     * Initialize the game, preparations for starting the game
     * Tell player who she is
     * Send map to players
     */
    public void initGame() throws IOException, InterruptedException {
        System.out.println("Start to initialize the game...");
        ArrayList<InitializationHandler> handlers = new ArrayList<>();
        for (int i = 0; i < playerNum; ++i) {
            InitializationHandler h = new InitializationHandler(clientOuts.get(i), clientIns.get(i),
                    gameController.getPlayerName(i), gameController.getRiskMap());
            handlers.add(h);
            this.threadPool.execute(h);
        }

        // wait for all the tasks to complete
        // wait for 1 second to check
        while (threadPool.getActiveCount() > 0 || !threadPool.getQueue().isEmpty()) {
            Thread.sleep(1000);
        }

        // resolve unit placement
        for (InitializationHandler h : handlers) {
            gameController.resolveUnitPlacement(h.getUnitPlacement());
        }

        System.out.println("Game initialization finished!");
    }

    /*
     * Start to play the game
     */
    public void playGame() throws InterruptedException {
        // tell every player that placing stage is over, let's start the game! (send)
        // until end, later need to change
        System.out.println("Let's start to play the game!");
        while (true) {
            // for each player:

            // record player's action list (receive)

            // may need to check valid
            // apply all the actions
            // check win or lose
            ArrayList<PlayHandler> phs = new ArrayList<>();
            for (int i = 0; i < playerNum; ++i) {
                PlayHandler ph = new PlayHandler(clientOuts.get(i), clientIns.get(i), this.gameController);
                phs.add(ph);
            }

            // wait for all the tasks to complete
            // wait for 1 second to check
            while (threadPool.getActiveCount() > 0 || !threadPool.getQueue().isEmpty()) {
                Thread.sleep(1000);
            }

            // get each player's actions and resolve actions.
            
            for()
        }
    }

    /*
     * Stop to close all the sockets and other resources.
     */
    public void stop() {
        try {
            // close IOs
            // close sockets
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