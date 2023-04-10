package edu.duke.ece651.team5.shared.game;

import java.util.List;

public class Game {
    private List<Player> players;
    private RISKMap map;

    /**
     * Constructor of the game, initialized by a list of players and the map
     * @param players players in the game
     * @param map the map in the game
     */
    public Game(List<Player> players, RISKMap map) {
        this.players = players;
        this.map = map;
    }


    /**
     * Getter for the players
     * @return a list of player
     */
    public List<Player> getPlayers() {
        return this.players;
    }

    /**
     * Getter for the map
     * @return the map
     */
    public RISKMap getMap() {
        return this.map;
    }

    /**
     * get player by its name
     * @param name the name of the player
     * @return the player
     */
    public Player getPlayeryByName(String name){
        return players.stream()
                    .filter(t -> t.getName().equals(name))
                    .findFirst()
                    .orElse(null);
    }
    
}
