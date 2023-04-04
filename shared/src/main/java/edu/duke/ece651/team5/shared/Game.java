package edu.duke.ece651.team5.shared;

import java.util.List;

public class Game {
    private List<Player> players;
    private RISKMap map;


    public Game(List<Player> players, RISKMap map) {
        this.players = players;
        this.map = map;
    }

    public Player getPlayerByName(String name){
        return players
        .stream()
        .filter(player -> player.getName().equals(name))
        .findFirst()
        .orElse(null);
    }


    public List<Player> getPlayers() {
        return this.players;
    }


    public RISKMap getMap() {
        return this.map;
    }

    public Player getPlayeryByName(String name){
        return players.stream()
                    .filter(t -> t.getName().equals(name))
                    .findFirst()
                    .orElse(null);
    }
    
}
