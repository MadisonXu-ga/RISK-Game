package edu.duke.ece651.team5.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Territory {
    private int id;
    private String name;
    private String owner;
    private int troops;

    @JsonCreator
    public Territory(@JsonProperty("id") int id,
                     @JsonProperty("name") String name,
                     @JsonProperty("owner") String owner,
                     @JsonProperty("troops") int troops) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.troops = troops;
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

    public int getTroops() {
        return troops;
    }
}
