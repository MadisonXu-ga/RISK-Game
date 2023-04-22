package edu.duke.ece651.team5.server;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import edu.duke.ece651.team5.shared.*;

import java.io.*;

public class Server {
    // the port of server
    private int port;
    // total number of player in this one game
    private ServerSocket serverSocket;
    private ThreadPoolExecutor threadPool;
    // the sockets and io resources of all the clients
    private ArrayList<Socket> clientSockets;
    private final PrintStream out;

    // true -> normal accept; null -> lost but watch the game;
    // false -> lost and disconnect.
    // TODO: move to gamecontroller
    HashMap<Integer, Boolean> playerConnectionStatus;

    // ----------add v2 new features------------
    private HashMap<User, PlayerConnection> clients;
    private UserManager userManager;
    private HashMap<Integer, GameController> allGames;
    private UserGameMap userGameMap;

    // v3
    private HashMap<User, PlayerChatConnection> clientsChat;

    // private ChatServer chatServer;

    /**
     * Default constructor of server
     * 
     * @param port the port that the server will run on
     * @throws IOException     if any IO failure
     * @throws SocketException if socket create failure
     */
    public Server(int port, PrintStream out) throws IOException, SocketException {
        this.port = port;
        this.serverSocket = new ServerSocket(this.port);
        this.out = out;
        this.clientSockets = new ArrayList<>();

        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(32);
        this.threadPool = new ThreadPoolExecutor(20, 20, 100, TimeUnit.SECONDS, workQueue);
        serverSocket.setSoTimeout(1200000);

        this.playerConnectionStatus = new HashMap<>();

        // ------------------v2 new code-------------------------
        this.clients = new HashMap<>();
        this.userManager = new UserManager();
        this.allGames = new HashMap<>();
        this.userGameMap = new UserGameMap();

        // v3
        this.clientsChat = new HashMap<>();
    }

    /**
     * server start to accept clients(players)
     * 
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void start() throws IOException, ClassNotFoundException {
        while (true) {
            Socket clientSocket_game = this.serverSocket.accept();  // for game
            System.out.println("accept client for game");
            Socket clientSocket_chat = this.serverSocket.accept();  // for chat
            System.out.println("accept client for chat");
            PlayerConnection playerConnection_game = new PlayerConnection(clientSocket_game);
            PlayerChatConnection playerConnection_chat = new PlayerChatConnection(clientSocket_chat);
            // clients.add(playerConnection);
            UserHandler userHandler = new UserHandler(playerConnection_game, playerConnection_chat, userManager, allGames, userGameMap, clients, clientsChat);
            ChatHandler chatHandler = new ChatHandler(playerConnection_chat, allGames, userGameMap, clientsChat);
            this.threadPool.execute(userHandler);
            this.threadPool.execute(chatHandler);
        }
    }

    /**
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
            out.println("Server stopped");
        } catch (IOException e) {
            out.println("Failed to stop server: " + e.getMessage());
        }
    }
}
