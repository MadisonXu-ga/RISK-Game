package edu.duke.ece651.team5.shared;

import java.util.Objects;

public class Soldier {
    private SoldierType type;
    private int level;


    public Soldier(SoldierType type, int level) {
        this.type = type;
        this.level = level;
    }


    public SoldierType getType() {
        return this.type;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Soldier)) {
            return false;
        }
        Soldier soldier = (Soldier) o;
        return Objects.equals(type, soldier.type) && level == soldier.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, level);
    }

    @Override
    public String toString() {
        return "{" +
            " type='" + getType() + "'" +
            ", level='" + getLevel() + "'" +
            "}";
    }





}
