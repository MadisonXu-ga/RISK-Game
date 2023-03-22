package edu.duke.ece651.team5.shared;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Random;


public class AttackOrder extends BasicOrder implements Serializable{

    public AttackOrder(String source, String destination, int number, Unit type, String playerName) {
        super(source, destination, number, type, playerName);
        // source.updateUnitCount(type, true, number);

    }

    /**
     * update source territory number of unit
     */
    @Override
    public void execute(RISKMap map) {
        Territory source = map.getTerritoryByName(sourceName);
        source.updateUnitCount(type, true, number);
    }
}
