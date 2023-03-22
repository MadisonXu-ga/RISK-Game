package edu.duke.ece651.team5.shared;

public class UnitNumberRuleChecker extends OrderRuleChecker{
    public UnitNumberRuleChecker(OrderRuleChecker next) {
        super(next);
    }

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
