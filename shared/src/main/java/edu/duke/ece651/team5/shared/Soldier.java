package edu.duke.ece651.team5.shared;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Soldier {
    private SoldierType type;
    private int level;


    public Soldier(SoldierType type, int level) {
        this.type = type;
        this.level = level;
    }
}
