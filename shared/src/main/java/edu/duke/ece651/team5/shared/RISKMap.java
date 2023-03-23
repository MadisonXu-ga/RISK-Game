package edu.duke.ece651.team5.shared;

import java.io.Serializable;
import java.util.*;

public class RISKMap implements Serializable {

    private static final long serialVersionUID = 3107749286550437606L;
    private final ArrayList<Territory> territories;
    private ArrayList<Player> players;
    public final HashMap<Territory, HashSet<Territory>> connection;

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

    /**
     * Helper function to init a map
     */
    private void initMap() {
        ArrayList<Territory> territoryList = getTerritoryList();
        territories.addAll(territoryList);
        initConnection();
    }

    // todo: this is just for convenience, might refactor it later
    // I might wanna read this from json

    /**
     * Helper function to init a list of territories
     * to be added to the map
     * @return a list of territories with necessary info
     */
    private ArrayList<Territory> getTerritoryList() {
        return new ArrayList<>(Arrays.asList(
            initTerritory("A", UnitType.SOLDIER, 0),
            initTerritory("B", UnitType.SOLDIER, 0),
            initTerritory("C", UnitType.SOLDIER, 0)
   
            ));
        // return new ArrayList<>(Arrays.asList(
        // initTerritory("Narnia", UnitType.SOLDIER, 0),
        // initTerritory("Elantris", UnitType.SOLDIER, 0),
        // initTerritory("Midkemia", UnitType.SOLDIER, 0),
        // initTerritory("Scadrial", UnitType.SOLDIER, 0),
        // initTerritory("Roshar", UnitType.SOLDIER,0),
        // initTerritory("Oz", UnitType.SOLDIER, 0),
        // initTerritory("Gondor", UnitType.SOLDIER, 0),
        // initTerritory("Mordor", UnitType.SOLDIER, 0),
        // initTerritory("Hogwarts", UnitType.SOLDIER, 0)
        // ));
    }

    /**
     * Init a single territory
     * @param name name of the territory
     * @param unit unit type to be placed
     * @param num number of unit
     * @return territory created
     */
    private Territory initTerritory(String name, Unit unit, int num) {
        HashMap<Unit, Integer> units = new HashMap<>();
        units.put(unit, num);
        return new Territory(name, units);
    }

    /**
     * Get a certain territory by its name
     * @param name the name of the territory
     * @return the territory with this name
     */
    public Territory getTerritoryByName(String name) {
        for (Territory t : territories) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Does not exist territory " + name);
    }

    /**
     * Get a certain player by its color
     * @param name color
     * @return the palyer with this color
     */
    public Player getPlayerByName(String name) {
        for (Player p : players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Does not exist Player " + name);
    }

    // todo: add more

    /**
     * Helper function to init all the connections
     */
    // private void initConnection() {
    //     addConnection("Narnia", Arrays.asList("Elantris", "Midkemia"));
    //     addConnection("Elantris", Arrays.asList("Narnia", "Midkemia", "Scadrial", "Roshar"));
    //     addConnection("Midkemia", Arrays.asList("Elantris", "Narnia", "Scadrial", "Oz"));
    //     addConnection("Scadrial", Arrays.asList("Elantris", "Narnia", "Midkemia", "Roshar", "Oz", "Mordor", "Hogwarts"));
    //     addConnection("Oz", Arrays.asList("Midkemia", "Scadrial", "Mordor", "Gondor"));
    //     addConnection("Roshar", Arrays.asList("Elantris", "Scadrial", "Hogwarts"));
    //     addConnection("Gondor", Arrays.asList("Oz", "Mordor"));
    //     addConnection("Mordor", Arrays.asList("Gondor", "Oz","Scadrial","Hogwarts" ));
    //     addConnection("Hogwarts", Arrays.asList("Mordor", "Scadrial", "Roshar" ));
    // }

    private void initConnection() {
        addConnection("A", Arrays.asList("B", "C"));
        addConnection("B", Arrays.asList("A"));
        addConnection("C", Arrays.asList("B"));
  
    }

    /**
     * Helper function to initialize the connections between territories
     * @param name territory name
     * @param names all the neighbours' name of this territory
     */
    private void addConnection(String name, List<String> names) {
        Territory t = getTerritoryByName(name);

        HashSet<Territory> adjTerritories = new HashSet<>();
        for (String n : names) {
            Territory adjTerritory = getTerritoryByName(n);
            adjTerritories.add(adjTerritory);
        }

        connection.put(t, adjTerritories);
    }

    /**
     * To tell if two territories are adjacent
     * @param t1 one territory
     * @param t2 the other
     * @return true of they are adjacent, otherwise false
     */
    public boolean isAdjacent(Territory t1, Territory t2) {
        return connection.get(t1).contains(t2);
    }

    /**
     * Get all adjacent territories for a certain territory
     * @param t any territory
     * @return the set of adjacent territories
     */
    public HashSet<Territory> getAdjacentTerritories(Territory t) {
        if (connection.containsKey(t)) {
            return connection.get(t);
        } else {
            System.out.println(t.toString() + " " + t.hashCode());
            return null;
        }
    }

    /**
     * Using BFS to tell if there is a path from source to dest
     * where all the passing territories all belong to
     * the owner of source and dest
     * @param source start of the path
     * @param destination end of the path
     * @return true if exist such a path, otherwise false
     */
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

    //TODO move this to a separate class in the following way:
    /*
     * create an interface called assignTerritories
     * then a subclass called pre-assgined with the methods below
     * gamecontroller -> collects assigning, rules, validating, issue an order
     * 
     */
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