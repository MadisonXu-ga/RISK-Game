package edu.duke.ece651.team5.shared;

import java.io.Serializable;

public class MoveOrder extends BasicOrder implements Serializable {

    public MoveOrder(String sourceName, String destinationName, int number, Unit type) {
        super(sourceName, destinationName, number, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoveOrder m = (MoveOrder) o;

    public MoveOrder(Territory source, Territory destination, int number, Unit type) {
        super(source, destination, number, type);
    }

    @Override
    public void execute(RISKMap map) {
        Territory source = map.getTerritoryByName(sourceName);
        Territory destination = map.getTerritoryByName(destinationName);
        source.updateUnitCount(type, true, number);
        destination.updateUnitCount(type, false, number);
    }
}

