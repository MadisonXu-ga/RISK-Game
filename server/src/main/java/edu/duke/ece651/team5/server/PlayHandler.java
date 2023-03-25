package edu.duke.ece651.team5.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import edu.duke.ece651.team5.shared.*;

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
    protected String checkActions(Action action) {
        ArrayList<MoveOrder> mos = action.getMoveOrders();
        ArrayList<AttackOrder> aos = action.getAttackOrders();

        OrderRuleChecker moveActionChecker = new MoveOwnershipRuleChecker(
                new UnitNumberRuleChecker(new MovePathWithSameOwnerRuleChecker(null)));

        OrderRuleChecker attackActionChecker = new AttackOwnershipRuleChecker(
                new UnitNumberRuleChecker(new AdjacentRuleChecker(null)));

        UnitValidRuleChecker actionUnitChecker = new UnitValidRuleChecker();

        HashMap<String, Integer> oldTerriUnitNum = getTerrUnitNum();

        // check move actions valid
        String message = checkMoveValid(mos, moveActionChecker, actionUnitChecker, oldTerriUnitNum);
        if (message != null) {
            return message;
        }

        // check attack actions valid
        message = checkAttackValid(aos, attackActionChecker, actionUnitChecker, oldTerriUnitNum);

        return message;
    }

    /*
     * Check whether move orders are valid.
     */
    protected String checkMoveValid(ArrayList<MoveOrder> mos, OrderRuleChecker moveActionChecker,
            UnitValidRuleChecker moveActionUnitChecker, HashMap<String, Integer> oldTerriUnitNum) {
        String message = null;
        for (MoveOrder mo : mos) {
            message = moveActionChecker.checkOrder(mo,
                    this.gameController.getRiskMap().getPlayerByName(playerName), this.gameController.getRiskMap());
            if (message != null) {
                revertTerrUnitChanges(oldTerriUnitNum);
                return message;
            }

            mo.execute(this.gameController.getRiskMap());
        }
        return message;
    }

    /**
     * Get territories' unit number
     * 
     * @return territories' unit number
     */
    protected HashMap<String, Integer> getTerrUnitNum() {
        HashMap<String, Integer> TerriUnitNum = new HashMap<>();
        for (Territory terr : this.gameController.getRiskMap().getTerritories()) {
            TerriUnitNum.put(terr.getName(), terr.getUnitNum(UnitType.SOLDIER));
        }
        return TerriUnitNum;
    }

    /**
     * Revert map's territories' unit number to old status
     * 
     * @param oldTerriUnitNum old territories' unit number
     */
    protected void revertTerrUnitChanges(HashMap<String, Integer> oldTerriUnitNum) {
        for (Territory terr : this.gameController.getRiskMap().getTerritories()) {
            terr.setUnitCount(UnitType.SOLDIER, oldTerriUnitNum.get(terr.getName()));
        }
    }

    /*
     * Check whether attack orders are valid.
     */
    protected String checkAttackValid(ArrayList<AttackOrder> aos, OrderRuleChecker attackActionChecker,
            UnitValidRuleChecker attackActionUnitChecker, HashMap<String, Integer> oldTerriUnitNum) {
        String message = null;
        for (AttackOrder ao : aos) {
            message = attackActionChecker.checkOrder(ao,
                    this.gameController.getRiskMap().getPlayerByName(playerName), this.gameController.getRiskMap());
            if (message != null) {
                revertTerrUnitChanges(oldTerriUnitNum);
                return message;
            }
        }
        message = attackActionUnitChecker.checkAttackOrderUnitValid(this.gameController.getRiskMap(), aos);
        revertTerrUnitChanges(oldTerriUnitNum);

        return message;
    }

    /*
     * Get one player's valid move orders in this turn.
     */
    protected ArrayList<MoveOrder> getPlayerMoveOrders() {
        return action.getMoveOrders();
    }

    /*
     * Get one player's valid attack orders in this turn.
     */
    protected ArrayList<AttackOrder> getPlayerAttackOrders() {
        return action.getAttackOrders();
    }

    protected void setAction(Action action){
        this.action = action;
    }
}