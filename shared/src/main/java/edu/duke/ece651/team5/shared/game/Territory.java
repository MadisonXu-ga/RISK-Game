package edu.duke.ece651.team5.shared.game;

import java.util.Objects;

import edu.duke.ece651.team5.shared.constant.Constants;
import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;

public class Territory {
    private int id;
    private String name;
    private Player owner;
    private SoldierArmy soldierArmy;

    public Territory(int id, String name, Player owner, SoldierArmy soldierArmy) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.soldierArmy = soldierArmy;
    }

    public Territory(int id, String name, Player owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.soldierArmy = new SoldierArmy();
    }

    public Territory(int id, String name) {
        this.id = id;
        this.name = name;
        this.soldierArmy = new SoldierArmy();
    }

    /**
     * produce resource every turn
     * @param resource the resource object with a specific type
     */
    public void produceResource(Resource resource) {
        int amount = (resource.getType().equals(ResourceType.FOOD)) ? Constants.PRODUCE_FOOD_RESOURCE_PER_TURN : Constants.PRODUCE_TECH_RESOURCE_PER_TURN;
        owner.addResourceFromTerritory(resource, amount);
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the territory name
     * @return the name of the territory
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for territory name
     * @param name territory name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for territory owner
     * @return territory owner
     */
    public Player getOwner() {
        return this.owner;
    }

    /**
     * Setter for territory owner
     * @return territory owner
     */
    public void setOwner(Player newOwner) {
        this.owner = newOwner;
    }

    /**
     * Getter for territory soldier army
     * @return territory soldier army
     */
    public SoldierArmy getSoldierArmy() {
        return this.soldierArmy;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        Territory territory = (Territory) o;
        return id == territory.id && Objects.equals(name, territory.name) && Objects.equals(owner, territory.owner) && Objects.equals(soldierArmy, territory.soldierArmy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, owner, soldierArmy);
    }

}
