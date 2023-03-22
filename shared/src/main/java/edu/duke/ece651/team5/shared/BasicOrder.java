package edu.duke.ece651.team5.shared;

import java.io.Serializable;

public abstract class BasicOrder implements Serializable{
    protected String sourceName;
    protected String destinationName;
    protected int number;
    protected Unit type;
    protected String playerName;

    public BasicOrder(String sourceName, String destinationName, int number, Unit type, String playerName) {
        this.sourceName = sourceName;
        this.destinationName = destinationName;
        this.number = number;
        this.type = type;
        this.playerName = playerName;
    }

    public void loseOneUnit(){
        number--;
    }

    public void updateUnitNumber(int update){
        number += update;
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


    public String getPlayerName(){
        return playerName;
    }
}

