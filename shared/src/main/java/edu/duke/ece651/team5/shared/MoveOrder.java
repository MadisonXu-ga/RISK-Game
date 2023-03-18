package edu.duke.ece651.team5.shared;

public class MoveOrder extends BasicOrder {

    public MoveOrder(Territory source, Territory destination, int number, Unit type) {
        super(source, destination, number, type);
    }

    @Override
    public void execute() {
        source.updateUnitCount(type, true, number);
        destination.updateUnitCount(type, false, number);
    }
}

