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
        this.playerColors = new ArrayList<>(Arrays.asList("Green", "Blue", "Red", "Yellow"));

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
        System.out.println("Successfully accept the first client!");

        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(firstClientSocket.getOutputStream()));
        oos.writeObject("You are the first player. Please choose the player num in this game!");
        oos.flush();

        System.out.println("Successfully tell it that it is the first client!");

        ObjectInputStream ois = new ObjectInputStream(firstClientSocket.getInputStream());
        // do i need to valid player num?
        this.playerNum = (int) ois.readObject();

        System.out.println(this.playerNum);
        System.out.println("Successfully get the player num!");

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

        System.out.println("Start to accept remaining clients");

        // accept remaining connections
        int acceptNum = 1;
        while (true) {
            Socket clientSocket = this.serverSocket.accept();
            acceptNum += 1;
            clientSockets.add(clientSocket);
            if (acceptNum == this.playerNum) {
                break;
            }
        }
    }

    /*
     * Initialize the game, preparations for starting the game
     */
    public void initGame() throws IOException, InterruptedException {
        // create a Game
        // for each player:
        // inform player who she is (send) (hardcode?)
        // need to send map to player? (send)

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
            // display players' units and territories info (send) x
            // point who you are and provide actions (chooseAction) (send) x

            // record player's action list (receive)

            // may need to check valid
            // apply all the actions
            // check win or lose
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