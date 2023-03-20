package edu.duke.ece651.team5.shared;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
 
public class AttackOrder extends BasicOrder{


    private HashMap<Territory, HashMap <Player, Unit>> attackOrders;
    
    public AttackOrder(Territory source, Territory 

    
        
    }


        
 
        /*
         *  VERIFICATION
         * see if source has enough units to attack ()
         * territory source owner != territority destination owner
         * units to attack >0, source and destination specified 
         * attack can only be done in adjacent territories
         * 
         *  

        //
         * 
         * 
        
         * MECHANICS TO ATTACK
         * validation occurs
         * 
         * create LinkedHashMap<playerName, Unit> for each unit going into a territory
         * check if the name of the player exists, if it does you add units, if it doesnt you create player
         * //TODO create a class that records all the un
         * do a "move out, for the units that are attacking from the source territories"
         * initialize the dice for attacker and defender 
         * //TODO dice process s   b rver (take out, Ruolin)
         * iteratate through the list of attackers and defenders
         * itr1 = 0, itr2 = 1,  12    20    01
         * 

        

        Territory winner = null; 
        
        Random rand = new Random();
        int dice_attacker = rand.nextInt(20);
        int dice_deffender = rand.nextInt(2 0);
 
        if (dice_attacker > dice_deffender){
               destination.updateUnitCount(type, true, 1);
        } 
        else{
         

        


     

    public  boolean verifyAttackOrder(Territory source, Territory destination, i

        // TODO we need to check if there are enough units  from source to attack
        
        i     ret urn false; 
        }
        e     return false; 
        }
        else if (!RISKMap.isAdjacent(source, destination)){
            return false;
        } 

        //TODO: maybe add the type of unit in the future

        return true;
    }
}
