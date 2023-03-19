package edu.duke.ece651.team5.shared;

public abstract class BasicOrder {
    protected Territory source;
    protected Territory destination;
    protected int number;
    protected Unit type;
    protected RISKMap RISKMap;


    public BasicOrder(Territory source, Territory destination, int number, Unit type, RISKMap RISKMap) {
        this.source = source;
        this.destination = destination;
        this.number = number;
        this.type = type;
        this.RISKMap = RISKMap;
    }

    public abstract void execute();
}

