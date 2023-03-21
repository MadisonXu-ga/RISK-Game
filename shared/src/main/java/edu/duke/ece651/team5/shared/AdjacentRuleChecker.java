package edu.duke.ece651.team5.shared;

public class AdjacentRuleChecker extends OrderRuleChecker {
    public AdjacentRuleChecker(OrderRuleChecker next) {
        super(next);
    }


    @Override
    protected String checkMyRule(BasicOrder order, Player player, RISKMap map) {
        String sourceName = order.getSourceName();
        String destinationName = order.getDestinationName();
        Territory source = map.getTerritoryByName(sourceName);
        Territory destination = map.getTerritoryByName(destinationName);
        if (map.isAdjacent(source, destination)) {
            return null;
        } else {
            return sourceName + " and " + destinationName + " are not adjacent";
        }
    }
}
