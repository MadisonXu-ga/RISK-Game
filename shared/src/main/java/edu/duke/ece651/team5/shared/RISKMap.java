package edu.duke.ece651.team5.shared;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RISKMap implements Serializable {
    @Serial
    private static final long serialVersionUID = 3107749286550437606L;
    private final ArrayList<Territory> territories;
    private ArrayList<Player> players;
    private final HashMap<Territory, HashSet<Territory>> connection;
    private final int availableUnit;

    public RISKMap(){
        territories = new ArrayList<>();
        connection = new HashMap<>();
        initMapFromConfigFile("map_config.txt");
        this.availableUnit = 50;
    }

    public RISKMap(String fileName) {
        territories = new ArrayList<>();
        connection = new HashMap<>();
        initMapFromConfigFile(fileName);
        this.availableUnit = 50;
    }

    public RISKMap(ArrayList<Player> players) {
        this("map_config.txt");
        this.players = players;
    }

    public void initPlayers(ArrayList<Player> players){
        this.players = players;
    }

    public int getAvailableUnit(){
        return availableUnit;
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }

    public ArrayList<Territory> getTerritories(){
        return territories;
    }

    private void initMapFromConfigFile(String fileName) {
        InputStream inputStream =
                getClass().getClassLoader().getResourceAsStream(fileName);
        //String fileName = "shared/src/test/resources/map_config.txt";
        ArrayList<Territory> territoryList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            line = reader.readLine();
            String[] names = line.split(", ");
            for (String name: names) {
                Territory t = new Territory(name);
                territoryList.add(t);
            }
            territories.addAll(territoryList);

            while ((line = reader.readLine()) != null) {
                // parse line
                String[] split = line.split(": ");
                String territoryName = split[0];
                String connectionNames = split[1];
                String[] connectionNameArray = connectionNames.split(", ");
                addConnection(territoryName, Arrays.asList(connectionNameArray));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        return connection.get(t);
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
}