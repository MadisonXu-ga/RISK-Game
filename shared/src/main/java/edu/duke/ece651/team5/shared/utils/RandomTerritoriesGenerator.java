package edu.duke.ece651.team5.shared.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;

public class RandomTerritoriesGenerator {

    public static List<Territory> generate(RISKMap map){
        List<String> territoryKeys = new ArrayList<>(map.getAllTerritories().keySet());
        List<Territory> selectedTerritories = new ArrayList<>();
        // long seed = 12345L; // choose a fixed seed
        // Collections.shuffle(territoryKeys, new Random(seed));
        int num = Math.min(10, map.getAllTerritories().size() / 2);
        for (int i = 0; i < num; i++) {
            String territoryKey = territoryKeys.get(i);
            Territory territory = map.getTerritoryByName(territoryKey);
            selectedTerritories.add(territory);
        }

        return selectedTerritories;
    }
}
