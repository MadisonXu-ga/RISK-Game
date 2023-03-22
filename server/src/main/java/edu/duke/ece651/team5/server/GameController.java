package edu.duke.ece651.team5.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import edu.duke.ece651.team5.shared.Player;
import edu.duke.ece651.team5.shared.RISKMap;
import edu.duke.ece651.team5.shared.Territory;
import edu.duke.ece651.team5.shared.UnitType;

public class GameController {
    private RISKMap riskMap;
    private ArrayList<String> playerNames;
    private ArrayList<Player> defaultPlayers;

    public GameController() {
        this.playerNames = new ArrayList<>(Arrays.asList("Green", "Blue", "Red", "Yellow"));
        this.defaultPlayers = new ArrayList<>(Arrays.asList(
            new Player("Green"), 
            new Player("Blue"), 
            new Player("Red"), 
            new Player("Yellow"))); 
        // change to playerName
        this.riskMap = new RISKMap(defaultPlayers);
    }

    public void assignTerritories(int numPlayers){
        ArrayList<String> terriName = new ArrayList<> (Arrays.asList("Narnia", "Midkemia", "Oz", "Elantris", "Scadrial", "Roshar", "Gondor", "Mordor", "Hogwarts"));
        for(int i=0; i<numPlayers; i++){
            Player p = riskMap.getPlayerByName(playerNames.get(i));
            for(int j=0; j<(terriName.size()/numPlayers); j++){
                p.addTerritory(riskMap.getTerritoryByName(terriName.get(j+(i*3))));
                riskMap.getTerritoryByName(terriName.get(j+(i*3))).setOwner(p);
            }
        }
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
