package edu.duke.ece651.team5.shared.game;

import java.io.Serializable;
import java.util.List;

/*
 * This class handle a list of players and a map for the game
 */

public class Game implements Serializable {
    private List<Player> players;
    private RISKMap map;
    private Integer gameID;

    /**
     * Constructor of the game, initialized by a list of players and the map
     * 
     * @param players players in the game
     * @param map     the map in the game
     */
    public Game(List<Player> players, RISKMap map) {
        this.players = players;
        this.map = map;
        this.gameID = null;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }

    public Player getPlayerByName(String name) {
        return players
                .stream()
                .filter(player -> player.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Getter for the players
     * 
     * @return a list of player
     */
    public List<Player> getPlayers() {
        return this.players;
    }

    /**
     * Getter for the map
     * 
     * @return the map
     */
    public RISKMap getMap() {
        return this.map;
    }

    /**
     * get player by its name
     * 
     * @param name the name of the player
     * @return the player
     */
    public Player getPlayeryByName(String name) {
        return players.stream()
                .filter(t -> t.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

}
