package edu.duke.ece651.team5.shared.game;

import edu.duke.ece651.team5.shared.order.AttackOrder;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * This class handle Players who choose to attack one territory in a turn
 */

public class CombatPlayers {
    //list to keep track of bonus unit
    private final ArrayList<Integer> levelToBonus = 
        new ArrayList<>(Arrays.asList(0, 1, 3, 5, 8, 11, 15));
    
    //list to store soldier level
    private final SoldierLevel[] bonusToLevel = SoldierLevel.values();
    
    //players with list of soldier represented by integer
    private Map<Player, List<Integer>> playerToBonusSoldier;
    private List<Player> combatPlayersforThisTurn = new ArrayList<>();
    Map<Player, Integer> alliaceRatio = new HashMap<>();

    /**
     * constructor to create combatPlayers in combatResolver
     * abstract into CombatResolver, should not call directly
     * @param attackOrders
     */
    public CombatPlayers(List<AttackOrder> attackOrders){
        this.playerToBonusSoldier = createBonusUnit(attackOrders);
    }

    /**
     * getter for players with list of soldier
     * @return map with player as key, and list of soldiers(represented in values with bonus) as value 
     */
    public Map<Player, List<Integer>> getPlayerToBonusSoldier() {
        return playerToBonusSoldier;
    }

    public List<Player> getCombatPlayersForThisTurn(){
        return combatPlayersforThisTurn;
    }

    /**
     * convert bonus soldier to actual soldier
     * @param winner winner Player for this round
     * @return converted soldier with corresponding number
     */
    public Map<Soldier, Integer> convertToSoldier(Player winner){
        Map<Soldier, Integer> res = new HashMap<>();
        for(Integer bonus: playerToBonusSoldier.get(winner)){
            Soldier soldier = new Soldier(bonusToLevel[levelToBonus.indexOf(bonus)]);
            res.merge(soldier, 1, Integer::sum);
        }
        return res;
    }

    /**
     * player lost unit when lose in certain turn
     * if detect player is lose in this round, remove this player in combatPlayers
     * @param loser loser Player for certain turn 
     * @param isAttack boolean to check if player is attacker or defender
     */
    public void resolveLosePlayer(Player loser, boolean isAttack){
        int removeIdx = isAttack ? 0 :  playerToBonusSoldier.get(loser).size() - 1;
        playerToBonusSoldier.get(loser).remove(removeIdx);
        System.out.println("loser: " + loser.getName() + "remain unit num: "  + playerToBonusSoldier.get(loser).size());
        if(playerToBonusSoldier.get(loser).size() == 0){
            playerToBonusSoldier.remove(loser);
        }
    }

    /**
     * check if certain player is lose for certain round
     * @param loser Player to check
     * @return true if lose, false if not
     */
    public boolean isPlayerLose(Player loser){
        return !playerToBonusSoldier.containsKey(loser);
    }

    /**
     * private helper method to convert attackOrder to combat players
     * @param attackOrders orders need to converted for current turn
     * @return a map with Player as key, list of integer of values ready for combat
     */
    private Map<Player, List<Integer>> createBonusUnit(List<AttackOrder> attackOrders){
        Map<Player, List<Integer>> playerToBonusSoldier = new HashMap<>();
        for (AttackOrder order : attackOrders) {
            playerToBonusSoldier.put(order.getPlayer(), addBonusToOrder(order));
            combatPlayersforThisTurn.add(order.getPlayer());
        }
        return mergeAlliance(playerToBonusSoldier);
    }


    private Map<Player, List<Integer>> mergeAlliance( Map<Player, List<Integer>> playerToBonusSoldier){
        Map<Player, List<Integer>> mergeAlliance = new HashMap<>();

        for (Player player : playerToBonusSoldier.keySet()) {
            if (mergeAlliance.containsKey(player)) continue;
    
            Player alliance = player.getAlliancePlayer();
            if (alliance == null) {
                mergeAlliance.put(player, playerToBonusSoldier.get(player));
                continue;
            }
    
            List<Integer> playerBonusSoldier = playerToBonusSoldier.get(player);
            List<Integer> allianceBonusSoldier = playerToBonusSoldier.get(alliance);
            List<Integer> combinedBonusSoldier = new ArrayList<>(playerBonusSoldier);
            combinedBonusSoldier.addAll(allianceBonusSoldier);
    
            int playerCount = playerBonusSoldier.stream().mapToInt(Integer::intValue).sum();
            int allianceCount = allianceBonusSoldier.stream().mapToInt(Integer::intValue).sum();
            Player leader = (playerCount > allianceCount) ? player : alliance;
            Player dependence = (leader == player) ? alliance : player;
            int ratio = Math.floorDiv(Math.min(playerCount, allianceCount), Math.max(playerCount, allianceCount));
    
            mergeAlliance.put(leader, combinedBonusSoldier);
            alliaceRatio.put(dependence, ratio);
        }
    
        return mergeAlliance;

    }

    /**
     * helper method to add bonus to each order's soldier
     * @param order order needs to convert
     * @return a list of integer represent unit with bonus 
     */
    private List<Integer> addBonusToOrder(AttackOrder order){
        List<Integer> bonusSoldier = new ArrayList<>();
        for(Soldier soldier: order.getSoldierToNumber().getAllSoldiers().keySet()){
            int bonus = levelToBonus.get(soldier.getLevel().ordinal());
            int numToAdd = order.getSoldierToNumber().getAllSoldiers().get(soldier);
            bonusSoldier.addAll(Collections.nCopies(numToAdd, bonus));
        }
        bonusSoldier.sort(Collections.reverseOrder());
        return bonusSoldier;
    }
}
