package edu.duke.ece651.team5.shared.game;

import javax.xml.transform.Source;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class RISKMap {
    private Map<String, Territory> territories;

    private HashMap<Integer, List<Edge>> connections;

    /**
     * Inner class to represent an edge
     */
    public static class Edge {
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
     * Get the shortest distance between two territories owned by the same player
     * @param sourceName name of the source territory
     * @param destName name of the dest territory
     * @return the distance between them, if there is no such a path return Integer.MAX_VALUE
     */
    public int getShortestPathDistance(String sourceName, String destName) {
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
                    if (!source.getOwner().equals(neighbor.getOwner())
                            || !dest.getOwner().equals(neighbor.getOwner())) {
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
