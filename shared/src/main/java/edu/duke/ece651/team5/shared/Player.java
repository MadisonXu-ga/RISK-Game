package edu.duke.ece651.team5.shared;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable{
  private String name;
  private ArrayList<Territory> territories;
  private final int availableUnit = 50;

  public Player(String color){
    this.name = color;
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

    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Player player = (Player) o;

    return Objects.equals(name, player.name);
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }
}
