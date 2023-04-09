package edu.duke.ece651.team5.shared.order;

import java.io.Serializable;
import java.util.Map;

import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.*;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;

public abstract class BasicOrder implements Order, Serializable {
     // @Serial
     private static final long serialVersionUID = 1847314966415949919L;
     protected String sourceName;
     protected String destinationName;
     //todo: change this to soldier army
     protected SoldierArmy soldierToNumber;
     protected Player player;

     public BasicOrder(String sourceName, String destinationName, SoldierArmy soldierToNumber, Player player) {
         this.sourceName = sourceName;
         this.destinationName = destinationName;
         this.soldierToNumber = soldierToNumber;
         this.player = player;
     }

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
     public SoldierArmy getSoldierToNumber() {
         return soldierToNumber;
     }

     
     /**
      * get playerName
      * @return
      */
     public Player getPlayer() {
         return player;
     }
 

}
