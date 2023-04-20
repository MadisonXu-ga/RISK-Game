package edu.duke.ece651.team5.shared.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Allience implements Serializable{
    List<Player> alliancePlayers;

    public Allience() {
        alliancePlayers = new ArrayList<>();
    }

    /**
     * add alliancePlayer
     * @param alliancePlayer player to alliance
     */
    public void addAlliance(Player alliancePlayer){
        alliancePlayers.add(alliancePlayer);
    }

    /**
     * remove break alliance player
     * @param breakPlayer player to be removed from alliance
     */
    public void removeAlliance(Player breakPlayer){
        alliancePlayers.remove(breakPlayer);
    }

    public boolean containsAlliance(Player player) {
        return alliancePlayers.contains(player);
    }
    
}
