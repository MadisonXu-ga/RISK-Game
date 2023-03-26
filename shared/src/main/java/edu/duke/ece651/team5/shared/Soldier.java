package edu.duke.ece651.team5.shared;

public class Soldier {
    int level;
    SoldierType type;
    int soldierNum;

    public Soldier(SoldierType soldierType, int level, int number) {
        this.level = level;
        this.type = soldierType;
        this.soldierNum = number;
    }

    public int getLevel() {
        return level;
    }

    public SoldierType getType() {
        return type;
    }

    public int getSoldierNum() {
        return soldierNum;
    }

    public void setSoldierNum(int soldierNum) {
        this.soldierNum = soldierNum;
    }
}
