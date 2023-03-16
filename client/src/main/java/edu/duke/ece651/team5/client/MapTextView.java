package edu.duke.ece651.team5.client;

import edu.duke.ece651.team5.shared.*;


//todo: currently implement using pseudo code, and missing testCase, will add them later when map finished related features
public class MapTextView {
  private RISKMap map;

  public MapTextView(RISKMap map){
    this.map = map;
  }

  public String displayMap(){
    StringBuilder view = new StringBuilder();
    //for each player in map
    // for(Player p: RISKMap):
      view.append("Green Player: \n");
      view.append(printPlayerTerry());
      view.append("\n");
    // }
    return view.toString();
  }

  private String printPlayerTerry(){
    StringBuilder playerInfo = new StringBuilder();
    playerInfo.append("-".repeat(13));
    //for each territory under player's control
    // for(Territory t: player.currTerritories){
      // playerInfo.append(printTerryInfo(t));
      Territory t = map.getTerritoryByName("Narnia");
      playerInfo.append(printTerriInfo(t));
    return playerInfo.toString();
  }

  //has parameter territory
  private String printTerriInfo(Territory t){
    StringBuilder terriInfo = new StringBuilder();
    terriInfo.append("10 units: in " + t.getName());
    terriInfo.append(" (next to: ");
    //for territory neighbor: currTerri
      //neighbor.getName()
      terriInfo.append("Elantris");
    terriInfo.append(")");

    return terriInfo.toString();
  }
}
