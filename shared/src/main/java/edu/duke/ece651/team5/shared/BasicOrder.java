package edu.duke.ece651.team5.shared;

public abstract class BasicOrder {
    protected String sourceName;
    protected String destinationName;
    protected int number;
    protected Unit type;

    public BasicOrder(String sourceName, String destinationName, int number, Unit type) {
        this.sourceName = sourceName;
        this.destinationName = destinationName;
        this.number = number;
        this.type = type;
    }

    public abstract void execute(RISKMap map);
}

