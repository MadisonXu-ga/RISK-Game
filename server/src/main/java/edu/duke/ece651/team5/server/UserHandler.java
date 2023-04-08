package edu.duke.ece651.team5.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.duke.ece651.team5.shared.Game;
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
     * 
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
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
                gameIDs.add(game.getID());
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
                System.out.println("User " + currentUser.getUserName() + " joined game " + gameID);
                if (msg == "Start") {
                    // TODO: sned map to all clients in this game
                    // loop -> playerConnection.writeData(map);
                    System.out.println("User " + currentUser.getUserName() + " joined game " + gameID);
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
                    // game.kickUserOut(currentUser);
                    gamesNotStarted.add(game);
                }
                // if game started, pause game to wait user
                else if (game.getStatus() != GameStatus.ENDED) {
                    // notify all the active players
                    broadcastPauseGame(game);
                }
            }

            // deal with games that not started
            for (GameController game : gamesNotStarted) {
                game.kickUserOut(currentUser);
                userGameMap.deleteMap(currentUser, game);
            }

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
        try {
            int gameID = (int) playerConnection.readData();
            // TODO: change this completely
            // boolean canContinue = allGames.get(gameID).continueGame(currentUser);
            // // if all users are active in this game, can continue
            // if (canContinue) {
            // broadcastContinueGame(allGames.get(gameID));
            // }
            // // else tell this user to pause
            // else {
            // clients.get(currentUser).writeData("Pause");
            // }

            boolean canContinue = allGames.get(gameID).continueGame(currentUser);

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected void handleUnitPlacement() {
        try {
            int gameID = (int) playerConnection.readData();
            // TODO:
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    protected void handleMoveOrder() {
        try {
            int gameID = (int) playerConnection.readData();

        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
