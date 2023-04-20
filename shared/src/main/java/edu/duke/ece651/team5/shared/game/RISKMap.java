package edu.duke.ece651.team5.shared.game;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;

/**
 * this class handle functionality of a map
 */

public class RISKMap implements Serializable{
    private Map<String, Territory> territories;

    private HashMap<Integer, List<Edge>> connections;

    /**
     * Inner class to represent an edge
     */
    public static class Edge implements Serializable{
        private int from;
        private int to;
        private int distance;

        public Edge(int from,
                int to,
                int distance) {
            this.from = from;
            this.to = to;
            this.distance = distance;
        }

        public int getFrom(){
            return from;
        }

        public int getTo(){
            return to;
        }
        public int getDistance(){
            return distance;
        }
    }

    /**
     * default constructor
     */
    public RISKMap() {
        this("map_config.txt");
    }

    /**
     * Constructor to initialize the map from a config file
     * @param fileName file name of the config file
     */
    public RISKMap(String fileName) {
        territories = new HashMap<>();
        connections = new HashMap<>();
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        initMapFromConfigFile(inputStream);
    }

    /**
     * Helper function to initialize the map
     * @param inputStream input stream indicates where to read from
     */
    private void initMapFromConfigFile(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            line = reader.readLine();
            String[] names = line.split(", ");
            int id = 0;
            for (String name : names) {
                Territory t = new Territory(id, name);
                territories.put(name, t);
                id++;
            }

            while ((line = reader.readLine()) != null) {
                // parse line
                String[] split = line.split(": ");
                String territoryName = split[0];
                String connectionNames = split[1];
                String[] connectionNameArray = connectionNames.split(", ");
                addConnection(territoryName, Arrays.asList(connectionNameArray));
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RISKMap getDeepCopy() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(this);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        RISKMap copy = (RISKMap) ois.readObject();
        ois.close();

        return copy;
    }

    /**
     * Helper function to add an edge
     * @param name a territory name
     * @param names names all the neighbours' name of this territory
     */
    private void addConnection(String name, List<String> names) {
        List<Edge> edgeInfo = new ArrayList<>();
        Territory t = getTerritoryByName(name);
        int from_id = t.getId();
        for (String n : names) {
            Territory adjTerritory = getTerritoryByName(n);
            int to_id = adjTerritory.getId();
            edgeInfo.add(new Edge(from_id, to_id, 1));
        }
        connections.put(from_id, edgeInfo);
    }

    /**
     * Get a certain territory by its name
     * @param name the name of the territory
     * @return the territory with this name
     */

    public Territory getTerritoryByName(String name) {
        return territories.get(name);
    }

    /**
     * Get a certain territory by its id
     * @param id the name of the territory
     * @return the territory with this id
     */
    public Territory getTerritoryById(int id) {
        return territories.values()
                .stream()
                .filter(territory -> territory.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Map<String, Territory> getAllTerritories(){
        return territories;
    }

    /**
     * get list of neighbors territory for a given territoryId
     * @param targetTerritoryId target Territory to find neighbors
     * @param getAllNeighbors true to get all neighbors, false to only get territories not owned by player
     * @param player player who own the target territory
     * @return a list of territories who are neighbors of the target territory
     */
    public List<Territory> getNeighbors(int targetTerritoryId, boolean getAllNeighbors, Player player){
        return connections.get(targetTerritoryId).stream()
        .map(Edge::getTo)
        .map(this::getTerritoryById)
        .filter(t -> getAllNeighbors || !t.getOwner().equals(player))
        .collect(Collectors.toList());
    }

    /**
     * Constructor with all params
     * usually should not call this unless it is a test
     * @param territories map from territory name to territory object
     * @param connections the connections between them
     */
    public RISKMap(Map<String, Territory> territories,
            HashMap<Integer, List<Edge>> connections) {
        this.territories = territories;
        this.connections = connections;
    }

    /**
     * To tell if two territories are adjacent
     * @param srcTerri one territory
     * @param destTerry the other
     * @return true of they are adjacent, otherwise false
     */

    public boolean isAdjacent(Territory srcTerri, Territory destTerry) {
        int srcId = srcTerri.getId();
        int destId = destTerry.getId();
        return connections.get(srcId).stream().anyMatch(edge -> edge.to == destId);
    }

    /**
     * find nearest neigher whose territory is owned by target player 
     * @param startTerritory player
     * @param player target Player
     * @return nearest territory
     */
    public Territory findNearestNeighbor(Territory startTerritory, Player player) {
        // Create a map to keep track of the shortest distances to each territory
        Map<Territory, Integer> distance = new HashMap<>();
        for (Territory territory : territories.values()) {
            distance.put(territory, Integer.MAX_VALUE);
        }
        distance.put(startTerritory, 0);
    
        // Create a priority queue to store the territories by their distance from the start territory
        PriorityQueue<Territory> queue = new PriorityQueue<>(Comparator.comparingInt(distance::get));
        queue.add(startTerritory);
    
        // Perform Dijkstra's algorithm until we find a neighbor owned by the player
        while (!queue.isEmpty()) {
            Territory currentTerritory = queue.poll();
            int currentDistance = distance.get(currentTerritory);
    
            // Check if the current territory is owned by the player
            if (currentTerritory.getOwner() == player) {
                return currentTerritory;
            }
    
            // Update the distances to all adjacent territories
            for (Edge edge : connections.get(currentTerritory.getId())) {
                int neighborId = edge.getTo();
                Territory neighborTerritory = getTerritoryById(neighborId);
                int neighborDistance = currentDistance + edge.getDistance();
                if (neighborDistance < distance.get(neighborTerritory)) {
                    distance.put(neighborTerritory, neighborDistance);
                    queue.remove(neighborTerritory);
                    queue.add(neighborTerritory);
                }
            }
        }
    
        // If no neighbor owned by the player was found, return null
        return null;
    }
    
    
    
    

    /**
     * Get the shortest distance between two territories owned by the same player
     * @param sourceName name of the source territory
     * @param destName name of the dest territory
     * @return the distance between them, if there is no such a path return Integer.MAX_VALUE
     */
    public int getShortestPathDistance(String sourceName, String destName, boolean hasSameOwner) {
        Territory source = getTerritoryByName(sourceName);
        Territory dest = getTerritoryByName(destName);

        Map<Territory, Integer> distances = new HashMap<>();
        Set<Territory> visited = new HashSet<>();
        for (Territory territory : territories.values()) {
            distances.put(territory, Integer.MAX_VALUE);
        }
        distances.put(source, 0);

        PriorityQueue<Map.Entry<Territory, Integer>> pq = new PriorityQueue<>((a, b) -> a.getValue() - b.getValue());
        pq.offer(new AbstractMap.SimpleEntry<>(source, 0));

        while (!visited.contains(dest) && !pq.isEmpty()) {
            Territory current = pq.poll().getKey();

            List<Edge> edges = connections.get(current.getId());
            if (edges != null) {
                for (Edge edge : edges) {
                    int neighbourId = edge.to;
                    Territory neighbor = getTerritoryById(neighbourId);
                    // if (hasSameOwner 
                    // || (!source.getOwner().equals(neighbor.getOwner()))
                    // || !(dest.getOwner().equals(neighbor.getOwner()) || source.getOwner().containsAlliance(neighbor.getOwner()))
                        
                    //         ) {
                    //     continue; // skip neighbors with different owners
                    // }
                    if (hasSameOwner && (!source.getOwner().equals(neighbor.getOwner())
                            || (!dest.getOwner().equals(neighbor.getOwner()) && !dest.getOwner().containsAlliance(neighbor.getOwner()))
                            )) {
                        continue; // skip neighbors with different owners
                    }

                    int newDist = distances.get(current) + edge.distance;
                    if (newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        pq.offer(new AbstractMap.SimpleEntry<>(neighbor, newDist));
                    }
                }
            }

            visited.add(current);
        }

        return distances.get(dest);
    }


    /**
     * for debugging
     */
    public void printMap() {
        for (Map.Entry<String, Territory> terri : territories.entrySet()) {
            System.out.println(terri.getKey());
            List<Edge> edges = connections.get(terri.getValue().getId());
            for (Edge e : edges) {
                System.out.println(
                        "from " + terri.getKey() + " to " + getTerritoryById(e.to).getName() + " : " + e.distance);
            }
        }
    }

}
