package edu.duke.ece651.team5.shared;

import java.util.ArrayList;
import java.io.Serializable;

public class Player implements Serializable{
  private String color;
  private ArrayList<Territory> territories;
  private final int availableUnit = 50;

  public Player(String color){
    this.color = color;
    this.territories = new ArrayList<>();
  }

  public void addTerritory(Territory aTerritory){

    territories.add(aTerritory);

  }

  public int getAvailableUnit(){
    return availableUnit;
  }

  public ArrayList<Territory> getTerritories(){

    return territories;

  }

  public String getName(){

    return color;
  }

}
