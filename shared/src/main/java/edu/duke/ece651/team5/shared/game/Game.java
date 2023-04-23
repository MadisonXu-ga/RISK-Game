package edu.duke.ece651.team5.shared.game;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

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

    public boolean checkPlayerAlliance(Player player) {
        return player.hasAlliance();
    }

    public String checkAllAlliance() {
        StringBuilder sb = new StringBuilder();
        for (Player player : players) {
            if (player.hasAlliance()) {
                sb.append(player + " is alliance with " + player.getAlliancePlayer() + "\n");
            }
        }
        return sb.toString();
    }

    public List<Player> findAvailableAlliance(Player currentPlayer) {
        return players.stream()
                .filter(player -> !player.equals(currentPlayer))
                .collect(Collectors.toList());
    }

    public void removeBreakUpAlliance(Player player) {
        for (Territory territory : player.getTerritories()) {
            Territory nearestTerri = map.findNearestNeighbor(territory, player);
            nearestTerri.removeBreakUpAlliance(territory);
        }
        player.removeAlliance();
    }

    public Integer getTotalPlayerNum() {
        return players.size();
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

    /**
     * get player by player object
     * 
     * @param player the target player to find
     * @return the player
     */
    public Player getPlayer(Player player) {
        return players.stream()
                .filter(targetPlayer -> targetPlayer.equals(player))
                .findFirst()
                .orElse(null);

    }

}
