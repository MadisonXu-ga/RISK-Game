package edu.duke.ece651.team5.shared.rulechecker;


import edu.duke.ece651.team5.shared.order.ResearchOrder;

public abstract class ResearchOrderRuleChecker{

    private final ResearchOrderRuleChecker next;

    /**
     * Constructor to chain the rule checkers
     * @param next
     */
    public ResearchOrderRuleChecker(ResearchOrderRuleChecker next) {
        this.next = next;
    }

    /**
     * Override this method for each check rules
     * @param order the order that should be checked
     * @return error message if it does not meet the rule, null if it does
     */
    protected abstract String checkMyRule(ResearchOrder order);

    // do not override this method in subclasses
    public String checkOrder(ResearchOrder order) {
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
