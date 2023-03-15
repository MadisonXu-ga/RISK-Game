package edu.duke.ece651.team5.shared;

import java.util.ArrayList;

public class Player {
  private String color;
  private ArrayList<Territory> territories;

  public Player(String color){
    this.color = color;
  }

  public void addTerritory(Territory aTerritory){

    territories.add(aTerritory);

  }

}
