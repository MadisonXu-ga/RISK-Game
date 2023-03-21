package edu.duke.ece651.team5.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import edu.duke.ece651.team5.shared.MoveOrder;

public class PlayHandler extends ConnectionHandler {
    private GameController gameController;
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
            Action action = (Action) recvObject();
            isValid = checkActions(action);
        } while (!isValid);
    }

    private void checkActions(Action action){
        ArrayList<MoveOrder> mos = action.getMoveOrders();
        ArrayList<AttackOrder> aos = action.getAttackOrders();

        
    }
}
