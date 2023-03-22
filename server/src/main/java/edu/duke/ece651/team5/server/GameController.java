package edu.duke.ece651.team5.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import edu.duke.ece651.team5.shared.*;;

public class GameController {
    private RISKMap riskMap;
    private ArrayList<String> playerNames;
    private ArrayList<Player> defaultPlayers;

    public GameController() {
        this.playerNames = new ArrayList<>(Arrays.asList("Green", "Blue", "Red", "Yellow"));
        this.defaultPlayers = new ArrayList<>(Arrays.asList(
            new Player("Green"), 
            new Player("Blue"), 
            new Player("Red"), 
            new Player("Yellow"))); 
        // change to playerName
        this.riskMap = new RISKMap(defaultPlayers);
    }

    public void assignTerritories(int numPlayers){
        ArrayList<String> terriName = new ArrayList<> (Arrays.asList("Narnia", "Midkemia", "Oz", "Elantris", "Scadrial", "Roshar", "Gondor", "Mordor", "Hogwarts"));
        for(int i=0; i<numPlayers; i++){
            Player p = riskMap.getPlayerByName(playerNames.get(i));
            for(int j=0; j<(terriName.size()/numPlayers); j++){
                p.addTerritory(riskMap.getTerritoryByName(terriName.get(j+(i*3))));
                riskMap.getTerritoryByName(terriName.get(j+(i*3))).setOwner(p);
            }
        }
    }

    public RISKMap getRiskMap() {
        return this.riskMap;
    }

    public String getPlayerName(int index) {
        return playerNames.get(index);
    }

    public void resolveUnitPlacement(HashMap<String, Integer> unitPlacements) {
        for (Map.Entry<String, Integer> entry : unitPlacements.entrySet()) {
            String name = entry.getKey();
            System.out.println("num: name");
            int unitNum = entry.getValue();
            Territory terr = riskMap.getTerritoryByName(name);
            System.out.println("inital unitNum: " + terr.getUnitNum(UnitType.SOLDIER));
            terr.updateUnitCount(UnitType.SOLDIER, false, unitNum);
            System.out.println("Get updated: " + terr.getUnitNum(UnitType.SOLDIER));
        }
    }

    public void executeAttackOrder(ArrayList<AttackOrder> attackOrders){
        for(AttackOrder order: attackOrders){
            order.execute(riskMap);

        }
    }

    public HashMap<String, ArrayList<AttackOrder>> mergeSamePlayers(HashMap<String, ArrayList<AttackOrder>> attackOrderByTerris){
        //des terri, arraylist
        HashMap<String, ArrayList<AttackOrder>> mergeSamePlayerOrders = new HashMap<>();
        for(String destiTerri: attackOrderByTerris.keySet()){
            //key player value attakorer
            HashMap<String, AttackOrder> mergeOrder = new HashMap<>();
            for(AttackOrder order: attackOrderByTerris.get(destiTerri)){
                if(!mergeOrder.containsKey(order.getPlayerName())){
                    mergeOrder.put(order.getPlayerName(), order);
                }else{
                    mergeOrder.get(order.getPlayerName()).updateUnitNumber(order.getNumber());
                }
            }
            ArrayList<AttackOrder> orders = new ArrayList<>();
            orders.addAll(mergeOrder.values());
            mergeSamePlayerOrders.put(destiTerri, orders);
        }
        return mergeSamePlayerOrders;
    }

    /*
     * Add one new unit to every territoried at the end of one turn.
     */
    public void addOneUnitToTerrirories() {
        for (Territory terr : riskMap.getTerritories()) {
            terr.updateUnitCount(UnitType.SOLDIER, false, 1);
        }
    }



    //todo merge attack order belong to same player
    public void resolveAttackOrder(HashMap<String, ArrayList<AttackOrder>> attackOrderByTerris){
        HashMap<String, ArrayList<AttackOrder>> mergeSamePlayerOrders = mergeSamePlayers(attackOrderByTerris);
        for(String terriName: mergeSamePlayerOrders.keySet()){
            beginFight(riskMap.getTerritoryByName(terriName), attackOrderByTerris.get(terriName));
        }
    }

    protected void beginFight(Territory fightingTerri, ArrayList<AttackOrder> fightOrders){
        if(fightOrders.isEmpty()){
            return;
        }
        AttackOrder defenseOrder = new AttackOrder(fightingTerri.getName(), fightingTerri.getName(), fightingTerri.getUnitNum(UnitType.SOLDIER), UnitType.SOLDIER, fightingTerri.getOwner().getName());
        defenseOrder.execute(riskMap);
        fightOrders.add(defenseOrder);
        ArrayList<Boolean> check = new ArrayList<>();
        for(int i=0; i<fightOrders.size();++i){
            check.add(true);
        }
        while(fightOrders.size() != 1){
            for(int i=0; i<fightOrders.size(); i++){
                if(!check.get(i)){
                    continue;
                }
                if(fightOrders.get(i).getNumber() == 0){
                    // fightOrders.remove(i);
                    check.set(i, false);
                    continue;
                }
                AttackOrder loserForThisRound = (rollDice()) ? fightOrders.get(i): ((i == fightOrders.size()-1) ? fightOrders.get(0) :  fightOrders.get(i + 1));
                System.out.println("loser: " + loserForThisRound.getSourceName());
                loserForThisRound.loseOneUnit();
                System.out.println("unit after lose: " + loserForThisRound.getNumber());
                if(loserForThisRound.getNumber() == 0){
                    // fightOrders.remove(loserForThisRound);
                    check.set(fightOrders.indexOf(loserForThisRound), false);
                }
            }
        }
        System.out.println("winner: " + fightOrders.get(0).getPlayerName());
        resolveWinnerForThisRound(fightOrders.get(0), fightingTerri);
    }

    protected void resolveWinnerForThisRound(AttackOrder winOrder, Territory fightingTerri){
        fightingTerri.getOwner().loseTerritory(fightingTerri);
        Player winner = riskMap.getTerritoryByName(winOrder.getSourceName()).getOwner();
        fightingTerri.setOwner(winner);
        System.out.println("new owner: " + fightingTerri.getOwner().getName());
        fightingTerri.updateUnitCount(UnitType.SOLDIER, false, winOrder.getNumber());
        System.out.println("new unit: " + fightingTerri.getUnitNum(UnitType.SOLDIER));
        winner.addTerritory(fightingTerri);
    }

    protected boolean rollDice(){
        Random rand = new Random();
        int x = rand.nextInt(20);
        int y = rand.nextInt(20);
        return x > y;
    }
}
