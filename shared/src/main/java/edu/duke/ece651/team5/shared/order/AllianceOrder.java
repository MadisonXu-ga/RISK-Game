package edu.duke.ece651.team5.shared.order;

import edu.duke.ece651.team5.shared.game.*;

public class AllianceOrder {
    Player player;
    Player targetAlliancePlayer;
    Game game;
    

    public AllianceOrder(Player player, Player targetAlliancePlayer, Game game ){
        this.player = player;
        this.targetAlliancePlayer = targetAlliancePlayer;
        this.game = game;
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
        result = prime * result + ((game == null) ? 0 : game.hashCode());
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
        if (game == null) {
            if (other.game != null)
                return false;
        } else if (!game.equals(other.game))
            return false;
        return true;
    }


    

    


}
