package edu.duke.ece651.team5.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


public class Territory {
    private int id;
    private String name;
    private String owner;
    @JsonSerialize(using = SoldierArmySerializer.class)
    private SoldierArmy soldierArmy;


    @JsonCreator
    public Territory(@JsonProperty("id") int id,
                     @JsonProperty("name") String name,
                     @JsonProperty("owner") String owner,
                     @JsonProperty("soldierArmy") SoldierArmy soldierArmy) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.soldierArmy = soldierArmy;
    }

    public Territory(int id, String name, String owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.soldierArmy = new SoldierArmy();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public SoldierArmy getSoldierArmy() {
        return soldierArmy;
    }
}
