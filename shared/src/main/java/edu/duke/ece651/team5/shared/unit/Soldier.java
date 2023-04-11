package edu.duke.ece651.team5.shared.unit;

/**
 * Soldier class handle the level of soldier
 */
public class Soldier {

    private SoldierLevel level;

    /**
     * Constructor
     * @param level level for this soldier
     */
    public Soldier(SoldierLevel level) {
        this.level = level;
    }

   /**
    * getter to get current soldier level
    * @return
    */
    public SoldierLevel getLevel() {
        return this.level;
    }

    /**
     * upgrade current soldier to target level
     * @param targetLevel target level
     */
    public void upgradeLevel(SoldierLevel targetLevel) {
        this.level = targetLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Soldier soldier = (Soldier) o;

        return level == soldier.level;
    }

    @Override
    public int hashCode() {
        // return level != null ? level.hashCode() : 0;
        return level.hashCode();
    }
    
    @Override
    public String toString(){
        return level.name();
    }


}
