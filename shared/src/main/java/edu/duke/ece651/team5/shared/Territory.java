package edu.duke.ece651.team5.shared;

import java.util.HashMap;
import java.util.Objects;
import java.io.Serializable;

public class Territory implements Serializable {
    private final String name;
    // todo: enum -> int?
    private final HashMap<Unit, Integer> units;
    private Player playerOwner;

    public Territory(String name, HashMap<Unit, Integer> units) {
        this.name = name;
        this.units = units;
        this.playerOwner = null;
    }

    public String getName() {
        return name;
    }

    public int getUnitNum(Unit unit) {
        return units.get(unit);
    }

    public void updateUnitCount(Unit type, boolean isOut, int count) {
        if (units.containsKey(type)) {
            int currentCount = units.get(type);
            if (isOut) {
                units.put(type, currentCount - count);
            } else {
                units.put(type, currentCount + count);
            }
        } else {
            throw new IllegalArgumentException("this unit type does not exist");
        }
    }

    public void addOwner(Player owner){
        playerOwner = owner;
    }

    public boolean hasOwner(){
        if (playerOwner == null){
            return false;
        }
        return true;
    }

    public Player whoOwnsit(){
        return playerOwner;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Territory territory = (Territory) o;

        if (!Objects.equals(name, territory.name)) return false;
        return Objects.equals(units, territory.units);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (units != null ? units.hashCode() : 0);
        return result;
    }
}
