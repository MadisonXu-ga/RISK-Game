package edu.duke.ece651.team5.shared;

public class UnitNumberRuleChecker extends OrderRuleChecker{
    public UnitNumberRuleChecker(OrderRuleChecker next) {
        super(next);
    }

    /**
     * Check if the order meets the rule that the number of unit
     * that is going to be updated from the source territory
     * cannot be larger than it currently has
     * @param order the order that should be checked
     * @param player the player that issues this order
     * @param map the map
     * @return error message if it does not meet, null if it does
     */
    @Override
    protected String checkMyRule(BasicOrder order, Player player, RISKMap map) {
        int number = order.getNumber();
        String sourceName = order.getSourceName();
        Territory source = map.getTerritoryByName(sourceName);
        Unit type = order.getType();
        int unitNum = source.getUnitNum(type);
        if (number > unitNum) {
            return "There are only " + unitNum + " units in " + sourceName + ", but you entered " + number;
        } else {
            return null;
        }
    }
}
