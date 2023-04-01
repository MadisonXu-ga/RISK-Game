package edu.duke.ece651.team5.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player {
    private String name;
    private List<Territory> territories;
    private int maxTechnologyLevel;

    /**
     * @param color string
     */
    public Player(String color) {
        this.name = color;
        this.territories = new ArrayList<>();
    }

    /**
     * @param aTerritory territory
     */
    public void addTerritory(Territory aTerritory) {
        territories.add(aTerritory);
    }

    public List<Territory> getTerritories() {
        return territories;
    }

    public int getMaxTechnologyLevel() {
        return this.maxTechnologyLevel;
    }

    public void setMaxTechnologyLevel(int maxTechnologyLevel) {
        this.maxTechnologyLevel = maxTechnologyLevel;
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
