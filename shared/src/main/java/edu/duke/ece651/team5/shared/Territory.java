package edu.duke.ece651.team5.shared;

import java.util.Objects;

public class Territory {
    private final String name;

    public Territory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Territory territory = (Territory) o;

        return Objects.equals(name, territory.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
