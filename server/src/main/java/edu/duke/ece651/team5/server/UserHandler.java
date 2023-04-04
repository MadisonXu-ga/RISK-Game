package edu.duke.ece651.team5.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.PascalCaseStrategy;

import edu.duke.ece651.team5.shared.Game;
import edu.duke.ece651.team5.shared.PlayerConnection;
import edu.duke.ece651.team5.server.MyEnum.*;

public class UserHandler implements Runnable {

    private PlayerConnection playerConnection;
    private UserManager userManager;
    private HashMap<String, Runnable> operationHandlers;
    private ArrayList<GameController> allGames;
    private User currentUser;
    private UserGameMap userGameMap;
    private HashMap<User, PlayerConnection> clients;

    public UserHandler(PlayerConnection playerConnection, UserManager userManager, ArrayList<GameController> allGames,
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

    // use enum to replace operation name!

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
        // read info from client
        try {
            ArrayList<String> inputInfo = (ArrayList<String>) playerConnection.readData();
            String inputName = inputInfo.get(0);
            String inputPassword = inputInfo.get(1);

            // check if user exists
            if (!userManager.findUser(inputName)) {
                playerConnection.writeData("Not exists");
                return;
            }

            // authenticate password
            if (!userManager.authenticate(inputName, inputPassword)) {
                playerConnection.writeData("Not match");
                return;
            }

            // success
            playerConnection.writeData("Login succeeded");
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
     * 
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    protected void handleSignUp() {
        try {
            // read info from client
            ArrayList<String> inputInfo = (ArrayList<String>) playerConnection.readData();
            String inputName = inputInfo.get(0);
            String inputPassword = inputInfo.get(1);

            // check if user exists
            if (userManager.findUser(inputName)) {
                playerConnection.writeData("User exists");
                return;
            }

            // success
            userManager.addUser(inputName, inputPassword);
            playerConnection.writeData("Sign up succeeded");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected void handleNewGame() {
        try {
            int playerNum = (int) playerConnection.readData();
            // create new game
            GameController newGame = new GameController(playerNum);
            // add game to all games
            allGames.add(newGame);
            // let user join this game
            String msg = newGame.joinGame(currentUser);
            // actually this will never fail in logic
            if (msg == null) {
                playerConnection.writeData("Create successfully");
                userGameMap.addGameToUser(currentUser, newGame);
                userGameMap.addUserToGame(newGame, currentUser);
            } else {
                playerConnection.writeData(msg);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // TODO: now is user's all games not active.
    // need to think delete game or not to fix this part
    // think i should give number of players to client
    protected ArrayList<Integer> handleRetrieveActiveGames() {
        // TODO: seems like this part only need i return game id to client?
        ArrayList<Integer> gameIDs = new ArrayList<>();
        for (GameController game : userGameMap.getUserGames(currentUser)) {
            gameIDs.add(game.getID());
        }
        return gameIDs;
    }

    // TODO: have similar problems as above
    protected ArrayList<Integer> handleGetJoinableGames() {
        ArrayList<Integer> gameIDs = new ArrayList<>();
        for (GameController game : allGames) {
            if (game.getStatus() == GameStatus.WAITING) {
                gameIDs.add(game.getID());
            }
        }

        return gameIDs;
    }

    protected void handleJoinGame() {
        try {
            int gameID = (int) playerConnection.readData();
            for (GameController game : allGames) {
                if (game.getID() == gameID) {
                    String msg = game.joinGame(currentUser);
                    // success
                    if (msg == null) {
                        userGameMap.addGameToUser(currentUser, game);
                        userGameMap.addUserToGame(game, currentUser);
                        playerConnection.writeData("Joined Success");
                    }
                    // fail
                    else {
                        playerConnection.writeData(msg);
                    }
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

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
                    pauseGame(game);
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

    protected void pauseGame(GameController game) throws IOException {
        // send pause to every active user in the game
        for (User user : userGameMap.getGameUsers(game)) {
            if (user.getUserStatus() == UserStatus.LOGGED_IN) {
                clients.get(user).writeData("Pause");
            }
        }
    }

    protected void handleContinueGame() {
        //
    }

}
