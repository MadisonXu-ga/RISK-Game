package edu.duke.ece651.team5.shared;

// import java.io.Serial;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {
  // @Serial
  private static final long serialVersionUID = 1956072100912475484L;
  private String name;
  private ArrayList<Territory> territories;
  private int availableUnit;

  /**
   * @param color string
   */
  public Player(String color) {
    this.name = color;
    this.territories = new ArrayList<>();
    this.availableUnit = 50;
  }

  /**
   * @param aTerritory territory
   */
  public void addTerritory(Territory aTerritory) {
    territories.add(aTerritory);
  }

  public int getAvailableUnit() {
    return availableUnit;
  }

  public ArrayList<Territory> getTerritories() {
    return territories;
  }

  public String getName() {

    return name;
  }

  public void loseTerritory(Territory t) {
    territories.remove(t);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Player player = (Player) o;

    return Objects.equals(name, player.name);
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }
}
