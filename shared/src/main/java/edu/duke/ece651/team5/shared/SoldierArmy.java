package edu.duke.ece651.team5.shared;

import java.util.ArrayList;
import java.util.List;

import static edu.duke.ece651.team5.shared.Constants.DEFAULT_INIT_SOLDIER_NUM;

public class SoldierArmy {
    private List<Soldier> soldiers;

    public SoldierArmy() {
        soldiers = new ArrayList<>();
        addSoldiers(SoldierType.INFANTRY, 1, DEFAULT_INIT_SOLDIER_NUM);
    }

    public void addSoldiers(SoldierType type, int level, int numSoldiers) {
        // check if there are any soldiers of the given type and level already
        for (Soldier soldier : soldiers) {
            if (soldier.getType() == type && soldier.getLevel() == level) {
                soldier.setSoldierNum(soldier.getSoldierNum() + numSoldiers);
                return;
            }
        }

        // if no soldiers of the given type and level were found, add a new one
        soldiers.add(new Soldier(type, level, numSoldiers));
    }

    // similar methods for removing soldiers, getting the number of soldiers of a given type and level, etc.


    public List<Soldier> getSoldiers() {
        return soldiers;
    }

    public void setSoldiers(List<Soldier> soldiers) {
        this.soldiers = soldiers;
    }
}

