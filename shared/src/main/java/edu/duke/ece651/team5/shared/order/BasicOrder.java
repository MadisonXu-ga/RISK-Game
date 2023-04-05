package edu.duke.ece651.team5.shared.order;



import java.io.Serializable;
import java.util.Map;

import edu.duke.ece651.team5.shared.Order;
import edu.duke.ece651.team5.shared.Player;
import edu.duke.ece651.team5.shared.RISKMap;
import edu.duke.ece651.team5.shared.unit.Soldier;

public abstract class BasicOrder implements Order, Serializable {
     // @Serial
     private static final long serialVersionUID = 1847314966415949919L;
     protected String sourceName;
     protected String destinationName;
     //todo: change this to soldier army
     protected Map<Soldier, Integer> soldierToNumber;
     protected Player player;

     public BasicOrder(String sourceName, String destinationName, Map<Soldier, Integer> soldiers, Player player) {
         this.sourceName = sourceName;
         this.destinationName = destinationName;
         this.soldierToNumber = soldiers;
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
     public Map<Soldier, Integer> getSoldierToNumber() {
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
