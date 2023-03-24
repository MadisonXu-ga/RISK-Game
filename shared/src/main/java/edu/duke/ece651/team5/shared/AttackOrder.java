package edu.duke.ece651.team5.shared;

import java.util.LinkedHashMap;
import java.util.Random;

public class AttackOrder extends BasicOrder {
    

    public AttackOrder(String source, String destination, int number, Unit type, String playerName) {
        super(source, destination, number, type, playerName);

    }

    @Override
    public void execute(RISKMap map) {
        Territory source = map.getTerritoryByName(sourceName);
        source.updateUnitCount(UnitType.SOLDIER, true, number);
    }
}
