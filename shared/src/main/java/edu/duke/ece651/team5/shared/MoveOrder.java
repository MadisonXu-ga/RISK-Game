package edu.duke.ece651.team5.shared;

public class MoveOrder extends BasicOrder {

    public MoveOrder(String sourceName, String destinationName, int number, Unit type) {
        super(sourceName, destinationName, number, type);
    }

    @Override
    public void execute(RISKMap map) {
        Territory source = map.getTerritoryByName(sourceName);
        Territory destination = map.getTerritoryByName(destinationName);
        source.updateUnitCount(type, true, number);
        destination.updateUnitCount(type, false, number);
    }
}

