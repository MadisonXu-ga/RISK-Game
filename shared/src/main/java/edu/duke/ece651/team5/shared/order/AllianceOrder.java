package edu.duke.ece651.team5.shared.order;

import java.io.Serializable;

import edu.duke.ece651.team5.shared.game.*;

public class AllianceOrder implements Serializable{
    Player player;
    Player targetAlliancePlayer;
    

    public AllianceOrder(Player player, Player targetAlliancePlayer){
        this.player = player;
        this.targetAlliancePlayer = targetAlliancePlayer;
    }


    public Player getPlayer() {
        return player;
    }


    public Player getTargetAlliancePlayer() {
        return targetAlliancePlayer;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((player == null) ? 0 : player.hashCode());
        result = prime * result + ((targetAlliancePlayer == null) ? 0 : targetAlliancePlayer.hashCode());
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
        AllianceOrder other = (AllianceOrder) obj;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        if (targetAlliancePlayer == null) {
            if (other.targetAlliancePlayer != null)
                return false;
        } else if (!targetAlliancePlayer.equals(other.targetAlliancePlayer))
            return false;

        return true;
    }


    

    


}
