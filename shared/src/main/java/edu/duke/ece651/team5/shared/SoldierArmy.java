package edu.duke.ece651.team5.shared;

import java.util.*;

import static edu.duke.ece651.team5.shared.Constants.DEFAULT_INIT_SOLDIER_NUM;

public class SoldierArmy {
    private Map<Soldier, Integer> soldiers = new HashMap<>();

    public SoldierArmy() {
        addSoldier(new Soldier(SoldierType.INFANTRY, 1), DEFAULT_INIT_SOLDIER_NUM);
        addSoldier(new Soldier(SoldierType.ARTILLERY, 1), DEFAULT_INIT_SOLDIER_NUM);
    }

    public void addSoldier(Soldier soldier, int count) {
        soldiers.put(soldier, soldiers.getOrDefault(soldier, 0) + count);
    }

    public void removeSoldier(Soldier soldier, int count) {
        int currentCount = soldiers.getOrDefault(soldier, 0);
        int newCount = Math.max(currentCount - count, 0);
        if (newCount == 0) {
            soldiers.remove(soldier);
        } else {
            soldiers.put(soldier, newCount);
        }
    }

    public int getSoldierCount(Soldier soldier) {
        return soldiers.getOrDefault(soldier, 0);
    }

    public Map<Soldier, Integer> getAllSoldiers() {
        return Collections.unmodifiableMap(soldiers);
    }
}

