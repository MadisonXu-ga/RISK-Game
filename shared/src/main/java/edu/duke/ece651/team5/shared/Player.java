package edu.duke.ece651.team5.shared;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Player player = (Player) o;

    return Objects.equals(color, player.color);
  }

  @Override
  public int hashCode() {
    return color != null ? color.hashCode() : 0;
  }
}
