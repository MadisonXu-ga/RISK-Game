package edu.duke.ece651.team5.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import edu.duke.ece651.team5.shared.Action;
import edu.duke.ece651.team5.shared.AdjacentRuleChecker;
import edu.duke.ece651.team5.shared.AttackOrder;
import edu.duke.ece651.team5.shared.MoveOrder;
import edu.duke.ece651.team5.shared.MoveOwnershipRuleChecker;
import edu.duke.ece651.team5.shared.OrderRuleChecker;

public class PlayHandler extends ConnectionHandler {
    private GameController gameController;
    private Action action;
    private Boolean playerConnectionStatus;

    public PlayHandler(ObjectOutputStream oos, ObjectInputStream ois, GameController gameController,
            Boolean playerConnectionStatus) {
        super(oos, ois);
        this.gameController = gameController;
        this.playerConnectionStatus = playerConnectionStatus;
    }

    @Override
    public void run() {
        try {
            if (playerConnectionStatus != false) {
                sendObject(gameController.getRiskMap());
            }
            if(playerConnectionStatus!=true){
                return;
            }
            // only normal connections send actions
            boolean isValid = false;
            do {
                this.action = (Action) recvObject();
                isValid = checkActions(action);
            } while (!isValid);
            sendObject(isValid);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean checkActions(Action action) {
        ArrayList<MoveOrder> mos = action.getMoveOrders();
        ArrayList<AttackOrder> aos = action.getAttackOrders();

        OrderRuleChecker moveActionChecker = new MoveOwnershipRuleChecker(new AdjacentRuleChecker(null));
        // TODO: attackActionChecker

        // TODO: check unit number later
        for (MoveOrder mo : mos) {
            String message = moveActionChecker.checkOrder(mo, null, null);
            if (message != null) {
                return false;
            }
        }

        for (AttackOrder ao : aos) {
            // TODO: check attack valid
        }

        return true;
    }

    public ArrayList<MoveOrder> getPlayerMoveOrders() {
        return action.getMoveOrders();
    }

    public ArrayList<AttackOrder> getPlayerAttackOrders() {
        return action.getAttackOrders();
    }
}
