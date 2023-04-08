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

public class CombatPlayers {
    private final ArrayList<Integer> levelToBonus = 
        new ArrayList<>(Arrays.asList(0, 1, 3, 5, 8, 11, 15));
        
    private final SoldierLevel[] bonusToLevel = SoldierLevel.values();
    
    private Map<Player, List<Integer>> playerToBonusSoldier;


    public void setPlayerToBonusSoldier(Map<Player, List<Integer>> playerToBonusSoldier) {
        this.playerToBonusSoldier = playerToBonusSoldier;
    }


    public CombatPlayers(List<AttackOrder> attackOrders){
        this.playerToBonusSoldier = createBonusUnit(attackOrders);
    }


    public Map<Player, List<Integer>> getPlayerToBonusSoldier() {
        return playerToBonusSoldier;
    }

    public Map<Soldier, Integer> convertToSoldier(Player winner){
        Map<Soldier, Integer> res = new HashMap<>();
        for(Integer bonus: playerToBonusSoldier.get(winner)){
            Soldier soldier = new Soldier(bonusToLevel[levelToBonus.indexOf(bonus)]);
            res.merge(soldier, 1, Integer::sum);
        }
        return res;
    }

    public void resolveLosePlayer(Player loser, boolean isAttack){
        int removeIdx = isAttack ? 0 :  playerToBonusSoldier.get(loser).size() - 1;
        playerToBonusSoldier.get(loser).remove(removeIdx);
        if(playerToBonusSoldier.get(loser).size() == 0){
            playerToBonusSoldier.remove(loser);
        }
    }

    public boolean isPlayerLose(Player loser){
        return !playerToBonusSoldier.containsKey(loser);
    }

    private Map<Player, List<Integer>> createBonusUnit(List<AttackOrder> attackOrders){
        Map<Player, List<Integer>> playerToBonusSoldier = new HashMap<>();
        for (AttackOrder order : attackOrders) {
            playerToBonusSoldier.put(order.getPlayer(), addBonusToOrder(order));
        }
        return playerToBonusSoldier;
    }

    private List<Integer> addBonusToOrder(AttackOrder order){
        List<Integer> bonusSoldier = new ArrayList<>();
        for(Soldier soldier: order.getSoldierToNumber().keySet()){
            //todo do bonus need to add original bonus level?
            int bonus = levelToBonus.get(soldier.getLevel().ordinal());
            int numToAdd = order.getSoldierToNumber().get(soldier);
            bonusSoldier.addAll(Collections.nCopies(numToAdd, bonus));
        }
        bonusSoldier.sort(Collections.reverseOrder());
        return bonusSoldier;
    }



}
