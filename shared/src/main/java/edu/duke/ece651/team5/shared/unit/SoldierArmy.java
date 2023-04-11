package edu.duke.ece651.team5.shared.unit;

import java.util.*;


/**
 * This class handle all the soldiers in an army setting
 */
public class SoldierArmy {
    //store all the soldires with key of soldier in different level, and value of corresponding soldier number
    private Map<Soldier, Integer> soldiers;

    /**
     * default contructor: create a map of soldier with all soldier levels with 0 number for each type
     */
    public SoldierArmy() {
        soldiers = initSoldierArmy();
        //addSoldier(new Soldier(SoldierLevel.INFANTRY), DEFAULT_INIT_SOLDIER_NUM);
        //addSoldier(new Soldier(SoldierType.ARTILLERY, 1), DEFAULT_INIT_SOLDIER_NUM);
    }



    /**
     * constructor with target level and number of soldiers
     * @param soldiers
     */
    public SoldierArmy(Map<Soldier, Integer> soldiers) {
        this.soldiers = soldiers;
    }

    /**
     * add soldier and corresponding number to soldier army
     * @param soldier target soldier
     * @param count number of this soldier
     */
    public void addSoldier(Soldier soldier, int count) {
        soldiers.put(soldier, soldiers.get(soldier) + count);
    }

    /**
     * remove soldier and corresponding number from soldier army
     * @param soldier target soldier
     * @param count number to remove
     */
    public void removeSoldier(Soldier soldier, int removeNum) {
        int currentNum = soldiers.get(soldier);
        if(currentNum < removeNum){
            throw new IllegalArgumentException("You only have " + currentNum + " for soldier " + soldier.getLevel() + ", cannot remove " + removeNum);
        }
        int newNum = currentNum - removeNum;
        soldiers.put(soldier, newNum);
    }

    /**
     * upgrade soldier with related number to target level
     * @param soldier soldier to upgrade
     * @param count number of soldier want to upgrade
     * @param targetLevel target level for these soldiers to upgrade
     */
    public void upgradeSoldier(Soldier soldier, int count, SoldierLevel targetLevel) {
        removeSoldier(soldier, count);
        addSoldier(new Soldier(targetLevel), count);
    }


    /**
     * get all the number of soldier in the soldierArmy
     * @return number of all soldiers
     */
    public int getTotalCountSolider() {
        return soldiers.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * setter to set soldierArmy
     * @param soldiers map of soldier to reset
     */
    public void setSoldiers(Map<Soldier, Integer> soldiers){
        this.soldiers = soldiers;
    }

    /**
     * get number of soldier with related soldier
     * @param soldier type of soldier want to get
     * @return number of soldier for this related type
     */
    public int getSoldierCount(Soldier soldier) {
        return soldiers.get(soldier);
    }

    /**
     * getter for all soldierArmy
     * @return map of soldier
     */
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


    /**
     * helper metohod to init soldier army
     * @return soldierArmy map to get ready for initialize
     */
    private Map<Soldier, Integer> initSoldierArmy(){
        Map<Soldier, Integer> res = new HashMap<>();
        res.put(new Soldier(SoldierLevel.AIRBORNE), 0);
        res.put(new Soldier(SoldierLevel.ARMOR), 0);
        res.put(new Soldier(SoldierLevel.ARTILLERY), 0);
        res.put(new Soldier(SoldierLevel.AVIATION), 0);
        res.put(new Soldier(SoldierLevel.CAVALRY), 0);
        res.put(new Soldier(SoldierLevel.INFANTRY), 0);
        res.put(new Soldier(SoldierLevel.INTELLIGENCE), 0);
        return res;
    }
}

