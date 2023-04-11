package edu.duke.ece651.team5.shared.rulechecker;


import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.order.BasicOrder;
import edu.duke.ece651.team5.shared.unit.*;

public class SoldierNumberChecker extends OrderRuleChecker{

    /**
     * Constructor to chain the rule checkers
     * @param next
     */
    public SoldierNumberChecker(OrderRuleChecker next) {
        super(next);
    }

    /**
     *  Check if the target territory has enough soldier number
     * @param order order
     * @param map the map
     * @return error message if it does not meet, null if it does
     */
    @Override
    protected String checkMyRule(BasicOrder order, RISKMap map) {
        SoldierArmy targetArmy = order.getSoldierToNumber();
        String sourceName = order.getSourceName();
        Territory source = map.getTerritoryByName(sourceName);
        SoldierArmy currentArmy = source.getSoldierArmy();
        for(Soldier soldier: targetArmy.getAllSoldiers().keySet()){
            if (currentArmy.getSoldierCount(soldier) < targetArmy.getSoldierCount(soldier)) {
                return "There are only " + currentArmy.getSoldierCount(soldier) + " units in " + sourceName + ", but you entered " + targetArmy.getSoldierCount(soldier);
            }
        }
        
        return null;
    }
    
}
