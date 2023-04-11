package edu.duke.ece651.team5.shared.rulechecker;

import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.order.BasicOrder;

/**
 * Use chain responsibility pattern to check
 * the rules that must meet for each order
 */
public abstract class OrderRuleChecker {
    private final OrderRuleChecker next;

    /**
     * Constructor to chain the rule checkers
     * @param next next rule checker to be checked
     */
    public OrderRuleChecker(OrderRuleChecker next) {
        this.next = next;
    }

    /**
     * Override this method for each check rules
     * @param order the order that should be checked
     * @param map the map
     * @return error message if it does not meet the rule, null if it does
     */
    protected abstract String checkMyRule(BasicOrder order, RISKMap map);

    // do not override this method in subclasses
    public String checkOrder(BasicOrder order, RISKMap map) {
        //if we fail our own rule: stop the order is not legal
        String message = checkMyRule(order, map);
        if (message != null) {
            return message;
        }
        //otherwise, ask the rest of the chain.
        if (next != null) {
            return next.checkOrder(order, map);
        }
        //if there are no more rules, then the order is legal
        return null;
    }



}
