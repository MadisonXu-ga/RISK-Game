package edu.duke.ece651.team5.shared.game;

import java.util.*;

import edu.duke.ece651.team5.shared.resource.Resource;


public class Player {
    private final String name;
    private final List<Territory> territories;
    private int currTechnologyLevel;
    private final Map<Resource, Integer> resourceToAmount;

    public Player(String playerName) {
        this.name = playerName;
        this.territories = new ArrayList<>();
        currTechnologyLevel = 0;
        resourceToAmount = new HashMap<>();
    }

    public void loseTerritory(Territory t) {
        territories.remove(t);
    }

    public void consumeResource(Resource resource, int amount) {
        int currentCount = resourceToAmount.getOrDefault(resource, 0);
        int newCount = Math.max(currentCount - amount, 0);
        if (newCount == 0) {
            resourceToAmount.remove(resource);
        } else {
            resourceToAmount.put(resource, newCount);
        }
    }

    public int getResourceCount(Resource resource) {
        return resourceToAmount.getOrDefault(resource, 0);
    }

    public void addResourceFromTerritory(Resource resource, int num) {
        resourceToAmount.put(resource,
                resourceToAmount.getOrDefault(resource, 0) + num);
    }

    public void upgradeTechnologyLevel() {
        this.currTechnologyLevel++;
    }

    public void addTerritory(Territory t) {
        territories.add(t);
    }

    public List<Territory> getTerritories() {
        return territories;
    }

    public int getCurrTechnologyLevel() {
        return this.currTechnologyLevel;
    }

    public Map<Resource, Integer> getResourceToAmount() {
        return resourceToAmount;
    }

    public String getName() {
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
