package edu.duke.ece651.team5.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import edu.duke.ece651.team5.shared.Action;
import edu.duke.ece651.team5.shared.AdjacentRuleChecker;
import edu.duke.ece651.team5.shared.MoveOrder;
import edu.duke.ece651.team5.shared.MoveOwnershipRuleChecker;
import edu.duke.ece651.team5.shared.OrderRuleChecker;

public class PlayHandler extends ConnectionHandler {
    private GameController gameController;
    private Action action;

    public PlayHandler(ObjectOutputStream oos, ObjectInputStream ois, GameController gameController) {
        super(oos, ois);
        this.gameController = gameController;
    }

    @Override
    public void run() {
        // TODO: sss
        try {
            boolean isValid = false;
            do {
                sendObject(gameController.getRiskMap());
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
        ArrayList<Integer> aos = action.getAttackOrders();

        OrderRuleChecker moveActionChecker = new MoveOwnershipRuleChecker(new AdjacentRuleChecker(null));
        // TODO: attackActionChecker

        // TODO: check unit number later
        for (MoveOrder mo : mos) {
            String message = moveActionChecker.checkOrder(mo, null, null);
            if (message != null) {
                return false;
            }
        }

        for (Integer ao : aos) {
            // TODO: check attack valid
        }

        return true;
    }

    public ArrayList<MoveOrder> getPlayerMoveOrders() {
        return action.getMoveOrders();
    }

    public ArrayList<Integer> getPlayerAttackOrders() {
        return action.getAttackOrders();
    }
}
