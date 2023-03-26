package edu.duke.ece651.team5.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.*;

public class RISKMap {

    private Map<String, Territory> territories;
    private HashMap<Integer, List<Edge>> connections;

    public static class Edge {
        private int from;
        private int to;
        private int distance;

        @JsonCreator
        public Edge(@JsonProperty("from") int from,
                    @JsonProperty("to") int to,
                    @JsonProperty("distance") int distance) {
            this.from = from;
            this.to = to;
            this.distance = distance;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }

        public int getDistance() {
            return distance;
        }

        // getters and setters

        public void setFrom(int from) {
            this.from = from;
        }

        public void setTo(int to) {
            this.to = to;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }
    }


    @JsonCreator
    public RISKMap(@JsonProperty("territories") Map<String, Territory> territories,
                   @JsonProperty("connections") HashMap<Integer, List<Edge>> connections) {
        this.territories = territories;
        this.connections = connections;
    }


    // getters and setters
    public Map<String, Territory> getTerritories() {
        return territories;
    }

    public void setTerritories(Map<String, Territory> territories) {
        this.territories = territories;
    }

    public HashMap<Integer, List<Edge>> getConnections() {
        return connections;
    }

    public void setConnections(HashMap<Integer, List<Edge>> connections) {
        this.connections = connections;
    }

    public static void main(String[] args) {
        Map<String, Territory> territories = new HashMap<>();
        territories.put("Territory 1", new Territory(1, "Territory 1", "Player 1"));
        territories.put("Territory 2", new Territory(2, "Territory 2", null));

        HashMap<Integer, List<Edge>> connections = new HashMap<>();
        connections.put(1,
                new ArrayList<>(
                        Arrays.asList(
                        new Edge(1, 2, 5),
                        new Edge(1, 3, 8))));
        connections.put(2, new ArrayList<>());
        connections.put(3, new ArrayList<>());

        RISKMap map = new RISKMap(territories, connections);

        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addSerializer(SoldierArmy.class, new SoldierArmySerializer());
            mapper.registerModule(module);
            String json = mapper.writeValueAsString(map);
            System.out.println(json);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
