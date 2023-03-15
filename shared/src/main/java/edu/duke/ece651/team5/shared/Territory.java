package edu.duke.ece651.team5.shared;

import java.util.HashMap;
import java.util.Objects;

public class Territory {
    private final String name;
    // todo: string -> int?
    private final HashMap<Unit, Integer> units;

    // todo: delete this constructor later
    public Territory(String name) {
        this.name = name;
        this.units = new HashMap<>();
    }

    public Territory(String name, HashMap<Unit, Integer> units) {
        this.name = name;
        this.units = units;
    }

    public String getName() {
        return name;
    }

    public int getUnitNum(Unit unit) {
        return units.get(unit);
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
