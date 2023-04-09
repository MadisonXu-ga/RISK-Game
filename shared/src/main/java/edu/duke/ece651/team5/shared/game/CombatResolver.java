package edu.duke.ece651.team5.shared.game;

import edu.duke.ece651.team5.shared.order.AttackOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class CombatResolver {
    
    public CombatResolver() {
    }

    //todo can move to server
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

    //todo: change fighting way for unit
    public void beginFight(Territory fightingTerri, List<AttackOrder> fightOrders, Game game) {
        if (fightOrders.isEmpty()) {
            return;
        }
        AttackOrder defenseOrder = new AttackOrder(fightingTerri.getName(), fightingTerri.getName(),
                fightingTerri.getSoldierArmy(), fightingTerri.getOwner());
        fightOrders.add(defenseOrder);
        

        CombatPlayers combatPlayers = new CombatPlayers(fightOrders);
        Set<Player> remainingPlayers = new HashSet<>(combatPlayers.getPlayerToBonusSoldier().keySet());
    
        while (remainingPlayers.size() > 1) {
            for (Iterator<Player> it = combatPlayers.getPlayerToBonusSoldier().keySet().iterator(); it.hasNext();) {
                Player attacker = it.next();
                if (combatPlayers.isPlayerLose(attacker)) {
                    it.remove();
                    continue;
                }
                Player defender = getNextOpponent(attacker, remainingPlayers);
                boolean isAttackWin = rollDice(attacker, defender, combatPlayers);
                Player loserForThisRound = (isAttackWin) ? attacker : defender;
                // System.out.println("loser: " + loserForThisRound.getSourceName());
                combatPlayers.resolveLosePlayer(loserForThisRound, isAttackWin);
                if (combatPlayers.isPlayerLose(loserForThisRound)) {
                    remainingPlayers.remove(loserForThisRound);
                }
            }
        }
        Player winner = remainingPlayers.iterator().next();
        System.out.println("winner: " + winner);
        resolveWinnerForThisRound(winner, fightingTerri, combatPlayers);
    }
    
    private Player getNextOpponent(Player attacker, Set<Player> remainingPlayers) {
        Iterator<Player> it = remainingPlayers.iterator();
        while (it.hasNext()) {
            Player defender = it.next();
            if (!defender.equals(attacker)) {
                return defender;
            }
        }
        return null;
    }
    
    //todo change territory player field to Player?
    protected void resolveWinnerForThisRound(Player winner, Territory fightingTerri, CombatPlayers combatPlayers) {
        //remove fight territories owner
        winner.loseTerritory(fightingTerri);
        fightingTerri.setOwner(winner);
        System.out.println("new owner: " + fightingTerri.getOwner());
        fightingTerri.getSoldierArmy().setSoldiers(combatPlayers.convertToSoldier(winner));
        System.out.println("new unit: " + fightingTerri.getSoldierArmy());
        winner.addTerritory(fightingTerri);
    }
    
    protected boolean rollDice(Player attacker, Player defender, CombatPlayers combatPlayers) {
        Random rand = new Random(42);
        List<Integer> attackerSoldiers = combatPlayers.getPlayerToBonusSoldier().get(attacker);
        List<Integer> defenderSoldiers = combatPlayers.getPlayerToBonusSoldier().get(defender);
        int attackRes = rand.nextInt(20) + attackerSoldiers.get(0);
        int defenseRes = rand.nextInt(20) + defenderSoldiers.get(defenderSoldiers.size() - 1);
        return attackRes > defenseRes;
    }

}
