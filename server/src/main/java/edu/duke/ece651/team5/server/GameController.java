package edu.duke.ece651.team5.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.duke.ece651.team5.server.MyEnum.*;
import edu.duke.ece651.team5.shared.*;

/**
 * Control one game
 */
public class GameController {
    private static int nextId = 1;
    private final int id;

    private GameStatus status;
    private GameStatus statusBeforePause;
    private Game game;
    private int playerNum;
    private int userNum;

    private ArrayList<Player> players;
    private HashMap<String, User> playerToUserMap;
    private HashMap<User, Boolean> userActiveStatus;

    // private HashMap<String, >

    public GameController(int playerNum) {
        this.id = nextId;
        ++nextId;

        this.status = GameStatus.WAITING;
        this.statusBeforePause = status;
        this.playerNum = playerNum;
        this.userNum = 0;
        this.players = new ArrayList<>();
        this.playerToUserMap = new HashMap<>();
        this.userActiveStatus = new HashMap<>();

        createPlayers(playerNum);
    }

    public int getID() {
        return id;
    }

    public GameStatus getStatus() {
        return status;
    }

    /**
     * Create default players according to player num
     * 
     * @param playerNum the number of players in this gamef
     */
    protected void createPlayers(int playerNum) {
        String[] colors = { "Green", "Blue", "Red", "Yellow" };
        for (int i = 0; i < playerNum; ++i) {
            players.add(new Player(colors[i]));
        }
    }

    /**
     * Mainly for test
     * 
     * @return
     */
    protected ArrayList<Player> getPlayers() {
        return this.players;
    }

    /**
     * User try to join Game
     * 
     * @param user
     * @return if null, joined successfully, else return failed msg
     */
    public synchronized String joinGame(User user) {
        if (status != GameStatus.WAITING) {
            return "Full";
        }
        // TODO: check if user is already in this game.

        // find a color for new user in this game
        for (Player player : players) {
            String playerColor = player.getName();
            if (playerToUserMap.containsKey(playerColor)) {
                continue;
            }
            playerToUserMap.put(playerColor, user);
            userActiveStatus.put(user, true);
            break;
        }

        ++userNum;
        if (userNum == playerNum) {
            status = GameStatus.INITIALIZING;
            statusBeforePause = status;

            // TODO: 
        }

        return null;
    }

    /**
     * Kick user out of this game
     * 
     * @param user the user that game want to kick off
     */
    public void kickUserOut(User user) {
        String playerColor = null;
        for (Map.Entry<String, User> entry : playerToUserMap.entrySet()) {
            if (entry.getValue().equals(user)) {
                playerColor = entry.getKey();
                break;
            }
        }
        // find the user and remove it
        if (playerColor != null) {
            playerToUserMap.remove(playerColor);
            userActiveStatus.remove(user);
        }
    }

    /**
     * Use for test
     * 
     * @return
     */
    protected HashMap<String, User> getPlayerToUserMap() {
        return this.playerToUserMap;
    }

    /**
     * Pause this game
     * 
     * @param user user who cause this game to pause
     */
    public void pauseGame(User user) {
        this.status = GameStatus.PAUSED;
        userActiveStatus.put(user, false);
    }

    /**
     * Check whether this game can continue
     * 
     * @param user user who come back to game
     * @return true if game can continue, false if not
     */
    public boolean continueGame(User user) {
        userActiveStatus.put(user, true);
        // check whether all users are active
        for (User u : userActiveStatus.keySet()) {
            if (userActiveStatus.get(u) == false) {
                return false;
            }
        }
        // can continus game
        status = statusBeforePause;
        return true;
    }

    /**
     * Get whether user is active in this game
     * 
     * @param user the user you want to check
     * @return true if active, false if not
     */
    public boolean getUserActiveStatus(User user) {
        return userActiveStatus.get(user);
    }


    public String initializeGame(){
        String msg = null;
        // not in initializing step
        if(this.status != GameStatus.INITIALIZING){
            return "Cannot initialize";
        }

        

        return msg;
    }

}
