package edu.duke.ece651.team5.server;

import java.util.ArrayList;

import edu.duke.ece651.team5.shared.Action;
import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.order.*;
import edu.duke.ece651.team5.shared.rulechecker.*;

public class ActionChecker {
    private OrderRuleChecker moveOrderChecker;
    private OrderRuleChecker attackOrderChecker;
    private ResearchOrderRuleChecker researchOrderRuleChecker;
    private UpgradeOrderRuleChecker upgradeOrderRuleChecker;

    public ActionChecker(){
        this.moveOrderChecker = new MoveOwnershipRuleChecker(
            new MovePathWithSameOwnerRuleChecker(new MoveResourceChecker(null)));
        this.attackOrderChecker = new AttackOwnershipRuleChecker(
            new AttackAdjacentRuleChecker(new AttackResourceChecker(null)));
        this.researchOrderRuleChecker = new ResearchEnoughResourceRuleChecker(
            new ResearchLevelBoundRuleChecker(null));
        this.upgradeOrderRuleChecker = new UpgradeEnoughResourceRuleChecker(
            new UpgradeLevelBoundRuleChecker(new UpgradeBackwardRuleChecker(null)));
    }

    public String checkActions(Action action, RISKMap map) {
        ArrayList<MoveOrder> moveOrders = action.getMoveOrders();
        ArrayList<AttackOrder> attackOrders = action.getAttackOrders();
        ResearchOrder researchOrder = action.getResearchOrder();
        ArrayList<UpgradeOrder> upgradeOrders = action.getUpgradeOrders();

        // check move orders valid
        String message = checkMoveValid(moveOrders, moveOrderChecker, map);
        if (message != null) {
            return message;
        }

        // check attack orders valid
        message = checkAttackValid(attackOrders, attackOrderChecker, map);
        if (message != null) {
            return message;
        }

        // check research order valid
        message = checkResearchValid(researchOrder, researchOrderRuleChecker);
        if (message != null) {
            return message;
        }

        // check upgrade orders valid
        message = checkUpgradeValid(upgradeOrders, upgradeOrderRuleChecker);

        return message;
    }

    /**
     * Check whether move orders are valid.
     * 
     * @param moveOrders
     * @param moveOrderChecker
     * @return
     */
    public String checkMoveValid(ArrayList<MoveOrder> moveOrders, OrderRuleChecker moveOrderChecker, RISKMap map) {
        String message = null;
        for (MoveOrder moveOrder : moveOrders) {
            message = moveOrderChecker.checkOrder(moveOrder, map);
            if (message != null) {
                return message;
            }
        }
        return message;
    }

    /**
     * Check whether attack orders are valid.
     * 
     * @param attackOrders
     * @param attOrderRuleChecker
     * @return
     */
    public String checkAttackValid(ArrayList<AttackOrder> attackOrders, OrderRuleChecker attackOrderRuleChecker, RISKMap map) {
        String message = null;
        for (AttackOrder attackOrder : attackOrders) {
            message = attackOrderRuleChecker.checkOrder(attackOrder, map);
            if (message != null) {
                return message;
            }
        }
        return message;
    }

    public String checkResearchValid(ResearchOrder researchOrder,
            ResearchOrderRuleChecker researchOrderRuleChecker) {
        String message = null;
        if (researchOrder != null) {
            message = researchOrderRuleChecker.checkOrder(researchOrder);
        }
        return message;
    }

    public String checkUpgradeValid(ArrayList<UpgradeOrder> upgradeOrders,
            UpgradeOrderRuleChecker upgradeOrderRuleChecker) {
        String message = null;
        for (UpgradeOrder upgradeOrder : upgradeOrders) {
            message = upgradeOrderRuleChecker.checkOrder(upgradeOrder);
            if (message != null) {
                return message;
            }
        }
        return message;
    }
}
