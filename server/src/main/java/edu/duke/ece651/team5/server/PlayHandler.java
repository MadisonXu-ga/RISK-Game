package edu.duke.ece651.team5.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import edu.duke.ece651.team5.shared.Action;
import edu.duke.ece651.team5.shared.AdjacentRuleChecker;
import edu.duke.ece651.team5.shared.AttackOrder;
import edu.duke.ece651.team5.shared.AttackOwnershipRuleChecker;
import edu.duke.ece651.team5.shared.MoveOrder;
import edu.duke.ece651.team5.shared.MoveOwnershipRuleChecker;
import edu.duke.ece651.team5.shared.MovePathWithSameOwnerRuleChecker;
import edu.duke.ece651.team5.shared.OrderRuleChecker;
import edu.duke.ece651.team5.shared.UnitNumberRuleChecker;

public class PlayHandler extends ConnectionHandler {
    private GameController gameController;
    private Action action;
    private Boolean playerConnectionStatus;
    private String playerName;

    public PlayHandler(ObjectOutputStream oos, ObjectInputStream ois, GameController gameController,
            Boolean playerConnectionStatus, String playerName) {
        super(oos, ois);
        this.gameController = gameController;
        this.playerConnectionStatus = playerConnectionStatus;
        this.playerName = playerName;
    }

    /*
     * Call this to run communication in parallel.
     */
    @Override
    public void run() {
        try {
            if (playerConnectionStatus != false) {
                sendObject(gameController.getRiskMap());
            }
            if (playerConnectionStatus != true) {
                return;
            }
            // only normal connections send actions
            boolean isValid = false;
            do {
                this.action = (Action) recvObject();
                isValid = checkActions(action);
                sendObject(isValid);
            } while (!isValid);
            

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
     * Check whether player's actions are valid.
     */
    private boolean checkActions(Action action) {
        ArrayList<MoveOrder> mos = action.getMoveOrders();
        ArrayList<AttackOrder> aos = action.getAttackOrders();

        OrderRuleChecker moveActionChecker = new MoveOwnershipRuleChecker(
                new UnitNumberRuleChecker(new MovePathWithSameOwnerRuleChecker(null)));

        OrderRuleChecker attackActionChecker = new AttackOwnershipRuleChecker(
                new UnitNumberRuleChecker(new AdjacentRuleChecker(null)));

        UnitValidRuleChecker actionUnitChecker = new UnitValidRuleChecker();

        // check move actions valid
        boolean valid = checkMoveValid(mos, moveActionChecker, actionUnitChecker);
        if (!valid) {
            return valid;
        }

        // check attack actions valid
        valid = checkAttackValid(aos, attackActionChecker, actionUnitChecker);

        return valid;
    }

    /*
     * Check whether move orders are valid.
     */
    private boolean checkMoveValid(ArrayList<MoveOrder> mos, OrderRuleChecker moveActionChecker,
            UnitValidRuleChecker moveActionUnitChecker) {
        for (MoveOrder mo : mos) {
            String message = moveActionChecker.checkOrder(mo,
                    this.gameController.getRiskMap().getPlayerByName(playerName), this.gameController.getRiskMap());
            if (message != null) {
                return false;
            }
        }
        return moveActionUnitChecker.checkMoveOrderUnitValid(this.gameController.getRiskMap(), mos);
    }

    /*
     * Check whether attack orders are valid.
     */
    private boolean checkAttackValid(ArrayList<AttackOrder> aos, OrderRuleChecker attackActionChecker,
            UnitValidRuleChecker attackActionUnitChecker) {
        for (AttackOrder ao : aos) {
            String message = attackActionChecker.checkOrder(ao,
                    this.gameController.getRiskMap().getPlayerByName(playerName), this.gameController.getRiskMap());
            if (message != null) {
                return false;
            }
        }
        return attackActionUnitChecker.checkAttackOrderUnitValid(this.gameController.getRiskMap(), aos);
    }

    /*
     * Get one player's valid move orders in this turn.
     */
    public ArrayList<MoveOrder> getPlayerMoveOrders() {
        return action.getMoveOrders();
    }

    /*
     * Get one player's valid attack orders in this turn.
     */
    public ArrayList<AttackOrder> getPlayerAttackOrders() {
        return action.getAttackOrders();
    }
}