package edu.duke.ece651.team5.shared;

import java.util.LinkedHashMap;
import java.util.Random;

public class AttackOrder extends BasicOrder {

    private LinkedHashMap<Player, Unit> attackingUnits;

    public AttackOrder(String source, String destination, int number, Unit type, String playerName) {
        super(source, destination, number, type, playerName);
        // source.updateUnitCount(type, true, number);

    }

    
    // public boolean verifyAttackOrder(Territory source, Territory destination, int number, Unit type) {

    //     // TODO we need to check if there are enough units from source to attack

    //     if (source.whoOwnsit() == destination.whoOwnsit()) {
    //         return false;
    //     } else if (number <= 0) {
    //         return false;
    //     } else if (!RISKMap.isAdjacent(source, destination)) {
    //         return false;
    //     }

    //     // TODO: maybe add the type of unit in the future

    //     return true;
    // }

    @Override
    public void execute(RISKMap map) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
}
