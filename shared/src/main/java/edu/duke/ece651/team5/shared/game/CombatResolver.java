package edu.duke.ece651.team5.shared.game;

import edu.duke.ece651.team5.shared.order.AttackOrder;
import edu.duke.ece651.team5.shared.unit.Soldier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

 //todo can move to server
public class CombatResolver {
    
    public CombatResolver() {
    }

   
    /**
     * merge attack order offered by same player to attack same destination territory
     * @param attackOrder
     * @return
     */
    public List<AttackOrder> mergeOrderByTerriForOnePlayer(List<AttackOrder> attackOrder) {
        Map<String, AttackOrder> mergedOrders = new HashMap<>();
        for (AttackOrder order : attackOrder) {
            String playerName = order.getPlayer().getName();
            AttackOrder mergedOrder = mergedOrders.get(playerName);
            if (mergedOrder == null) {
                // First order for this player
                mergedOrders.put(playerName, order);
            } else {
                // Merge with existing order for this player
                mergedOrder.mergeWith(order);
            }
        }
        // Return merged orders as a list
        return new ArrayList<>(mergedOrders.values());
    }


    /**
     * merge different players who choose to attack same territory, get ready to combat
     * @param attackOrders
     * @return
     */
    public Map<String, List<AttackOrder>> mergeOrderByTerritory(List<AttackOrder> attackOrders){
        return attackOrders.stream()
        .collect(Collectors.groupingBy(AttackOrder::getDestinationName));
    }


    /**
     * begin fight process for each territories being attacked
     * @param attackOrderByTerris HashMap<String, ArrayList<AttackOrder>>
     */
    public void resolveAttackOrder(HashMap<String, ArrayList<AttackOrder>> attackOrderByTerris, Game game) {
        System.out.println("============resolve attack order================");
        for (String terriName : attackOrderByTerris.keySet()) {
            System.out.println(terriName + "============begin fight================");
            beginFight(game.getMap().getTerritoryByName(terriName), attackOrderByTerris.get(terriName), game);
        }
    }

    /**
    * begin fight the the target territory
    * @param fightingTerri the target territories needs to be fighted
    * @param fightOrders all the orders that choose to attack this territory
    * @param game current game is playing on
    */
    public void beginFight(Territory fightingTerri, List<AttackOrder> fightOrders, Game game) {
        if (fightOrders.isEmpty()) {
            return;
        }
    
        // Add defender to the combat process
        AttackOrder defenseOrder = new AttackOrder(fightingTerri.getName(), fightingTerri.getName(),
                fightingTerri.getSoldierArmy(), fightingTerri.getOwner());
        fightOrders.add(defenseOrder);
    
        // Create combat players
        CombatPlayers combatPlayers = new CombatPlayers(fightOrders);
    
        // Create a list of attackers
        List<Player> attackers = combatPlayers.getCombatPlayersForThisTurn();
    
        // Use counter to keep track of current attacker
        int currentAttackerIndex = 0;
        while (attackers.size() > 1) {
            Player attacker = attackers.get(currentAttackerIndex);
            System.out.println("Attacker: " + attacker.getName());
            System.out.println("unit " + combatPlayers.getPlayerToBonusSoldier().get(attacker));
    
            Player defender = attackers.get((currentAttackerIndex + 1) % attackers.size());
            System.out.println("Defender: " + defender.getName());
            System.out.println("unit " + combatPlayers.getPlayerToBonusSoldier().get(defender));
    
    
            // Roll dice
            boolean isAttackWin = rollDice(attacker, defender, combatPlayers);
            System.out.println("Attacker win: " + isAttackWin);
    
            // Resolve loser for current round
            Player loserForThisRound = (isAttackWin) ? defender : attacker;
            System.out.println("Loser: " + loserForThisRound.getName());
            combatPlayers.resolveLosePlayer(loserForThisRound, isAttackWin);
    
            // Remove loser from the list of attackers
            if (combatPlayers.isPlayerLose(loserForThisRound)) {
                attackers.remove(loserForThisRound);
            }
    
            // Update counter to next attacker
            currentAttackerIndex = (currentAttackerIndex + 1) % attackers.size();
        }
    
        Player winner = attackers.get(0);
        System.out.println("Winner: " + winner.getName());
        resolveWinnerForThisRound(winner, fightingTerri, combatPlayers, game);
    }
    
    
   /**
    * remove previous owner and set new owner and remain amount of soldier to the territory
    * @param winner winner for this turn
    * @param fightingTerri territory being fight on
    * @param combatPlayers combatPlayers
    */
    protected void resolveWinnerForThisRound(Player winner, Territory fightingTerri, CombatPlayers combatPlayers, Game game) {
        Territory targetTerri = game.getMap().getTerritoryByName(fightingTerri.getName());
        Player targetPlayer = game.getPlayeryByName(winner.getName());
        targetTerri.getOwner().loseTerritory(fightingTerri);
        targetTerri.setOwner(targetPlayer);
        System.out.println("new owner: " + fightingTerri.getOwner().getName());
        Map<Soldier, Integer> res = combatPlayers.convertToSoldier(winner);
        for(Soldier soldier: res.keySet()){
            targetTerri.getSoldierArmy().addSoldier(soldier, res.get(soldier));;
        }
        System.out.println("new unit: " + fightingTerri.getSoldierArmy().getAllSoldiers());
        targetPlayer.addTerritory(fightingTerri);
    }
    
    /**
     * helper method to roll dice
     * @param attacker attack player
     * @param defender defend player
     * @param combatPlayers combatPlayers
     * @return true if attacker win else false
     */
    protected boolean rollDice(Player attacker, Player defender, CombatPlayers combatPlayers) {
        Random rand = new Random(42);
        List<Integer> attackerSoldiers = combatPlayers.getPlayerToBonusSoldier().get(attacker);
        List<Integer> defenderSoldiers = combatPlayers.getPlayerToBonusSoldier().get(defender);
        System.out.println("attack unit w bonus : " + attackerSoldiers.get(0));
        int attackRes = rand.nextInt(20) + attackerSoldiers.get(0);
        System.out.println("attack dice result : " + attackRes);
        System.out.println("defense unit w bonus : " +  defenderSoldiers.get(defenderSoldiers.size() - 1));
        int defenseRes = rand.nextInt(20) + defenderSoldiers.get(defenderSoldiers.size() - 1);
        System.out.println("defense dice result : " + defenseRes);
        return attackRes > defenseRes;
    }

}
