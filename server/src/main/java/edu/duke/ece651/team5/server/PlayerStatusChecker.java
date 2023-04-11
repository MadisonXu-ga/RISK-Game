package edu.duke.ece651.team5.server;

import java.util.HashMap;

public class PlayerStatusChecker {
    /**
     * get player's win/lose/playing status from map
     * 
     * @param riskMap
     * @return
     */

    // TODO: change this to match new map part
    // public HashMap<String, Boolean> getPlayerStatus(RISKMap riskMap) {
    //     HashMap<String, Boolean> playerStatus = new HashMap<>();
    //     ArrayList<Player> players = riskMap.getPlayers();
    //     for (Player player : players) {
    //         if (player.getTerritories().size() == 0) {
    //             playerStatus.put(player.getName(), false);
    //         } else if (player.getTerritories().size() == riskMap.getTerritories().size()) {
    //             playerStatus.put(player.getName(), true);
    //         } else {
    //             playerStatus.put(player.getName(), null);
    //         }
    //     }
    //     return playerStatus;
    // }

    /**
     * check win
     * win return name, otherwise return null
     * 
     * @param playerStatus pass playerStatus to check whether there is any player
     *                     win
     * @return return null if nobody win, winner's name if there is a winner
     */
    public String checkWin(HashMap<String, Boolean> playerStatus) {
        for (String name : playerStatus.keySet()) {
            if (playerStatus.get(name) != null && playerStatus.get(name) == true) {
                return name;
            }
        }
        return null;
    }
}
