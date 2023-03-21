package edu.duke.ece651.team5.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import edu.duke.ece651.team5.shared.AdjacentRuleChecker;
import edu.duke.ece651.team5.shared.MoveOrder;
import edu.duke.ece651.team5.shared.MoveOwnershipRuleChecker;
import edu.duke.ece651.team5.shared.OrderRuleChecker;

public class PlayHandler extends ConnectionHandler {
    private GameController gameController;
    private Action action;
    private ArrayList<MoveOrder> moveOrders;
    private ArrayList<AttackOrder> attackOrders;

    public PlayHandler(ObjectOutputStream oos, ObjectInputStream ois, GameController gameController) {
        super(oos, ois);
        this.gameController = gameController;
    }

    @Override
    public void run() {
        // TODO: sss
        boolean isValid = false;
        do {
            sendObject(gameController.getRiskMap());
            this.action = (Action) recvObject();
            isValid = checkActions(action);
        } while (!isValid);
        sendObject(isValid);
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

    public ArrayList<MoveOrder> getPlayerMoveOrders(){

    }

    public ArrayList<AttackOrder> getPlayerAttackOrders(){
        // 
    }
}
