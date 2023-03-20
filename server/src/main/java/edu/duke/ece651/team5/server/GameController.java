package edu.duke.ece651.team5.server;

import java.util.ArrayList;
import java.util.Arrays;

import edu.duke.ece651.team5.shared.RISKMap;

public class GameController {
    private RISKMap riskMap;
    private ArrayList<String> playerNames;

    public GameController(){
        this.riskMap = new RISKMap();
        this.playerNames = new ArrayList<>(Arrays.asList("Green", "Blue", "Red", "Yellow")); // change to playerName
    }

    public RISKMap getRiskMap(){
        return this.riskMap;
    }

    public String getPlayerName(int index){
        return playerNames.get(index);
    }
}
