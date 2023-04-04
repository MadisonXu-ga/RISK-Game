package edu.duke.ece651.team5.shared;

public class Soldier {
    private SoldierLevel level;

    public Soldier(SoldierLevel level) {
        this.level = level;
    }

    public SoldierLevel getLevel() {
        return this.level;
    }

    public void upgradeLevel(SoldierLevel targetLevel) {
        // todo: rule checker
        assert (this.level.ordinal() < targetLevel.ordinal());
        this.level = targetLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Soldier soldier = (Soldier) o;

        return level == soldier.level;
    }

    @Override
    public int hashCode() {
        return level != null ? level.hashCode() : 0;
    }

}
