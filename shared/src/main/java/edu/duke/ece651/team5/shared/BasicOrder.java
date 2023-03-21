package edu.duke.ece651.team5.shared;

public abstract class BasicOrder {
    protected String sourceName;
    protected String destinationName;
    protected int number;
    protected Unit type;
    protected RISKMap RISKMap;


    public BasicOrder(Territory source, Territory destination, int number, Unit type) {
        this.source = source;
        this.destination = destination;
        this.number = number;
        this.type = type;
        this.RISKMap = RISKMap;
    }

    public abstract void execute(RISKMap map);

    public String getSourceName() {
        return sourceName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public int getNumber() {
        return number;
    }

    public Unit getType() {
        return type;
    }
}

