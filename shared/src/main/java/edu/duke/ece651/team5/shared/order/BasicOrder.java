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
     // name of the source territory
     protected String sourceName;
     // name of the dest territory
     protected String destinationName;
     // soldier army to be manipulated on
     protected SoldierArmy soldierToNumber;
     // the player who issued this order
     protected Player player;

    /**
     * constructor for all params
     * @param sourceName name of the source territory
     * @param destinationName name of the dest territory
     * @param soldierToNumber soldier army to be manipulated on
     * @param player the player who issued this order
     */
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
      * @return soldier army to be manipulated on
      */
     public SoldierArmy getSoldierToNumber() {
         return soldierToNumber;
     }

     
     /**
      * get player
      *
      * @return the player issued this order
      */
     public Player getPlayer() {
         return player;
     }
 

}
