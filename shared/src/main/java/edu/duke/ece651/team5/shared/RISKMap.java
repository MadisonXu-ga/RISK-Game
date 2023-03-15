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

    // todo: this is just for convenience, might refactor it later
    // name soldier:num?
    // new soldier kinda weird...
    private ArrayList<Territory> getTerritoryList() {
        return new ArrayList<>(Arrays.asList(
        initTerritory("Narnia", new Soldier(), 10),
        initTerritory("Elantris", new Soldier(), 6),
        initTerritory("Midkemia", new Soldier(), 12),
        initTerritory("Scadrial", new Soldier(), 5),
        initTerritory("Roshar", new Soldier(),3),
        initTerritory("Oz", new Soldier(), 8),
        initTerritory("Gondor", new Soldier(), 13),
        initTerritory("Mordor", new Soldier(), 14),
        initTerritory("Hogwarts", new Soldier(), 3)
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
}