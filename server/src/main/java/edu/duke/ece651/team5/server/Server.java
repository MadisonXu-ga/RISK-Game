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
    private int playerNum;
    private ServerSocket serverSocket;
    private ThreadPoolExecutor threadPool;
    // the sockets and io resources of all the clients
    private ArrayList<Socket> clientSockets;
    private final PrintStream out;

    private ArrayList<PlayerConnection> clientIOs;

    // public GameController gameController;

    // true -> normal accept; null -> lost but watch the game;
    // false -> lost and disconnect.
    HashMap<Integer, Boolean> playerConnectionStatus;

    // ----------add v2 new features------------
    private ArrayList<PlayerConnection> clients;
    private UserManager userManager;
    

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

        this.playerNum = 0;
        this.out = out;
        this.clientSockets = new ArrayList<>();
        this.clientIOs = new ArrayList<>();

        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(32);
        this.threadPool = new ThreadPoolExecutor(20, 20, 100, TimeUnit.SECONDS, workQueue);
        serverSocket.setSoTimeout(1200000);

        // Game info initialization.
        gameController = new GameController();

        this.playerConnectionStatus = new HashMap<>();

        // ------------------v2 new code-------------------------
        this.clients = new ArrayList<>();
        this.userManager = new UserManager();
    }

    /**
     * server start to accept clients(players)
     * 
     * @throws IOException
     */
    public void start() throws IOException {
        while (true) {
            Socket clientSocket = this.serverSocket.accept();
            PlayerConnection playerConnection = new PlayerConnection(clientSocket);
            clients.add(playerConnection);
            UserHandler userHandler = new UserHandler(playerConnection, userManager);
            this.threadPool.execute(userHandler);
        }
    }



    
    // -------------------v1 code-----------------------

    // TODO: may move this method to Game
    /**
     * The method need to call when we want to start a game.
     * 
     * @throws IOException            if any IO failure
     * @throws InterruptedException   if interrupt occurred in thread.sleep()
     * @throws ClassNotFoundException if readObject cast failed
     */
    public void startRISCGame()
            throws IOException, InterruptedException, NumberFormatException, ClassNotFoundException {
        acceptClients();
        initGame();
        playGame();
        stop();
    }

    /**
     * Deal with the first connection.
     * Ask first player to give the total number of players of this game.
     * 
     * @throws IOException            if any IO failure
     * @throws ClassNotFoundException if readObject cast failed
     */
    private void dealWithFirstClient() throws IOException, ClassNotFoundException {
        Socket firstClientSocket = this.serverSocket.accept();
        out.println("Successfully accept the first player!");

        // create ios for first client
        clientIOs.add(new PlayerConnection(firstClientSocket));
        clientIOs.get(0).writeData("First");
        this.playerNum = (int) clientIOs.get(0).readData();

        out.println(
                "Successfully get the player num! This game is going to be played by " + this.playerNum + " players.");

        clientSockets.add(firstClientSocket);
        this.gameController.assignTerritories(playerNum);
    }

    /**
     * Start to accept clients.
     * 
     * @throws IOException            if any IO failure
     * @throws ClassNotFoundException if readObject cast failed
     */
    public void acceptClients() throws IOException, ClassNotFoundException {
        // accept one connection first, get playerNum from it
        dealWithFirstClient();

        out.println("Start to accept remaining clients...");

        // accept remaining connections, save their sockets and IO streams
        int acceptNum = 1;
        while (true) {
            Socket clientSocket = this.serverSocket.accept();
            acceptNum += 1;
            clientSockets.add(clientSocket);
            clientIOs.add(new PlayerConnection(clientSocket));
            out.println("Successfully accept player " + acceptNum);
            if (acceptNum == this.playerNum) {
                break;
            }
        }
    }

    /**
     * Initialize the game, preparations for starting the game
     * Tell player who she is
     * Send map to players
     * 
     * @throws IOException          if any IO failure
     * @throws InterruptedException if interrupt occurred in thread.sleep()
     */
    public void initGame() throws IOException, InterruptedException {
        out.println("Start to initialize the game...");
        // Initialize player connections. At the start, all the players are connected
        // normally.
        for (int i = 0; i < this.playerNum; ++i) {
            playerConnectionStatus.put(i, true);
        }

        // Handle players' initialization process.
        ArrayList<InitializationHandler> handlers = new ArrayList<>();
        for (int i = 0; i < playerNum; ++i) {
            InitializationHandler h = new InitializationHandler(clientIOs.get(i).getObjectOutputStream(),
                    clientIOs.get(i).getObjectInputStream(),
                    gameController.getPlayerName(i), gameController.getRiskMap());
            handlers.add(h);
            this.threadPool.execute(h);
        }

        waitForAllThreadsFinished();

        // resolve unit placement
        for (InitializationHandler h : handlers) {
            gameController.resolveUnitPlacement(h.getUnitPlacement());
        }

        out.println("Game initialization finished!");
    }

    /**
     * Start to play the game
     * Receive all
     * 
     * @throws InterruptedException   if interrupt occurred in thread.sleep()
     * @throws IOException            if any IO failure
     * @throws ClassNotFoundException if readObject cast failed
     */
    public void playGame() throws InterruptedException, IOException, ClassNotFoundException {
        out.println("Let's start to play the game!");
        while (true) {
            out.println("===============================");
            out.println("Ready to start new turn!");
            out.println("===============================");

            // receive actions from players
            ArrayList<PlayHandler> phs = new ArrayList<>();
            for (int i = 0; i < playerNum; ++i) {
                PlayHandler ph = new PlayHandler(clientIOs.get(i).getObjectOutputStream(),
                        clientIOs.get(i).getObjectInputStream(), this.gameController,
                        this.playerConnectionStatus.get(i), this.gameController.getPlayerName(i));
                phs.add(ph);
                this.threadPool.execute(ph);
            }

            waitForAllThreadsFinished();

            out.println("Player finished choosing actions.");

            // get each player's actions and resolve actions.
            // move first
            out.println("Start to resolve move actions.");
            this.resolveAllMoveOrders(playerNum, playerConnectionStatus, phs);
            out.println("Gondor unit after move order: "
                    + this.gameController.getRiskMap().getTerritoryByName("Gondor").getUnitNum(UnitType.SOLDIER));

            // attack later
            out.println("Start to resolve attack actions.");
            HashMap<Integer, ArrayList<AttackOrder>> attackResults = this.resolveAllAttackOrder(playerNum,
                    playerConnectionStatus, phs);

            // send attack results to valid players according to their id
            // (only contains their attack orders)
            sendAttackResultsToValidPlayers(attackResults, playerConnectionStatus, clientIOs);

            // check win or lose or null
            out.print("Start to check players' game status (win/lose/playing) ...");

            // send this turn results to connected players
            PlayerStatusChecker playerStatusChecker = new PlayerStatusChecker();
            HashMap<String, Boolean> playerStatus = playerStatusChecker
                    .getPlayerStatus(this.gameController.getRiskMap());
            sendTurnResultsToConnectedPlayers(playerStatus, playerConnectionStatus, clientIOs);
            out.println("Successfully sent results to players who are still connecting.");

            // check win
            String winPlayerName = playerStatusChecker.checkWin(playerStatus);
            if (winPlayerName != null) {
                out.println("Player " + winPlayerName + " won this game!");
                break;
            } else {
                // receive msgs from all lost players *in this one turn*
                receiveChoicesFromLostPlayers(playerStatus, playerConnectionStatus,
                        gameController.getRiskMap().getPlayers(), clientSockets, clientIOs);
            }

            this.gameController.addOneUnitToTerrirories();

            out.println("This turn is finished.");
        }

        out.println("Game is over!");
    }

    /**
     * Resolve all the move orders from all the players in one turn
     * 
     * @param playerNum              the number of players
     * @param playerConnectionStatus the players' connection staus
     * @param phs                    playerHandlers of all players
     */
    public void resolveAllMoveOrders(int playerNum, HashMap<Integer, Boolean> playerConnectionStatus,
            ArrayList<PlayHandler> phs) {
        for (int i = 0; i < playerNum; ++i) {
            if (playerConnectionStatus.get(i) == null || playerConnectionStatus.get(i) == false) {
                continue;
            }
            ArrayList<MoveOrder> moveOrders = phs.get(i).getPlayerMoveOrders();
            for (MoveOrder mo : moveOrders) {
                mo.execute(this.gameController.getRiskMap());
            }
        }
    }

    /**
     * Resolve all the attack orders from all the players in one turn
     * 
     * @param playerNum              the number of players
     * @param playerConnectionStatus the players' connection staus
     * @param phs                    playerHandlers of all players
     */
    public HashMap<Integer, ArrayList<AttackOrder>> resolveAllAttackOrder(int playerNum,
            HashMap<Integer, Boolean> playerConnectionStatus,
            ArrayList<PlayHandler> phs) {
        HashMap<String, ArrayList<AttackOrder>> attackOrdersGroupByTerritory = new HashMap<>();
        ArrayList<AttackOrder> allAttack = new ArrayList<>();
        for (int i = 0; i < playerNum; ++i) {
            if (playerConnectionStatus.get(i) == null || playerConnectionStatus.get(i) == false) {
                continue;
            }
            allAttack.addAll(phs.get(i).getPlayerAttackOrders());
        }
        this.gameController.groupAttackOrdersByDesTerritory(allAttack, attackOrdersGroupByTerritory);
        System.out.println("================begin execute Attack Order =========");
        this.gameController.executeAttackOrder(allAttack);
        this.gameController.resolveAttackOrder(attackOrdersGroupByTerritory);

        return this.gameController.groupAttackOrdersByPlayers(attackOrdersGroupByTerritory);
    }

    /**
     * receive msgs from all lost players *in this one turn*
     * 
     * @param playerStatus
     * @throws ClassNotFoundException
     * @throws IOException
     */
    protected void receiveChoicesFromLostPlayers(HashMap<String, Boolean> playerStatus,
            HashMap<Integer, Boolean> playerConnectionStatus, ArrayList<Player> players,
            ArrayList<Socket> clientSockets, ArrayList<PlayerConnection> clientIOs)
            throws ClassNotFoundException, IOException {
        for (int i = 0; i < players.size(); ++i) {
            String name = players.get(i).getName();
            //
            if (playerConnectionStatus.get(i) != null && playerStatus.get(name) != null
                    && playerConnectionStatus.get(i) == true && playerStatus.get(name) == false) {
                String lostInfo = (String) clientIOs.get(i).readData();
                // if lost player want to disconnect
                if (lostInfo.equals("Disconnect")) {
                    playerConnectionStatus.put(i, false);
                    clientSockets.get(i).close();
                } else if (lostInfo.equals("Display")) {
                    playerConnectionStatus.put(i, null);
                }
            }
        }
    }

    // Already tested!!
    /**
     * send attack results to valid players
     * 
     * @param attackResults
     * @throws IOException
     */
    protected void sendAttackResultsToValidPlayers(HashMap<Integer, ArrayList<AttackOrder>> attackResults,
            HashMap<Integer, Boolean> playerConnectionStatus, ArrayList<PlayerConnection> clientIOs)
            throws IOException {
        for (int i = 0; i < playerConnectionStatus.size(); ++i) {
            if (playerConnectionStatus.get(i) != null && playerConnectionStatus.get(i) == false) {
                continue;
            }
            if (attackResults.containsKey(i)) {
                clientIOs.get(i).writeData(attackResults.get(i));
            } else {
                ArrayList<AttackOrder> emptyAttackOrder = new ArrayList<>();
                clientIOs.get(i).writeData(emptyAttackOrder);
            }
        }
    }

    // ALready tested!!
    /**
     * Send this turn's results to all connected players
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    protected void sendTurnResultsToConnectedPlayers(HashMap<String, Boolean> playerStatus,
            HashMap<Integer, Boolean> playerConnectionStatus, ArrayList<PlayerConnection> clientIOs)
            throws IOException {
        for (int i = 0; i < playerConnectionStatus.size(); ++i) {
            if (playerConnectionStatus.get(i) != null && playerConnectionStatus.get(i) == false) {
                continue;
            }
            clientIOs.get(i).writeData(playerStatus);
        }
    }

    /**
     * wait for all the tasks to complete
     * wait for 1 second to check
     * 
     * @throws InterruptedException if interrupt occurred in thread.sleep()
     */
    private void waitForAllThreadsFinished() throws InterruptedException {
        while (threadPool.getActiveCount() > 0 || !threadPool.getQueue().isEmpty()) {
            Thread.sleep(1000);
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
