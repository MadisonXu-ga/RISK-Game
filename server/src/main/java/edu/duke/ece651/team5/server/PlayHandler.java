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
            if (playerConnectionStatus == null || playerConnectionStatus == true) {
                sendObject(gameController.getRiskMap());
                System.out.println("Successfully sent map to player " + playerName);
            }
            if (playerConnectionStatus == null || playerConnectionStatus == false) {
                return;
            }
            // only normal connections send actions
            System.out.println("Start to receive actions from player " + playerName);
            boolean isValid = false;
            String message = null;
            do {
                this.action = (Action) recvObject();
                message = checkActions(action);
                if (message == null) {
                    isValid = true;
                    message = "Correct";
                }
                sendObject(isValid);
                sendObject(message);
            } while (!isValid);

            System.out.println("Successfully received actions from player " + playerName);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
     * Check whether player's actions are valid.
     */
    private String checkActions(Action action) {
        ArrayList<MoveOrder> mos = action.getMoveOrders();
        ArrayList<AttackOrder> aos = action.getAttackOrders();

        OrderRuleChecker moveActionChecker = new MoveOwnershipRuleChecker(
                new UnitNumberRuleChecker(new MovePathWithSameOwnerRuleChecker(null)));

        OrderRuleChecker attackActionChecker = new AttackOwnershipRuleChecker(
                new UnitNumberRuleChecker(new AdjacentRuleChecker(null)));

        UnitValidRuleChecker actionUnitChecker = new UnitValidRuleChecker();

        // check move actions valid
        String message = checkMoveValid(mos, moveActionChecker, actionUnitChecker);
        if (message != null) {
            return message;
        }

        // check attack actions valid
        message = checkAttackValid(aos, attackActionChecker, actionUnitChecker);

        return message;
    }

    /*
     * Check whether move orders are valid.
     */
    private String checkMoveValid(ArrayList<MoveOrder> mos, OrderRuleChecker moveActionChecker,
            UnitValidRuleChecker moveActionUnitChecker) {
        for (MoveOrder mo : mos) {
            String message = moveActionChecker.checkOrder(mo,
                    this.gameController.getRiskMap().getPlayerByName(playerName), this.gameController.getRiskMap());
            if (message != null) {
                return message;
            }
        }
        return moveActionUnitChecker.checkMoveOrderUnitValid(this.gameController.getRiskMap(), mos);
    }

    /*
     * Check whether attack orders are valid.
     */
    private String checkAttackValid(ArrayList<AttackOrder> aos, OrderRuleChecker attackActionChecker,
            UnitValidRuleChecker attackActionUnitChecker) {
        for (AttackOrder ao : aos) {
            String message = attackActionChecker.checkOrder(ao,
                    this.gameController.getRiskMap().getPlayerByName(playerName), this.gameController.getRiskMap());
            if (message != null) {
                return message;
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