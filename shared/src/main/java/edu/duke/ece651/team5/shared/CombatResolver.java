package edu.duke.ece651.team5.shared;

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
    /**
     * merge attack order offered by same player to attack same destination territory
     * @param attackOrder
     * @return
     */
    public List<AttackOrder> mergeOrderByTerriForOnePlayer(List<AttackOrder> attackOrder) {
        
        Map<String, AttackOrder> mergedOrders = new HashMap<>();
        for (AttackOrder order : attackOrder) {
            String playerName = order.getPlayerName();
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
     * 
     * @param fightingTerri
     * @param fightOrders
     * @param map
     */
    //todo: change fighting way for unit
    protected void beginFight(Territory fightingTerri, List<AttackOrder> fightOrders, Game game) {
        if (fightOrders.isEmpty()) {
            return;
        }
    
        AttackOrder defenseOrder = new AttackOrder(fightingTerri.getName(), fightingTerri.getName(),
                fightingTerri.getSoldierArmy().getAllSoldiers(), fightingTerri.getOwner());
        defenseOrder.execute(game.getMap());
        System.out.println("=====check defense unit amount: " + fightingTerri.getSoldierArmy().getAllSoldiers());
        fightOrders.add(defenseOrder);
    
        Set<AttackOrder> remainingPlayers = new HashSet<>(fightOrders);
    
        while (remainingPlayers.size() > 1) {
            for (Iterator<AttackOrder> it = remainingPlayers.iterator(); it.hasNext();) {
                AttackOrder player = it.next();
                if (player.getSoldierToNumber().size() == 0) {
                    it.remove();
                    continue;
                }
                AttackOrder opponent = getNextOpponent(player, remainingPlayers);
                AttackOrder loserForThisRound = (rollDice()) ? player : opponent;
                System.out.println("loser: " + loserForThisRound.getSourceName());
                loserForThisRound.loseOneUnit();
    
                if (loserForThisRound.getSoldierToNumber().size() == 0) {
                    it.remove();
                }
            }
        }
    
        AttackOrder winner = remainingPlayers.iterator().next();
        System.out.println("winner: " + winner.getPlayerName());
        resolveWinnerForThisRound(winner, fightingTerri, game);
    }
    
    private AttackOrder getNextOpponent(AttackOrder player, Set<AttackOrder> remainingPlayers) {
        Iterator<AttackOrder> it = remainingPlayers.iterator();
        while (it.hasNext()) {
            AttackOrder opponent = it.next();
            if (!opponent.equals(player)) {
                return opponent;
            }
        }
        return null;
    }
    
    //todo change territory player field to Player?
    protected void resolveWinnerForThisRound(AttackOrder winOrder, Territory fightingTerri, Game game) {
        //remove fight territories owner
        game.getPlayeryByName(fightingTerri.getOwner()).loseTerritory(fightingTerri);
        Territory sourceTerri = game.getMap().getTerritoryByName(winOrder.getSourceName());
        String winner = sourceTerri.getOwner();
        fightingTerri.setOwner(winner);
        System.out.println("new owner: " + fightingTerri.getOwner());
        fightingTerri.getSoldierArmy().updateSoldier(winOrder.getSoldierToNumber());
        System.out.println("new unit: " + fightingTerri.getSoldierArmy());
        game.getPlayeryByName(winner).addTerritory(fightingTerri);
    }
    
    protected boolean rollDice() {
        Random rand = new Random(42);
        int x = rand.nextInt(20);
        int y = rand.nextInt(20);
        return x > y;
    }
    




    
    
}
