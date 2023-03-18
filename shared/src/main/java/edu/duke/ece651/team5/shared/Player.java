package edu.duke.ece651.team5.shared;

import java.util.ArrayList;
import java.io.Serializable;

public class Player implements Serializable{
  private String color;
  private ArrayList<Territory> territories;

  public Player(String color){
    this.color = color;
    this.territories = new ArrayList<>();
  }

  public void addTerritory(Territory aTerritory){

    territories.add(aTerritory);

  }

  public ArrayList<Territory> displayTerritories(){

    return territories;

  }

  public String getName(){

    return color;
  }

}
