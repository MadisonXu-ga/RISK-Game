package edu.duke.ece651.team5.shared.game;

import java.util.Objects;

import edu.duke.ece651.team5.shared.Constants;
import edu.duke.ece651.team5.shared.allResource.Resource;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;



public class Territory {
    private int id;
    private String name;
    private Player owner;
    private SoldierArmy soldierArmy;

    private int foodResource;
    private int techResource;



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

    public void setResources(int food, int tech){
        foodResource = food;
        techResource = tech;
    }

    public void produceResource(Resource resource) {
        owner.addResourceFromTerritory(resource, Constants.PRODUCE_FOOD_RESOURCE_PER_TURN);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getOwner() {
        return this.owner;
    }

    public void setOwner(Player newOwner) {
        this.owner = newOwner;
    }

    public SoldierArmy getSoldierArmy() {
        return this.soldierArmy;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Territory)) {
            return false;
        }
        Territory territory = (Territory) o;
        return id == territory.id && Objects.equals(name, territory.name) && Objects.equals(owner, territory.owner) && Objects.equals(soldierArmy, territory.soldierArmy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, owner, soldierArmy);
    }





}
