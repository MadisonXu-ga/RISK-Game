package edu.duke.ece651.team5.shared.rulechecker;

import edu.duke.ece651.team5.shared.order.UpgradeOrder;

public abstract class UpgradeOrderRuleChecker {
    private final UpgradeOrderRuleChecker next;

    /**
     * Constructor to chain the rule checkers
     * @param next the next order to be checked
     */
    public UpgradeOrderRuleChecker(UpgradeOrderRuleChecker next) {
        this.next = next;
    }

    /**
     * Override this method for each check rules
     * @param upgradeOrder the order that should be checked
     * @return error message if it does not meet the rule, null if it does
     */
    protected abstract String checkMyRule(UpgradeOrder upgradeOrder);

    // do not override this method in subclasses
    public String checkOrder(UpgradeOrder order) {
        //if we fail our own rule: stop the order is not legal
        String message = checkMyRule(order);
        if (message != null) {
            return message;
        }
        //otherwise, ask the rest of the chain.
        if (next != null) {
            return next.checkOrder(order);
        }
        //if there are no more rules, then the order is legal
        return null;
    }
}
