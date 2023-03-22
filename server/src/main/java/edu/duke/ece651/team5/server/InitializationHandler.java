package edu.duke.ece651.team5.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import edu.duke.ece651.team5.shared.RISKMap;

public class InitializationHandler extends ConnectionHandler {
    private String name;
    private RISKMap riskMap;
    private HashMap<String, Integer> unitPlacements;
    private UnitValidRuleChecker unitValidRuleChecker;

    public InitializationHandler(ObjectOutputStream oos, ObjectInputStream ois, String name, RISKMap riskMap) {
        super(oos, ois);
        this.name = name;
        this.riskMap = riskMap;
    }

    @Override
    public void run() {
        try {
            sendObject(this.name);
            sendObject(riskMap);
            // get unit placements
            boolean isValid = false;
            HashMap<String, Integer> uPs;
            do {
                uPs = (HashMap<String, Integer>) recvObject();
                isValid = unitValidRuleChecker.checkUnitValid(riskMap, uPs);
                sendObject(isValid);
            } while (!isValid);
            sendObject(isValid);
            this.unitPlacements = uPs;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Integer> getUnitPlacement() {
        return this.unitPlacements;
    }
}
