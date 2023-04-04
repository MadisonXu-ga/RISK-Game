package edu.duke.ece651.team5.shared;

import java.util.*;

public class RISKMap {
    private Map<String, Territory> territories;
   
    private HashMap<Integer, List<Edge>> connections;

    //todo players field

    public static class Edge {
        private int from;
        private int to;
        private int distance;

        public Edge( int from,
                     int to,
                     int distance) {
            this.from = from;
            this.to = to;
            this.distance = distance;
        }

        public int getTo(){
            return to;
        }
    }


    

    public List<Edge> getConnections(int id){
        return connections.get(id);
    }

    public Territory getTerritoryByName(String name){
        return territories.get(name);
    }

    public Territory getTerritoryById(int id) {
        return territories.values()
                .stream()
                .filter(territory -> territory.getId() == id)
                .findFirst()
                .orElse(null);
    }


    public RISKMap(Map<String, Territory> territories,
                    HashMap<Integer, List<Edge>> connections) {
        this.territories = territories;
        this.connections = connections;
    }

    public boolean isAdjacent(Territory srcTerri, Territory destTerry){
        int srcId = srcTerri.getId();
        int destId = destTerry.getId();
        return connections.get(srcId).stream().anyMatch(edge -> edge.to == destId);
    }


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
                    if (!source.getOwner().equals(neighbor.getOwner()) || !dest.getOwner().equals(neighbor.getOwner())) {
                        continue;  // skip neighbors with different owners
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



}
