package edu.duke.ece651.team5.shared;

/**
 * Use chain responsibility pattern to check
 * the rules that must meet for each order
 */
public abstract class OrderRuleChecker {
    private final OrderRuleChecker next;

    /**
     * Constructor to chain the rule checkers
     * @param next
     */
    public OrderRuleChecker(OrderRuleChecker next) {
        this.next = next;
    }

    /**
     * Override this method for each check rules
     * @param order the order that should be checked
     * @param player the player that issues this order
     * @param map the map
     * @return error message if it does not meet the rule, null if it does
     */
    protected abstract String checkMyRule(BasicOrder order, Player player, RISKMap map);

    // do not override this method in subclasses
    public String checkOrder(BasicOrder order, Player player, RISKMap map) {
        //if we fail our own rule: stop the order is not legal
        String message = checkMyRule(order, player, map);
        if (message != null) {
            return message;
        }
        //otherwise, ask the rest of the chain.
        if (next != null) {
            return next.checkOrder(order, player, map);
        }
        //if there are no more rules, then the order is legal
        return null;
    }



}
