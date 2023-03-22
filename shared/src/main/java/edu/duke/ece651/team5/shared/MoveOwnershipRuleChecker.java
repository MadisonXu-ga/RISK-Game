package edu.duke.ece651.team5.shared;

public class MoveOwnershipRuleChecker extends OrderRuleChecker{
    public MoveOwnershipRuleChecker(OrderRuleChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(BasicOrder order, Player player, RISKMap map) {
        String destinationName = order.getDestinationName();
        String sourceName = order.getSourceName();
        Territory source = map.getTerritoryByName(sourceName);
        Territory destination = map.getTerritoryByName(destinationName);
        if (!source.getOwner().equals(player) || !destination.getOwner().equals(player)) {
            return "You cannot move between territories that do not belong to you";
        } else {
            return null;
        }
    }
}
