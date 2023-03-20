package edu.duke.ece651.team5.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import edu.duke.ece651.team5.shared.RISKMap;
import edu.duke.ece651.team5.shared.Territory;
import edu.duke.ece651.team5.shared.UnitType;

public class GameController {
    private RISKMap riskMap;
    private ArrayList<String> playerNames;

    public GameController() {
        this.riskMap = new RISKMap();
        this.playerNames = new ArrayList<>(Arrays.asList("Green", "Blue", "Red", "Yellow")); // change to playerName
    }

    public RISKMap getRiskMap() {
        return this.riskMap;
    }

    public String getPlayerName(int index) {
        return playerNames.get(index);
    }

    public void resolveUnitPlacement(HashMap<String, Integer> unitPlacements) {
        for (Map.Entry<String, Integer> entry : unitPlacements.entrySet()) {
            String name = entry.getKey();
            int unitNum = entry.getValue();
            Territory terr = riskMap.getTerritoryByName(name);
            terr.updateUnitCount(UnitType.SOLDIER, false, unitNum);
        }
    }
}
