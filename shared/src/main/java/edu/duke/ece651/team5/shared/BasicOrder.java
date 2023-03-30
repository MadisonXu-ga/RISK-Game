package edu.duke.ece651.team5.shared;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class BasicOrder implements Order, Serializable{
     // @Serial
     private static final long serialVersionUID = 1847314966415949919L;
     protected String sourceName;
     protected String destinationName;
     protected Map<Soldier, Integer> soldierToNumber;
     protected String playerName;
 
     /**
      * @param sourceName      String
      * @param destinationName String
      * @param number          int
      * @param type            Unit
      * @param playerName      String
      */
     public BasicOrder(String sourceName, String destinationName, Map<Soldier, Integer> soldiers, String playerName) {
         this.sourceName = sourceName;
         this.destinationName = destinationName;
         this.soldierToNumber = soldiers;
         this.playerName = playerName;
     }
 
    //  public void loseOneUnit() {
    //      number--;
    //  }
 
    //  public void updateUnitNumber(int update) {
    //      number += update;
    //  }
 
     /**
      * The actual updates if an order is executed
      * 
      * @param map the map
      */
     public abstract void execute(RISKMap map);
 
     /**
      * Getter for source territory name
      * 
      * @return the name of source destination
      */
     public String getSourceName() {
         return sourceName;
     }
 
     /**
      * Getter for destination territory name
      * 
      * @return the name of dest territory
      */
     public String getDestinationName() {
         return destinationName;
     }

 
     /**
      * Getter for soldier to number map
      * 
      * @return the type
      */
     public Map<Soldier, Integer> getSoldierToNumber() {
         return soldierToNumber;
     }


     /**
      * get playerName
      * @return
      */
     public String getPlayerName() {
         return playerName;
     }
 

}
