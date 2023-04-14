package edu.duke.ece651.team5.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.duke.ece651.team5.shared.Action;
import edu.duke.ece651.team5.shared.game.*;
import edu.duke.ece651.team5.shared.order.*;
import edu.duke.ece651.team5.shared.rulechecker.*;

public class ActionChecker {
    private OrderRuleChecker moveOrderChecker;
    private OrderRuleChecker attackOrderChecker;
    private ResearchOrderRuleChecker researchOrderRuleChecker;
    private UpgradeOrderRuleChecker upgradeOrderRuleChecker;
    private RISKMap deepCopyMap;
    private HashMap<Player, Player> playerToDeepCopyPlayer;

    public ActionChecker(RISKMap map) throws ClassNotFoundException, IOException{
        this.moveOrderChecker = new MoveOwnershipRuleChecker(
            new MovePathWithSameOwnerRuleChecker(new MoveResourceChecker(new SoldierNumberChecker(null))));
        this.attackOrderChecker = new AttackOwnershipRuleChecker(
            new AttackAdjacentRuleChecker(new AttackResourceChecker(new SoldierNumberChecker(null))));
        this.researchOrderRuleChecker = new ResearchEnoughResourceRuleChecker(
            new ResearchLevelBoundRuleChecker(null));
        this.upgradeOrderRuleChecker = new UpgradeEnoughResourceRuleChecker(
            new UpgradeLevelBoundRuleChecker(new UpgradeBackwardRuleChecker(null)));

        this.deepCopyMap = map.getDeepCopy();
        this.playerToDeepCopyPlayer = new HashMap<>();
    }

    public String checkActions(Action action) throws ClassNotFoundException, IOException {
        ArrayList<MoveOrder> moveOrders = action.getMoveOrders();
        ArrayList<AttackOrder> attackOrders = action.getAttackOrders();
        ResearchOrder researchOrder = action.getResearchOrder();
        ArrayList<UpgradeOrder> upgradeOrders = action.getUpgradeOrders();

        // check move orders valid
        String message = checkMoveValid(moveOrders, moveOrderChecker, deepCopyMap);
        if (message != null) {
            return message;
        }

        // check attack orders valid
        message = checkAttackValid(attackOrders, attackOrderChecker, deepCopyMap);
        if (message != null) {
            return message;
        }

        // check upgrade orders valid
        message = checkUpgradeValid(upgradeOrders, upgradeOrderRuleChecker, deepCopyMap);
        System.out.println("action checker, check upgrade Valid: " + message);
        if (message != null) {
            return message;
        }

        // check research order valid
        message = checkResearchValid(researchOrder, researchOrderRuleChecker);

        return message;
    }

    /**
     * Check whether move orders are valid.
     * 
     * @param moveOrders
     * @param moveOrderChecker
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public String checkMoveValid(ArrayList<MoveOrder> moveOrders, OrderRuleChecker moveOrderChecker, RISKMap map) throws ClassNotFoundException, IOException {
        String message = null;
        for (MoveOrder moveOrder : moveOrders) {
            message = moveOrderChecker.checkOrder(moveOrder, map);
            if (message != null) {
                return message;
            }
            // make deep copy of player
            Player targetRealPlayer = moveOrder.getPlayer();
            if(!playerToDeepCopyPlayer.containsKey(targetRealPlayer)){
                playerToDeepCopyPlayer.put(targetRealPlayer, targetRealPlayer.getDeepCopy());
            }
            moveOrder.execute(map, playerToDeepCopyPlayer.get(targetRealPlayer));
        }
        return message;
    }

    /**
     * Check whether attack orders are valid.
     * 
     * @param attackOrders
     * @param attOrderRuleChecker
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public String checkAttackValid(ArrayList<AttackOrder> attackOrders, OrderRuleChecker attackOrderRuleChecker, RISKMap map) throws ClassNotFoundException, IOException {
        String message = null;
        
        for (AttackOrder attackOrder : attackOrders) {
            message = attackOrderRuleChecker.checkOrder(attackOrder, map);
            if (message != null) {
                return message;
            }
            // TODO: need deep copy for player
            Player targetRealPlayer = attackOrder.getPlayer();
            if(!playerToDeepCopyPlayer.containsKey(targetRealPlayer)){
                playerToDeepCopyPlayer.put(targetRealPlayer, targetRealPlayer.getDeepCopy());
            }
            attackOrder.execute(map, playerToDeepCopyPlayer.get(targetRealPlayer));
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
            UpgradeOrderRuleChecker upgradeOrderRuleChecker, RISKMap map) throws ClassNotFoundException, IOException {
        String message = null;
        for (UpgradeOrder upgradeOrder : upgradeOrders) {
            message = upgradeOrderRuleChecker.checkOrder(upgradeOrder);
            System.out.println("Upgrade order checker message: " + message);
            if (message != null) {
                return message;
            }
            // TODO: need deep copy for player
            Player targetRealPlayer = upgradeOrder.getPlayer();
            if(!playerToDeepCopyPlayer.containsKey(targetRealPlayer)){
                playerToDeepCopyPlayer.put(targetRealPlayer, targetRealPlayer.getDeepCopy());
            }
            upgradeOrder.execute(map, playerToDeepCopyPlayer.get(targetRealPlayer));
        }
        return message;
    }
}
