package edu.duke.ece651.team5.shared;

public class MovePathWithSameOwnerRuleChecker extends OrderRuleChecker{
    public MovePathWithSameOwnerRuleChecker(OrderRuleChecker next) {
        super(next);
    }
    /**
     * Override this method for each check rules
     *
     * @param order  the order that should be checked
     * @param player the player that issues this order
     * @param map    the map
     * @return error message if it does not meet the rule, null if it does
     */
    @Override
    protected String checkMyRule(BasicOrder order, Player player, RISKMap map) {
        String destinationName = order.getDestinationName();
        String sourceName = order.getSourceName();
        Territory source = map.getTerritoryByName(sourceName);
        Territory destination = map.getTerritoryByName(destinationName);
        if (!map.hasPathWithSameOwner(source, destination)) {
            return "There is no such a path from " +
                    sourceName + " to " + destinationName +
                    " owned by " + player.getName();
        } else {
            return null;
        }
    }
}
