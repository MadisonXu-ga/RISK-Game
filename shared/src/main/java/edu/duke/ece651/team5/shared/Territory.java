package edu.duke.ece651.team5.shared;

import java.util.HashMap;
import java.util.Objects;
import java.io.Serializable;

public class Territory implements Serializable {
    private final String name;
    private final HashMap<Unit, Integer> units;
    private Player owner;

    public Territory(String name) {
        this.name = name;
        HashMap<Unit, Integer> initUnits = new HashMap<>();
        initUnits.put(UnitType.SOLDIER, 0);
        this.units = initUnits;
    }

    public Territory(String name, HashMap<Unit, Integer> units) {
        this.name = name;
        this.units = units;
        this.owner = null;
    }

    /**
     * Get the territory name
     * @return the name of the territory
     */
    public String getName() {
        return name;
    }

    /**
     * Get the number of a certain unit type
     * @param unit unit type
     * @return the number of this type of unit placed in this territory
     */
    public int getUnitNum(Unit unit) {
        return units.get(unit);
    }

    /**
     * Update the unit count
     * @param type the type of unit
     * @param isOut update direction, true for subtract and false for add
     * @param count the number of count that is going to be updated
     */
    public void updateUnitCount(Unit type, boolean isOut, int count) {
        //if (units.containsKey(type)) {
            int currentCount = units.get(type);
            if (isOut) {
                units.put(type, currentCount - count);
            } else {
                units.put(type, currentCount + count);
            }
        //} else {
            //throw new IllegalArgumentException("this unit type does not exist");
        //}
    }

    /**
     * Set the owner of territory
     * @param owner the player that should own this territory
     */
    public void setOwner(Player owner){
        this.owner = owner;
    }

    /**
     * To tell if the territory has an owner
     * @return true if it does, otherwise false
     */
    public boolean hasOwner(){
        return owner != null;
    }

    /**
     * Get the owner of this territory
     * @return the owner
     */
    public Player getOwner(){
        return owner;
    }

    /**
     * to tell if two territories equals (on name)
     * @param o another object
     * @return true if the names of two territories equals
     *         false if names do not equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
    
        Territory territory = (Territory) o;
    
        return Objects.equals(name, territory.name);
    }

    /**
     * hashcode for territory compute with name
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Territory{" + "name='" + name + '\'' + "hashcode=" + name.hashCode() + '}';
    }

    
}
