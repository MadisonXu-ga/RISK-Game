package edu.duke.ece651.team5.server;

import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import edu.duke.ece651.team5.shared.*;

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

    // TODO: maybe change to game controller later
    // true -> normal accept; null -> lost but watch the game;
    // false -> lost and disconnect.
    HashMap<Integer, Boolean> playerConnectionStatus;

    public Server(int port) throws IOException, SocketException {
        this.port = port;
        this.serverSocket = new ServerSocket(this.port);

        this.playerNum = 0;
        this.clientSockets = new ArrayList<>();
        this.clientOuts = new ArrayList<>();
        this.clientIns = new ArrayList<>();

        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(32);
        this.threadPool = new ThreadPoolExecutor(4, 4, 100, TimeUnit.SECONDS, workQueue);
        serverSocket.setSoTimeout(1200000);

        // Game info initialization.
        gameController = new GameController();

        this.playerConnectionStatus = new HashMap<>();
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
        this.gameController.assignTerritories(playerNum);
        // initialize
        for (int i = 0; i < this.playerNum; ++i) {
            playerConnectionStatus.put(i, true);
        }
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
    public void playGame() throws InterruptedException, IOException, ClassNotFoundException {
        // tell every player that placing stage is over, let's start the game! (send)
        // until end, later need to change
        System.out.println("Let's start to play the game!");
        while (true) {
            System.out.println("===============================");
            ArrayList<PlayHandler> phs = new ArrayList<>();
            for (int i = 0; i < playerNum; ++i) {
                PlayHandler ph = new PlayHandler(clientOuts.get(i), clientIns.get(i), this.gameController,
                        this.playerConnectionStatus.get(i), this.gameController.getPlayerName(i));
                phs.add(ph);
                this.threadPool.execute(ph);
            }

            // wait for all the tasks to complete
            // wait for 1 second to check
            while (threadPool.getActiveCount() > 0 || !threadPool.getQueue().isEmpty()) {
                Thread.sleep(1000);
            }

            System.out.println("Player finished choosing actions.");

            // get each player's actions and resolve actions.
            // move first
            System.out.println("Start to resolve move actions.");
            for (int i = 0; i < playerNum; ++i) {
                if (playerConnectionStatus.get(i) == null || this.playerConnectionStatus.get(i) == false) {
                    continue;
                }
                ArrayList<MoveOrder> moveOrders = phs.get(i).getPlayerMoveOrders();
                for (MoveOrder mo : moveOrders) {
                    mo.execute(this.gameController.getRiskMap());
                }
            }

            // attack later
            // Territory
            System.out.println("Start to resolve attack actions.");
            HashMap<String, ArrayList<AttackOrder>> attackOrdersGroupByTerritory = new HashMap<>();
            ArrayList<AttackOrder> allAttack = new ArrayList<>();
            for (int i = 0; i < playerNum; ++i) {
                if (playerConnectionStatus.get(i) == null || this.playerConnectionStatus.get(i) == false) {
                    continue;
                }
                allAttack.addAll(phs.get(i).getPlayerAttackOrders());
                // ArrayList<AttackOrder> attackOrders = phs.get(i).getPlayerAttackOrders();

            }
            GroupAttackOrdersByDesTerritory(allAttack, attackOrdersGroupByTerritory);
            // TODO: call resolve attack method
            System.out.println("================begin execute Attack Order =========");
            this.gameController.executeAttackOrder(allAttack);
            this.gameController.resolveAttackOrder(attackOrdersGroupByTerritory);

            // check win or lose or null
            // TODO: abstract to funtion
            System.out.print("Start to check players' game status (win/lose/playing) ...");
            HashMap<String, Boolean> playerStatus = getPlayerStatus(this.gameController.getRiskMap());
            for (int i = 0; i < playerNum; ++i) {
                if (playerConnectionStatus.get(i) != null && this.playerConnectionStatus.get(i) == false) {
                    continue;
                }
                clientOuts.get(i).writeObject(playerStatus);
            }
            System.out.println("Successfully sent results to players who are still connecting.");

            // check win
            String winPlayerName = checkWin(playerStatus);
            if (winPlayerName != null) {
                System.out.println("Player " + winPlayerName + " won this game!");
                // close all the resources.
                // may have already done in the startRISCGame part.
                break;
            } else {
                // receive msgs from all lost players *in this one turn*
                for (int i = 0; i < playerNum; ++i) {
                    String name = this.gameController.getPlayerName(i);
                    //
                    if (this.playerConnectionStatus.get(i) != null && playerStatus.get(name) != null
                            && this.playerConnectionStatus.get(i) == true && playerStatus.get(name) == false) {
                        String lostInfo = (String) clientIns.get(i).readObject();
                        // if lost player want to disconnect
                        if (lostInfo.equals("Disconnect")) {
                            this.playerConnectionStatus.put(i, false);
                            this.clientSockets.get(i).close();
                        } else if (lostInfo.equals("Display")) {
                            this.playerConnectionStatus.put(i, null);
                        }
                    }
                }
            }
            System.out.println(new MapTextView(this.gameController.getRiskMap()).displayMap());
            this.gameController.addOneUnitToTerrirories();

            System.out.println("This turn is finished.");
            System.out.println("Ready to start new turn!");
        }

        System.out.println("Game is over!");
    }

    /*
     * check win
     * win return name, otherwise return null
     */
    private String checkWin(HashMap<String, Boolean> playerStatus) {
        for (String name : playerStatus.keySet()) {
            if (playerStatus.get(name) != null && playerStatus.get(name) == true) {
                return name;
            }
        }
        return null;
    }

    //
    private HashMap<String, Boolean> getPlayerStatus(RISKMap riskMap) {
        HashMap<String, Boolean> playerStatus = new HashMap<>();
        ArrayList<Player> players = riskMap.getPlayers();
        for (Player player : players) {
            if (player.getTerritories().size() == 0) {
                playerStatus.put(player.getName(), false);
            } else if (player.getTerritories().size() == riskMap.getTerritories().size()) {
                playerStatus.put(player.getName(), true);
            } else {
                playerStatus.put(player.getName(), null);
            }
        }
        return playerStatus;
    }

    private void GroupAttackOrdersByDesTerritory(ArrayList<AttackOrder> attackOrders,
            HashMap<String, ArrayList<AttackOrder>> attackOrdersGroupByTerritory) {
        for (AttackOrder attackOrder : attackOrders) {
            String destinationTerr = attackOrder.getDestinationName();
            // String destinationTerr = "Hardcode";
            ArrayList<AttackOrder> terrAtkOrders = new ArrayList<>();
            // if exists
            if (attackOrdersGroupByTerritory.containsKey(destinationTerr)) {
                terrAtkOrders = attackOrdersGroupByTerritory.get(destinationTerr);
            }
            terrAtkOrders.add(attackOrder);
            attackOrdersGroupByTerritory.put(destinationTerr, terrAtkOrders);
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
