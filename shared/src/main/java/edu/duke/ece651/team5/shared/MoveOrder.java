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

        return sourceName.equals(m.sourceName) && destinationName.equals(m.destinationName) && number == m.number;
    }

    @Override
    public void execute(RISKMap map) {
        Territory source = map.getTerritoryByName(sourceName);
        Territory destination = map.getTerritoryByName(destinationName);
        source.updateUnitCount(type, true, number);
        destination.updateUnitCount(type, false, number);
    }
}

