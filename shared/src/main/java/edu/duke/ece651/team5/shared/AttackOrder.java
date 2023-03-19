package edu.duke.ece651.team5.shared;

import java.util.LinkedHashMap;
import java.util.Random;

public class AttackOrder extends BasicOrder{

    private LinkedHashMap <Player, Unit> attackingUnits;
    
    public AttackOrder(Territory source, Territory destination, int number, Unit type, RISKMap RISKMAP) {
        super(source, destination, number, type, RISKMAP);
        source.updateUnitCount(type, true, number);

    }

    @Override
    public void execute() {

        /*
         *  VERIFICATION
         *  see if source has enough units to attack
         * territory source owner != territority destination owner
         * units to attack >0, source and destination specified 
         * attack can only be done in adjacent territories
         * 
         * 
         */
        //instantiate a list of <playerName, unitsAttacking>
        
        /*
         * MECHANICS TO ATTACK
         * validation occurs
         * create a list of <playerName, units> for each unit going into a territory
         * do a "move out, for the units that are attacking from the source territories"
         * initialize the dice for attacker and defender 
         * iteratate through the list of attackers and 
         */


        Territory winner = null; 
        
        Random rand = new Random();
        int dice_attacker = rand.nextInt(20);
        int dice_deffender = rand.nextInt(20);

        if (dice_attacker > dice_deffender){
            //the defender is the one that loses the unit
            destination.updateUnitCount(type, true, 1);
        }
        else{
            //the attacker is the one that loses the unit (equal is won by defender)
            source.updateUnitCount(type, true, 1);
        }
         


        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
    
    


    public boolean verifyAttackOrder(Territory source, Territory destination, int number, Unit type){

        //TODO we need to check if there are enough units from source to attack
        
        if(source.whoOwnsit() == destination.whoOwnsit()){
            return false;
        }
        else if(number <= 0 ){
            return false;
        }
        else if (!RISKMap.isAdjacent(source, destination)){
            return false;
        }

        //TODO: maybe add the type of unit in the future

        return true;
    }
}
