package edu.duke.ece651.team5.shared;

public class AdjacentRuleChecker extends OrderRuleChecker {
    public AdjacentRuleChecker(OrderRuleChecker next) {
        super(next);
    }


    /**
     * Check if the order meets the rule that src and dest must be adjacent
     * @param order attack order
     * @param player the player who issues this order
     * @param map the map
     * @return error message if it does not meet, null if it does
     */
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
