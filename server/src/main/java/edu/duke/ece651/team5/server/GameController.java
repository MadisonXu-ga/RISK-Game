package edu.duke.ece651.team5.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Objects;

import edu.duke.ece651.team5.server.MyEnum.*;

import edu.duke.ece651.team5.shared.game.*;
import edu.duke.ece651.team5.shared.Action;
import edu.duke.ece651.team5.shared.constant.*;
import edu.duke.ece651.team5.shared.datastructure.*;
import edu.duke.ece651.team5.shared.order.*;
import edu.duke.ece651.team5.shared.resource.*;
import edu.duke.ece651.team5.shared.rulechecker.*;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;

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
    private HashMap<String, User> playerToUserMap; // TODO: maybe deleted
    private HashMap<User, Player> userToPlayerMap;
    private HashMap<Player, Action> playerActions;
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
        this.userToPlayerMap = new HashMap<>();
        this.userActiveStatus = new HashMap<>();
        // create players
        createPlayers(playerNum);
        // create game
        this.game = new Game(players, new RISKMap());
    }

    public int getID() {
        return id;
    }

    public GameStatus getStatus() {
        return status;
    }

    public Game getGame() {
        return game;
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
     * Assign territories to players accroding to palyer num
     * 
     * @param numPlayer the number of players in this game
     */
    protected void assignTerritories(int numPlayer) {
        ArrayList<String> terriName = new ArrayList<>(Arrays.asList(
                "Narnia", "Elantris", "Midkemia", "Scadrial", "Oz", "Roshar",
                "Gondor", "Mordor", "Hogwarts", "Thalassia", "Arathia",
                "Eryndor", "Sylvaria", "Kaelindor", "Eterna", "Celestia",
                "Frosthold", "Shadowmire", "Ironcliff", "Stormhaven",
                "Mythosia", "Draconia", "Emberfall", "Verdantia"));

        int numTerritories = terriName.size();

        for (int i = 0; i < numTerritories; ++i) {
            Player player = players.get(i % numPlayer);
            String territoryName = terriName.get(i);
            Territory territory = game.getMap().getTerritoryByName(territoryName);
            player.addTerritory(territory);
            territory.setOwner(player);
        }
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
            userToPlayerMap.put(user, player);
            userActiveStatus.put(user, true);
            break;
        }

        ++userNum;

        return tryStartGame();
    }

    protected String tryStartGame() {
        if (userNum == playerNum) {
            status = GameStatus.INITIALIZING;
            statusBeforePause = status;
            assignTerritories(playerNum);
            return "Start";
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
            userToPlayerMap.remove(user);
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

    /**
     * For each user to initialize their game
     * 
     * @param user
     * @return
     */
    public String initializeGame(User user, HashMap<String, Integer> unitPlacements) {
        String msg = "Placement success";
        // not in initializing step
        if (this.status != GameStatus.INITIALIZING) {
            return "Cannot initialize";
        }

        resolveUnitPlacement(unitPlacements);

        return msg;
    }

    /**
     * Resolve the placement of units
     * 
     * @param unitPlacements unit number to be placed in each territory
     */
    protected void resolveUnitPlacement(HashMap<String, Integer> unitPlacements) {
        for (Map.Entry<String, Integer> entry : unitPlacements.entrySet()) {
            String name = entry.getKey();
            int unitNum = entry.getValue();
            Territory terr = game.getMap().getTerritoryByName(name);
            terr.getSoldierArmy().addSoldier(new Soldier(SoldierLevel.INFANTRY), unitNum);
        }
    }

    
    public void receiveActionFromUser(User user, Action action){
        // //
    }

    

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        GameController gameController = (GameController) object;
        return Objects.equal(id, gameController.id);
    }

    @Override
    public int hashCode() {
        return id;
    }

}
