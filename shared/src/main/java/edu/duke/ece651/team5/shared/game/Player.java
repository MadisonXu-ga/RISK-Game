package edu.duke.ece651.team5.shared.game;

import java.io.Serializable;
import java.util.*;

import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;

/*
 * This class handles all the actions for a player
 */


public class Player implements Serializable {
    //name of the player
    private final String name;
    //list of territories this player owned
    private final List<Territory> territories;
    //current technology level for this player
    private int currTechnologyLevel;
    //the resource player currently owned for food and tech resources
    private final Map<Resource, Integer> resourceToAmount;

    /**
     * Constructor to create a player
     * @param playerName
     */
    public Player(String playerName) {
        this.name = playerName;
        this.territories = new ArrayList<>();
        currTechnologyLevel = 0;
        resourceToAmount = new HashMap<>();
        resourceToAmount.put(new Resource(ResourceType.FOOD), 0);
        resourceToAmount.put(new Resource(ResourceType.TECHNOLOGY), 0);
    }

    /**
     * when player as defenser lose in combat, lose control of this territory
     * @param t territory that losed in the combat phase
     */
    public void loseTerritory(Territory t) {
        territories.remove(t);
    }

    /**
     * needs to consume resource when making orders, 
     * abstract into orders, should not called directly
     * @param resource
     * @param amount
     */
    public void consumeResource(Resource resource, int amount) {
        int currentCount = resourceToAmount.getOrDefault(resource, 0);
        int newCount = Math.max(currentCount - amount, 0);
        if (newCount == 0) {
            resourceToAmount.remove(resource);
        } else {
            resourceToAmount.put(resource, newCount);
        }
    }

    /**
     * get current resource amount
     * @param resource type of resource 
     * @return the number of resource currently owned by a player
     */
    public int getResourceCount(Resource resource) {
        return resourceToAmount.getOrDefault(resource, 0);
    }

    /**
     * add resource to territory at end of each turn
     * abstract into territory, should not called directly
     * @param resource type of resource 
     * @param num number of resource add to 
     */
    public void addResourceFromTerritory(Resource resource, int num) {
        resourceToAmount.put(resource,
                resourceToAmount.get(resource) + num);
    }

    /**
     * upgrade current technical level by 1
     */
    public void upgradeTechnologyLevel() {
        this.currTechnologyLevel++;
    }

    /**
     * add ownership of a territory
     * @param t target territory to add ownership
     */
    public void addTerritory(Territory t) {
        territories.add(t);
    }

    /**
     * getter method for territories
     * @return a list of territories player currently owned
     */
    public List<Territory> getTerritories() {
        return territories;
    }

    /**
     * getter method for current technology level
     * @return current technology level
     */
    public int getCurrTechnologyLevel() {
        return this.currTechnologyLevel;
    }

    /**
     * protected method for testing, should not use directly
     * @param level level to set
     * @return upgraded level
     */
    public int setCurrTechnologyLevel(int level) {
        return this.currTechnologyLevel = level;
    }

    /**
     * getter for all the resources player currently owned
     * @return map with key of resource type, value of resource amount
     */
    public Map<Resource, Integer> getResourceToAmount() {
        return resourceToAmount;
    }

    /**
     * getter for player name
     * @return
     */
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
