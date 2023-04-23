package edu.duke.ece651.team5.shared.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;

public class RandomTerritoriesGenerator {

    public static List<Territory> generate(RISKMap map){
        List<String> territoryKeys = new ArrayList<>(map.getAllTerritories().keySet());
        Collections.shuffle(territoryKeys);
        List<Territory> selectedTerritories = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String territoryKey = territoryKeys.get(i);
            Territory territory = map.getTerritoryByName(territoryKey);
            selectedTerritories.add(territory);
        }

        return selectedTerritories;
    }
}
