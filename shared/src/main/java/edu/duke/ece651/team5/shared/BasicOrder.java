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

    /**
     * The actual updates if an order is executed
     * @param map the map
     */
    public abstract void execute(RISKMap map);

    /**
     * Getter for source territory name
     * @return the name of source destination
     */
    public String getSourceName() {
        return sourceName;
    }

    /**
     * Getter for destination territory name
     * @return the name of dest territory
     */
    public String getDestinationName() {
        return destinationName;
    }

    /**
     * Getter for number of units that is going to change
     * @return number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Getter for unit type
     * @return the type
     */
    public Unit getType() {
        return type;
    }
}

