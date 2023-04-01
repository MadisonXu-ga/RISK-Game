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
    }

    public Territory getTerritoryByName(String name){
        return territories.get(name);
    }




    public RISKMap(Map<String, Territory> territories,
                   HashMap<Integer, List<Edge>> connections) {
        this.territories = territories;
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


    }

}
