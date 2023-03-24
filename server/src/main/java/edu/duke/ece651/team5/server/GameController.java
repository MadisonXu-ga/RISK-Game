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

    public void assignTerritories(int numPlayers) {
        ArrayList<String> terriName = new ArrayList<>(Arrays.asList(
                "Narnia", "Elantris", "Midkemia", "Scadrial", "Oz", "Roshar",
                "Gondor", "Mordor", "Hogwarts", "Thalassia", "Arathia",
                "Eryndor", "Sylvaria", "Kaelindor", "Eterna", "Celestia",
                "Frosthold", "Shadowmire", "Ironcliff", "Stormhaven",
                "Mythosia", "Draconia", "Emberfall", "Verdantia"));

        int numTerritories = terriName.size();

        for (int i = 0; i < numTerritories; ++i) {
            Player p = riskMap.getPlayerByName(playerNames.get(i % numPlayers));
            String territoryName = terriName.get(i);
            Territory territory = riskMap.getTerritoryByName(territoryName);
            p.addTerritory(territory);
            territory.setOwner(p);
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

    public void executeAttackOrder(ArrayList<AttackOrder> attackOrders) {
        for (AttackOrder order : attackOrders) {
            order.execute(riskMap);
            System.out.println("initial order================");
            System.out.println("Source: " + order.getSourceName()
                    + "Destination: " + order.getDestinationName()
                    + "Number: " + order.getNumber()
                    + "Player: " + order.getPlayerName());
        }
    }

    public HashMap<String, ArrayList<AttackOrder>> mergeSamePlayers(
            HashMap<String, ArrayList<AttackOrder>> attackOrderByTerris) {
        // des terri, arraylist
        HashMap<String, ArrayList<AttackOrder>> mergeSamePlayerOrders = new HashMap<>();
        for (String destiTerri : attackOrderByTerris.keySet()) {
            // key player value attakorer
            HashMap<String, AttackOrder> mergeOrder = new HashMap<>();
            for (AttackOrder order : attackOrderByTerris.get(destiTerri)) {
                if (!mergeOrder.containsKey(order.getPlayerName())) {
                    mergeOrder.put(order.getPlayerName(), order);
                } else {
                    mergeOrder.get(order.getPlayerName()).updateUnitNumber(order.getNumber());
                }
            }
            System.out.println("merge order================");
            for (AttackOrder order : mergeOrder.values()) {
                System.out.println("Source: " + order.getSourceName()
                        + "Destination: " + order.getDestinationName()
                        + "Number: " + order.getNumber()
                        + "Player: " + order.getPlayerName());
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

    /**
     * Resolve all the move orders from all the players in one turn
     * 
     * @param playerNum              the number of players
     * @param playerConnectionStatus the players' connection staus
     * @param phs                    playerHandlers of all players
     */
    public void resolveAllMoveOrders(int playerNum, HashMap<Integer, Boolean> playerConnectionStatus,
            ArrayList<PlayHandler> phs) {
        for (int i = 0; i < playerNum; ++i) {
            if (playerConnectionStatus.get(i) == null || playerConnectionStatus.get(i) == false) {
                continue;
            }
            ArrayList<MoveOrder> moveOrders = phs.get(i).getPlayerMoveOrders();
            for (MoveOrder mo : moveOrders) {
                mo.execute(this.getRiskMap());
            }
        }
    }

    /**
     * Resolve all the attack orders from all the players in one turn
     * 
     * @param playerNum              the number of players
     * @param playerConnectionStatus the players' connection staus
     * @param phs                    playerHandlers of all players
     */
    public HashMap<Integer, ArrayList<AttackOrder>> resolveAllAttackOrder(int playerNum,
            HashMap<Integer, Boolean> playerConnectionStatus,
            ArrayList<PlayHandler> phs) {
        HashMap<String, ArrayList<AttackOrder>> attackOrdersGroupByTerritory = new HashMap<>();
        ArrayList<AttackOrder> allAttack = new ArrayList<>();
        for (int i = 0; i < playerNum; ++i) {
            if (playerConnectionStatus.get(i) == null || playerConnectionStatus.get(i) == false) {
                continue;
            }
            allAttack.addAll(phs.get(i).getPlayerAttackOrders());
        }
        groupAttackOrdersByDesTerritory(allAttack, attackOrdersGroupByTerritory);
        System.out.println("================begin execute Attack Order =========");
        executeAttackOrder(allAttack);
        resolveAttackOrder(attackOrdersGroupByTerritory);

        return groupAttackOrdersByPlayers(attackOrdersGroupByTerritory);
    }

    /**
     * Group attack orders by player ids
     * 
     * @param attackOrdersGroupByTerritory attack orders group by territories
     * @return attack orders group by player id
     */
    protected HashMap<Integer, ArrayList<AttackOrder>> groupAttackOrdersByPlayers(
            HashMap<String, ArrayList<AttackOrder>> attackOrdersGroupByTerritory) {
        HashMap<Integer, ArrayList<AttackOrder>> attackOrdersGroupByPlayers = new HashMap<>();
        for (String terr : attackOrdersGroupByTerritory.keySet()) {
            ArrayList<AttackOrder> terrAttackOrders = attackOrdersGroupByTerritory.get(terr);
            for (AttackOrder ao : terrAttackOrders) {
                String playerName = ao.getPlayerName();
                int playerID = getPlayerNumByName(playerName);
                if (attackOrdersGroupByPlayers.containsKey(playerID)) {
                    attackOrdersGroupByPlayers.get(playerID).add(ao);
                } else {
                    attackOrdersGroupByPlayers.put(playerID, new ArrayList<>());
                }
            }
        }
        return attackOrdersGroupByPlayers;
    }

    /**
     * Get player's id accroding to their name
     * 
     * @param playerName player name
     * @return player id
     */
    protected int getPlayerNumByName(String playerName) {
        for (int i = 0; i < playerNames.size(); ++i) {
            if (playerName.equals(playerNames.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Group attack orders by destination territory
     * 
     * @param attackOrders                 one player's attackOrders in one turn
     * @param attackOrdersGroupByTerritory the result I want to change
     */
    private void groupAttackOrdersByDesTerritory(ArrayList<AttackOrder> attackOrders,
            HashMap<String, ArrayList<AttackOrder>> attackOrdersGroupByTerritory) {
        for (AttackOrder attackOrder : attackOrders) {
            String destinationTerr = attackOrder.getDestinationName();
            // String destinationTerr = "Hardcode";
            ArrayList<AttackOrder> terrAtkOrders = new ArrayList<>();
            // if exists
            if (attackOrdersGroupByTerritory.containsKey(destinationTerr)) {
                terrAtkOrders = attackOrdersGroupByTerritory.get(destinationTerr);
            }
            terrAtkOrders.add(attackOrder);
            attackOrdersGroupByTerritory.put(destinationTerr, terrAtkOrders);
        }
    }

    // todo merge attack order belong to same player
    public void resolveAttackOrder(HashMap<String, ArrayList<AttackOrder>> attackOrderByTerris) {
        System.out.println("============resolve attack order================");
        HashMap<String, ArrayList<AttackOrder>> mergeSamePlayerOrders = mergeSamePlayers(attackOrderByTerris);
        for (String terriName : mergeSamePlayerOrders.keySet()) {
            System.out.println(terriName + "============begin fight================");
            beginFight(riskMap.getTerritoryByName(terriName), attackOrderByTerris.get(terriName));
        }
    }

    protected void beginFight(Territory fightingTerri, ArrayList<AttackOrder> fightOrders) {
        if (fightOrders.isEmpty()) {
            return;
        }
        AttackOrder defenseOrder = new AttackOrder(fightingTerri.getName(), fightingTerri.getName(),
                fightingTerri.getUnitNum(UnitType.SOLDIER), UnitType.SOLDIER, fightingTerri.getOwner().getName());
        defenseOrder.execute(riskMap);
        System.out.println("=====check defense unit amout: " + fightingTerri.getUnitNum(UnitType.SOLDIER));
        fightOrders.add(defenseOrder);
        ArrayList<Boolean> check = new ArrayList<>();
        for (int i = 0; i < fightOrders.size(); ++i) {
            check.add(true);
        }
        while (checkWin(check)) {
            for (int i = 0; i < fightOrders.size(); i++) {
                if (checkWin(check)) {
                    break;
                }
                if (!check.get(i)) {
                    continue;
                }
                if (fightOrders.get(i).getNumber() == 0) {
                    // fightOrders.remove(i);
                    check.set(i, false);
                    continue;
                }
                int another = 0;
                for (int j = i; j < fightOrders.size(); j++) {
                    if (!check.get(j)) {
                        j++;
                    } else {
                        another = j;
                        break;
                    }
                }
                AttackOrder loserForThisRound = (rollDice()) ? fightOrders.get(i) : fightOrders.get(another);

                System.out.println("loser: " + loserForThisRound.getSourceName());
                loserForThisRound.loseOneUnit();
                System.out.println("unit after lose: " + loserForThisRound.getNumber());
                if (loserForThisRound.getNumber() == 0) {
                    // fightOrders.remove(loserForThisRound);
                    check.set(fightOrders.indexOf(loserForThisRound), false);
                }
            }
        }
        System.out.println("winner: " + fightOrders.get(0).getPlayerName());
        resolveWinnerForThisRound(fightOrders.get(0), fightingTerri);
    }

    protected boolean checkWin(ArrayList<Boolean> check) {
        int count = 0;
        for (int i = 0; i < check.size(); ++i) {
            if (check.get(i)) {
                count++;
            }
        }
        System.out.println("count of check: " + count);
        return count == 1;
    }

    protected void resolveWinnerForThisRound(AttackOrder winOrder, Territory fightingTerri) {
        fightingTerri.getOwner().loseTerritory(fightingTerri);
        Player winner = riskMap.getTerritoryByName(winOrder.getSourceName()).getOwner();
        fightingTerri.setOwner(winner);
        System.out.println("new owner: " + fightingTerri.getOwner().getName());
        fightingTerri.updateUnitCount(UnitType.SOLDIER, false, winOrder.getNumber());
        System.out.println("new unit: " + fightingTerri.getUnitNum(UnitType.SOLDIER));
        winner.addTerritory(fightingTerri);
    }

    protected boolean rollDice() {
        Random rand = new Random();
        int x = rand.nextInt(20);
        int y = rand.nextInt(20);
        return x > y;
    }
}
