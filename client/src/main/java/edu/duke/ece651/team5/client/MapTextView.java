package edu.duke.ece651.team5.client;

import edu.duke.ece651.team5.shared.*;

/**
 * 
 */
public class MapTextView {
  // private RISKMap map;
  private Game game;
  

  /**
   * @param map riskmap
   */
  public MapTextView(Game game) {
    this.game = game;
  }

  /**
   * prints the map
   * 
   * @return string of viewMap
   */
  public String displayMap() {
    StringBuilder view = new StringBuilder();
    for (Player p : game.getPlayers()) {
      view.append(p.getName() + " player:\n");
      view.append(printPlayerTerry(p));
      view.append("\n");
    }
    return view.toString();
  }

  /**
   * a
   * 
   * @param p player object
   * @return player info in string format
   */
  private String printPlayerTerry(Player p) {
    StringBuilder playerInfo = new StringBuilder();
    playerInfo.append("-".repeat(13));
    playerInfo.append("\n");
    for (Territory t : p.getTerritories()) {
      playerInfo.append(printTerriInfo(t));
    }
    return playerInfo.toString();
  }

  /**
   * @param t territory
   * @return print info of the territory
   */
  private String printTerriInfo(Territory t) {
    int count = 0;
    StringBuilder terriInfo = new StringBuilder();
    terriInfo.append(t.getSoldierArmy().getTotalCountSolider() + " units: in " + t.getName());
    terriInfo.append(" (next to: ");
    for (RISKMap.Edge edge : game.getMap().getConnections(t.getId())) {
      Territory neighbor = game.getMap().getTerritoryById(edge.getTo());
      if (count != 0) {
        terriInfo.append(", ");
      }
      terriInfo.append(neighbor.getName());
      count++;
    }
    terriInfo.append(")\n");
    return terriInfo.toString();
  }
}
