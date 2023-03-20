package edu.duke.ece651.team5.shared;

import java.io.Serializable;
import java.util.*;

public class RISKMap implements Serializable {
    private static final long serialVersionUID = 3107749286550437606L;
    private final ArrayList<Territory> territories;
    private ArrayList<Player> players;
    private final HashMap<Territory, HashSet<Territory>> connection;

    public RISKMap(){
        this(null);
    }
    
    //public RISKMap(int numPlayers) {
    public RISKMap(ArrayList<Player> players) {
        territories = new ArrayList<>();
        connection = new HashMap<>();
        initMap();
        //todo Init Player here 
        /*
         * create 4 default playrs
         * then init Players and assign territories according to the actual player num input
         */
        this.players = players;
    }

    public void initPlayers(ArrayList<Player> players){
        this.players = players;
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }

    public ArrayList<Territory> getTerritories(){
        return territories;
    }

    private void initMap() {
        ArrayList<Territory> territoryList = getTerritoryList();
        territories.addAll(territoryList);
        initConnection();
    }

    // todo: this is just for convenience, might refactor it later
    // I might wanna read this from json
    private ArrayList<Territory> getTerritoryList() {
        return new ArrayList<>(Arrays.asList(
        initTerritory("Narnia", UnitType.SOLDIER, 10),
        initTerritory("Elantris", UnitType.SOLDIER, 6),
        initTerritory("Midkemia", UnitType.SOLDIER, 12),
        initTerritory("Scadrial", UnitType.SOLDIER, 5),
        initTerritory("Roshar", UnitType.SOLDIER,3),
        initTerritory("Oz", UnitType.SOLDIER, 8),
        initTerritory("Gondor", UnitType.SOLDIER, 13),
        initTerritory("Mordor", UnitType.SOLDIER, 14),
        initTerritory("Hogwarts", UnitType.SOLDIER, 3)
        ));
    }

    private Territory initTerritory(String name, Unit unit, int num) {
        HashMap<Unit, Integer> units = new HashMap<>();
        units.put(unit, num);
        return new Territory(name, units);
    }

    public Territory getTerritoryByName(String name) {
        for (Territory t : territories) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Does not exist territory " + name);
    }

    public Player getPlayerByName(String name) {
        for (Player p : players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Does not exist Player " + name);
    }

    // todo: add more
    private void initConnection() {
        addConnection("Narnia", Arrays.asList("Elantris", "Midkemia"));
        addConnection("Elantris", Arrays.asList("Narnia", "Midkemia", "Scadrial", "Roshar"));
        addConnection("Midkemia", Arrays.asList("Elantris", "Narnia", "Scadrial", "Oz"));
        addConnection("Scadrial", Arrays.asList("Elantris", "Narnia", "Midkemia", "Roshar", "Oz", "Mordor", "Hogwarts"));
        addConnection("Oz", Arrays.asList("Midkemia", "Scadrial", "Mordor", "Gondor"));
        addConnection("Roshar", Arrays.asList("Elantris", "Scadrial", "Hogwarts"));
        addConnection("Gondor", Arrays.asList("Oz", "Mordor"));
    }

    private void addConnection(String name, List<String> names) {
        Territory t = getTerritoryByName(name);

        HashSet<Territory> adjTerritories = new HashSet<>();
        for (String n : names) {
            Territory adjTerritory = getTerritoryByName(n);
            adjTerritories.add(adjTerritory);
        }

        connection.put(t, adjTerritories);
    }

    public boolean isAdjacent(Territory t1, Territory t2) {
        return connection.get(t1).contains(t2);
    }

    public HashSet<Territory> getAdjacentTerritories(Territory t) {
        return connection.get(t);
    }

    public boolean hasPathWithSameOwner(Territory source, Territory destination) {
        Player owner = source.getOwner();
        Set<Territory> visited = new HashSet<>();
        Queue<Territory> queue = new LinkedList<>();
        visited.add(source);
        queue.add(source);

        while (!queue.isEmpty()) {
            Territory curr = queue.remove();
            if (curr.getOwner().equals(owner)) {
                if (curr == destination) {
                    return true;
                }
                for (Territory neighbor : connection.get(curr)) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }

        return false;
    }

    /*
    for each territory we need to do the folowing:
    - iterate through all the players sequentially
        - assign territories to the player
    - each assigned territory needs to have a new "owner" 
    !right now it is assigned by going through the list of territories
    TODO see if we want to get players to choose them
    */
    public void assignTerritories(){

        Iterator<Territory> it = territories.iterator();


        Integer ctr = 0;
        while(it.hasNext()){

            Territory currentTerritory = it.next();
            Player currentPlayer = players.get(ctr % players.size());
            

            chooseTerritory(currentTerritory, currentPlayer);
            ctr++;

        }
    }

    public boolean chooseTerritory(Territory aTerritory, Player possibleOwner){

        if(aTerritory.hasOwner()){
            return false;
        }

        aTerritory.setOwner(possibleOwner);
        possibleOwner.addTerritory(aTerritory);
        return true;

    }
}