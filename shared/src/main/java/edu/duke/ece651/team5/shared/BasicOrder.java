package edu.duke.ece651.team5.shared;

public abstract class BasicOrder {
    protected Territory source;
    protected Territory destination;
    protected int number;
    protected Unit type;


    public BasicOrder(Territory source, Territory destination, int number, Unit type) {
        this.source = source;
        this.destination = destination;
        this.number = number;
        this.type = type;
    }

    public abstract void execute();
}

