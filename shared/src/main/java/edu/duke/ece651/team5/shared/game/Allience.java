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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alliancePlayers == null) ? 0 : alliancePlayers.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Allience other = (Allience) obj;
        if (alliancePlayers == null) {
            if (other.alliancePlayers != null)
                return false;
        } else if (!alliancePlayers.equals(other.alliancePlayers))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Form allience with:[ " + alliancePlayers + " ]";
    }



    
    
}
