package edu.duke.ece651.team5.shared.unit;

import java.util.*;

import static edu.duke.ece651.team5.shared.constant.Constants.DEFAULT_INIT_SOLDIER_NUM;

public class SoldierArmy {
    private Map<Soldier, Integer> soldiers = new HashMap<>();
    public SoldierArmy() {
        addSoldier(new Soldier(SoldierLevel.INFANTRY), DEFAULT_INIT_SOLDIER_NUM);
        // addSoldier(new Soldier(SoldierType.ARTILLERY, 1), DEFAULT_INIT_SOLDIER_NUM);
    }

    public SoldierArmy(Map<Soldier, Integer> soldiers) {
        this.soldiers = soldiers;
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

    public void upgradeSoldier(Soldier soldier, int count, SoldierLevel targetLevel) {
        removeSoldier(soldier, count);
        addSoldier(new Soldier(targetLevel), count);
    }

    public int getTotalCountSolider() {
        return soldiers.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void setSoldiers(Map<Soldier, Integer> soldiers){
        this.soldiers = soldiers;
    }

    public int getSoldierCount(Soldier soldier) {
        return soldiers.getOrDefault(soldier, 0);
    }

    public Map<Soldier, Integer> getAllSoldiers() {
        return soldiers;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        SoldierArmy soldierArmy = (SoldierArmy) o;
        return Objects.equals(soldiers, soldierArmy.soldiers);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(soldiers);
    }

}

