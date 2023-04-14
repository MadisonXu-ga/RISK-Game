package edu.duke.ece651.team5.shared.game;

import java.io.Serializable;
import java.util.Objects;

import edu.duke.ece651.team5.shared.constant.Constants;
import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;

/**
 * This class handle all the functionality of Territory
 */
public class Territory implements Serializable {
    //id to represent territory in connections
    private int id;
    //territory name
    private String name;
    //owner of territory
    private Player owner;
    //store all the soldiers
    private SoldierArmy soldierArmy;

    /**
     * only used for test case
     * @param id assigned id
     * @param name territory name
     * @param owner owner of 
     * @param soldierArmy
     */
    public Territory(int id, String name, Player owner, SoldierArmy soldierArmy) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.soldierArmy = soldierArmy;
    }

    /**
     * designed for test case
     * @param id
     * @param name
     * @param owner
     */
    public Territory(int id, String name, Player owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.soldierArmy = new SoldierArmy();
    }

    /**
     * constructor 
     * @param id assigned id
     * @param name territory name
     */
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
        System.out.println("territory produce resource hash code: " + resource.hashCode());
        owner.addResourceFromTerritory(resource, amount);
    }


    /**
     * getter for territory id
     * @return
     */
    public int getId() {
        return this.id;
    }

    /**
     * setter for territory id
     * @param id
     */
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

    public void setSoldierArmy(SoldierArmy soldierArmy){
        this.soldierArmy = soldierArmy;
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
