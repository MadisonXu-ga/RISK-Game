package edu.duke.ece651.team5.client;

import edu.duke.ece651.team5.shared.*;

/**
 * 
 */
public class MapTextView {
  private RISKMap map;

  /**
   * @param map riskmap
   */
  public MapTextView(RISKMap map) {
    this.map = map;
  }

  /**
   * prints the map
   * 
   * @return string of viewMap
   */
  public String displayMap() {
    StringBuilder view = new StringBuilder();
    for (Player p : map.getPlayers()) {
      view.append(p.getName() + " player:\n");
      view.append(printPlayerTerry(p));
      view.append("\n");
    }
    return view.toString();
  }

  /**
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
    terriInfo.append(t.getUnitNum(UnitType.SOLDIER) + " units: in " + t.getName());
    terriInfo.append(" (next to: ");
    for (Territory neighbor : map.getAdjacentTerritories(t)) {
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
