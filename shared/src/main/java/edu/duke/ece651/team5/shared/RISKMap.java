package edu.duke.ece651.team5.shared;

import java.io.Serializable;
import java.util.*;

public class RISKMap implements Serializable {
    private static final long serialVersionUID = 3107749286550437606L;
    private final ArrayList<Territory> territories;
    private final HashMap<Territory, HashSet<Territory>> connection;

    public RISKMap() {
        territories = new ArrayList<>();
        connection = new HashMap<>();
        initMap();
    }

    private void initMap() {
        ArrayList<Territory> territoryList = getTerritoryList();
        territories.addAll(territoryList);
        initConnection();
    }

    //todo: this is just for convenience, might refactor it later
    private ArrayList<Territory> getTerritoryList() {
        return new ArrayList<>(Arrays.asList(
                new Territory("Narnia"),
                new Territory("Elantris"),
                new Territory("Midkemia"),
                new Territory("Scadrial"),
                new Territory("Roshar"),
                new Territory("Oz"),
                new Territory("Gondor"),
                new Territory("Mordor"),
                new Territory("Hogwarts")
            )
        );
    }

    public Territory getTerritoryByName(String name) {
        for (Territory t : territories) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Does not exist territory " + name);
    }

    // todo: add more
    private void initConnection() {
        addConnection("Narnia", Arrays.asList("Elantris", "Midkemia"));
        addConnection("Elantris", Arrays.asList("Narnia", "Midkemia", "Scadrial", "Roshar"));
        addConnection("Midkemia", Arrays.asList("Elantris", "Narnia", "Scadrial", "Oz"));
        addConnection("Scadrial", Arrays.asList("Elantris", "Narnia", "Midkemia", "Roshar", "Oz", "Mordor", "Hogwarts"));
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

    public boolean isConnected(Territory t1, Territory t2) {
        return connection.get(t1).contains(t2);
    }
}