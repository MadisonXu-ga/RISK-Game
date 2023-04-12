package edu.duke.ece651.team5.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.duke.ece651.team5.shared.game.*;
import edu.duke.ece651.team5.shared.Action;
import edu.duke.ece651.team5.shared.PlayerConnection;
import edu.duke.ece651.team5.server.MyEnum.*;

public class UserHandler implements Runnable {

    private PlayerConnection playerConnection;
    private UserManager userManager;
    private HashMap<String, Runnable> operationHandlers;
    // private ArrayList<GameController> allGames;
    private HashMap<Integer, GameController> allGames;
    private User currentUser;
    private UserGameMap userGameMap;
    private HashMap<User, PlayerConnection> clients;

    public UserHandler(PlayerConnection playerConnection, UserManager userManager,
            HashMap<Integer, GameController> allGames,
            UserGameMap userGameMap, HashMap<User, PlayerConnection> clients) {
        this.playerConnection = playerConnection;
        this.userManager = userManager;
        this.operationHandlers = new HashMap<>();

        this.allGames = allGames;
        this.currentUser = null;

        this.userGameMap = userGameMap;

        this.clients = clients;

        // initializa functions
        this.operationHandlers.put("Login", this::handleLogin);
        this.operationHandlers.put("Signup", this::handleSignUp);
        this.operationHandlers.put("New game", this::handleNewGame);
        this.operationHandlers.put("Retrieve active games", this::handleRetrieveActiveGames);
        this.operationHandlers.put("Get joinable games", this::handleGetJoinableGames);
        this.operationHandlers.put("Join game", this::handleJoinGame);
        this.operationHandlers.put("Log out", this::handleLogOut);
        this.operationHandlers.put("Place unit", this::handleUnitPlacement);
        this.operationHandlers.put("Order", this::handleOrders);
    }

    @Override
    public void run() {
        // try 10 times.
        // put this loop into another function (deal sign in)
        while (currentUser == null) {
            try {
                String op = (String) playerConnection.getObjectInputStream().readObject();
                handleUserOperation(op);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }

        // loop for game
        while (currentUser != null) {
            String op;
            try {
                op = (String) playerConnection.getObjectInputStream().readObject();
                handleUserOperation(op);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 
     * @param operation
     */
    protected void handleUserOperation(String operation) {
        operationHandlers.get(operation).run();
    }

    /**
     * Handle login operation
     */
    protected void handleLogin() {
        System.out.println("Dealing login operation...");
        // read info from client
        try {
            ArrayList<String> inputInfo = (ArrayList<String>) playerConnection.readData();
            String inputName = inputInfo.get(0);
            String inputPassword = inputInfo.get(1);

            System.out.println("Receive name: " + inputName + "  password: " + inputPassword);

            // check if user exists
            if (!userManager.findUser(inputName)) {
                playerConnection.writeData("Not exists");
                System.out.println("User not exists");
                return;
            }

            // check if user is logged in
            if (userManager.getUser(inputName).getUserStatus() == UserStatus.LOGGED_IN) {
                playerConnection.writeData("Already logged in");
                return;
            }

            // authenticate password
            if (!userManager.authenticate(inputName, inputPassword)) {
                playerConnection.writeData("Not match");
                System.out.println("Password is wrong");
                return;
            }

            // success
            playerConnection.writeData("Login succeeded");
            System.out.println("User " + inputName + ": Login succeeded");
            userManager.changeUserStatus(inputName, UserStatus.LOGGED_IN);

            currentUser = userManager.getUser(inputName);

            // add user and connection to clients
            clients.put(currentUser, playerConnection);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Handle sign up operation
     */
    protected void handleSignUp() {
        System.out.println("Dealing sign up operation...");
        try {
            // read info from client
            ArrayList<String> inputInfo = (ArrayList<String>) playerConnection.readData();
            String inputName = inputInfo.get(0);
            String inputPassword = inputInfo.get(1);

            System.out.println("Receive name: " + inputName + "  password: " + inputPassword);

            // check if user exists
            if (userManager.findUser(inputName)) {
                playerConnection.writeData("User exists");
                System.out.println("User already existed");
                return;
            }

            // success
            userManager.addUser(inputName, inputPassword);
            playerConnection.writeData("Sign up succeeded");
            System.out.println("User " + inputName + ": Sign up succeeded");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Handle create new game operation
     */
    protected void handleNewGame() {
        System.out.println("Dealing new game operation...");
        try {
            int playerNum = (int) playerConnection.readData();
            System.out.println("New game player num: " + playerNum);
            // create new game
            GameController newGame = new GameController(playerNum);
            // add game to all games
            allGames.put(newGame.getID(), newGame);
            // let user join this game
            String msg = newGame.joinGame(currentUser);
            // actually this will never fail in logic
            if (msg == null) {
                playerConnection.writeData("Create successfully");
                userGameMap.addGameToUser(currentUser, newGame);
                userGameMap.addUserToGame(newGame, currentUser);
                // TODO: send map or send when full? seems when full is better
                playerConnection.writeData(newGame.getUserColor(currentUser));
                System.out.println("Created and joined new game successfully!");
            } else {
                playerConnection.writeData(msg);
                System.out.println("Join failed");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // TODO: now is user's all games not active.
    // need to think delete game or not to fix this part
    // think i should give number of players to client
    protected void handleRetrieveActiveGames() {
        // TODO: seems like this part only need i return game id to client?
        System.out.println("Dealing retrieve active games operation...");
        try {
            ArrayList<Integer> gameIDs = new ArrayList<>();
            for (GameController game : userGameMap.getUserGames(currentUser)) {
                if (game.getStatus() != GameStatus.ENDED && (game.getUserActiveStatus(currentUser) == null
                        || game.getUserActiveStatus(currentUser) == true)) {
                    gameIDs.add(game.getID());
                }
            }
            playerConnection.writeData(gameIDs);

            System.out.println("Send active games' ids to user " + currentUser.getUserName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: have similar problems as above
    protected void handleGetJoinableGames() {
        System.out.println("Dealing joinable games operation...");
        try {
            ArrayList<Integer> gameIDs = new ArrayList<>();
            for (Map.Entry<Integer, GameController> entry : allGames.entrySet()) {
                if (entry.getValue().getStatus() == GameStatus.WAITING
                        && !userGameMap.checkUserinGame(currentUser, entry.getValue())) {
                    gameIDs.add(entry.getKey());
                }
            }
            playerConnection.writeData(gameIDs);

            System.out.println("Send joinable games' ids to user " + currentUser.getUserName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle join game operation
     */
    protected void handleJoinGame() {
        System.out.println("Dealing join game operation...");
        try {
            int gameID = (int) playerConnection.readData();
            GameController gameToJoin = allGames.get(gameID);
            String msg = gameToJoin.joinGame(currentUser);
            // success
            if (msg == null || msg == "Start") {
                userGameMap.addGameToUser(currentUser, gameToJoin);
                userGameMap.addUserToGame(gameToJoin, currentUser);
                playerConnection.writeData("Joined Success");
                playerConnection.writeData(gameToJoin.getUserColor(currentUser));
                System.out.println("User " + currentUser.getUserName() + " joined game " + gameID);
                if (msg == "Start") {
                    broadcastGame(gameToJoin);
                    System.out.println("Game " + gameID + " is ready to start!");
                }
            }
            // fail
            else {
                playerConnection.writeData(msg);
            }

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Send game to all active players in this game
     * 
     * @param gameController
     */
    protected void broadcastGame(GameController gameController) {
        Game game = gameController.getGame();
        for (User user : userGameMap.getGameUsers(gameController)) {
            try {
                Boolean userActiveStatus = gameController.getUserActiveStatus(user);
                if (user.getUserStatus() == UserStatus.LOGGED_IN
                        && (userActiveStatus == true)) {
                    clients.get(user).writeData(game);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handle log out operation
     */
    protected void handleLogOut() {
        // change user status, remove user communication resource
        try {
            currentUser.setUserStatus(UserStatus.LOGGED_OUT);
            playerConnection.close();

            clients.remove(currentUser);

            // tell other users in this user's active games
            ArrayList<GameController> gamesNotStarted = new ArrayList<>();
            for (GameController game : userGameMap.getUserGames(currentUser)) {
                // if game not start, kick user out
                if (game.getStatus() == GameStatus.WAITING) {
                    gamesNotStarted.add(game);
                }
                // if game started, pause game to wait user
                // TODO: i think for now is to do nothing
                // else if (game.getStatus() != GameStatus.ENDED) {
                // // notify all the active players
                // broadcastPauseGame(game);
                // }
            }

            // deal with games that not started
            for (GameController game : gamesNotStarted) {
                game.kickUserOut(currentUser);
                userGameMap.deleteMap(currentUser, game);
            }

            // set current user to null
            currentUser = null;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Send message to active users in active games to pause game
     * 
     * @param game the game to pause
     * @throws IOException
     */
    protected void broadcastPauseGame(GameController game) throws IOException {
        // send pause to every active user in the game
        for (User user : userGameMap.getGameUsers(game)) {
            if (user.getUserStatus() == UserStatus.LOGGED_IN && game.getUserActiveStatus(user)) {
                clients.get(user).writeData("Pause");
                // pause reason (userid)
            }
        }
    }

    /**
     * Send message to active users(actually all) in active games to continue game
     * 
     * @param game the game to continue
     * @throws IOException
     */
    protected void broadcastContinueGame(GameController game) throws IOException {
        for (User user : userGameMap.getGameUsers(game)) {
            if (user.getUserStatus() == UserStatus.LOGGED_IN && game.getUserActiveStatus(user)) {
                clients.get(user).writeData("Continue");
            }
        }
    }

    /**
     * Handle continue game operation
     */
    protected void handleContinueGame() {
        System.out.println("Dealing continue game operation...");
        try {
            int gameID = (int) playerConnection.readData();
            GameController gameController = allGames.get(gameID);
            // send game status to client
            if (gameController.getStatus() == GameStatus.INITIALIZING) {
                playerConnection.writeData("Initializing");
            } else if (gameController.getStatus() == GameStatus.STARTED) {
                playerConnection.writeData("Started");
            }
            // else is wrong.

            playerConnection.writeData(gameController.getGame());

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected void handleUnitPlacement() {
        try {
            int gameID = (int) playerConnection.readData();
            GameController gameController = allGames.get(gameID);

            UnitValidRuleChecker unitValidRuleChecker = new UnitValidRuleChecker();
            boolean isValid = false;

            HashMap<String, Integer> uPs;
            uPs = (HashMap<String, Integer>) playerConnection.readData();
            // 1. checker: check placement number (maybe need to check owner but most on
            // client side)
            isValid = unitValidRuleChecker.checkUnitValid(gameController.getGame().getMap(), uPs);

            if (!isValid) {
                playerConnection.writeData("Unit number invalid");
                return;
            }
            // 2. if valid, need to place them
            String msg = gameController.initializeGame(currentUser, uPs);

            // 3. send to client whether it is valid (string maybe)
            // success and finished
            if (msg.equals("Placement finished")) {
                playerConnection.writeData("Placement succeeded");
                broadcastGame(gameController);
                return;
            }
            // success but not finished
            playerConnection.writeData(msg);

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    protected void handleOrders() {
        try {
            int gameID = (int) playerConnection.readData();
            GameController gameController = allGames.get(gameID);
            Action action = (Action) playerConnection.readData();
            String msg = gameController.receiveActionFromUser(currentUser, action);
            // action invalid
            if (msg != null) {
                playerConnection.writeData(msg);
            }

            // tell success
            playerConnection.writeData("Order succeeded");

            boolean receiveAll = gameController.tryResolveAllOrders();
            if (receiveAll) {
                // TODO: update unit and resource

                // send map to all active users in this game
                for (User user : userGameMap.getGameUsers(gameController)) {
                    Boolean userActiveStatus = gameController.getUserActiveStatus(user);
                    if (user.getUserStatus() == UserStatus.LOGGED_IN
                            && (userActiveStatus == null || userActiveStatus == true)) {
                        clients.get(user).writeData(gameController.getGame());
                    }
                }

                // check game win or not
                String winName = gameController.checkGameWin();
                // if win, send winner name and end this game
                if (winName != null) {
                    playerConnection.writeData(winName);
                    return;
                }

                // no one win until now
                playerConnection.writeData("No winner");

                // check user lose or not
                // lost
                if (gameController.checkUserLose(currentUser)) {
                    playerConnection.writeData("You lost");
                    String lostChoice = (String) playerConnection.readData();
                    if (lostChoice.equals("Disconnect")) {
                        gameController.setUserActiveStatus(currentUser, false);
                    } else if (lostChoice.equals("Display")) {
                        gameController.setUserActiveStatus(currentUser, null);
                    }
                    return;
                }

                playerConnection.writeData("Not lost");
            }

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

}
