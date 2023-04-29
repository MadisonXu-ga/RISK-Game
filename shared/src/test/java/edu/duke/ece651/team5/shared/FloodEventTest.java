package edu.duke.ece651.team5.shared;

import edu.duke.ece651.team5.shared.event.FloodEvent;
import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FloodEventTest {

    RISKMap map;
    List<Territory> territories;
    Territory oz;
    Territory roshar;
    @BeforeEach
    void setUp() {
        map = new RISKMap();
        territories = new ArrayList<>();
        territories.add(map.getTerritoryByName("Oz"));
        territories.add(map.getTerritoryByName("Mordor"));
        territories.add(map.getTerritoryByName("Roshar"));

        oz = map.getTerritoryByName("Oz");
        oz.getSoldierArmy().addSoldier(new Soldier(SoldierLevel.INFANTRY), 5);

        roshar = map.getTerritoryByName("Roshar");
        roshar.getSoldierArmy().addSoldier(new Soldier(SoldierLevel.INFANTRY), 1);
        roshar.getSoldierArmy().addSoldier(new Soldier(SoldierLevel.CAVALRY), 3);
        roshar.getSoldierArmy().addSoldier(new Soldier(SoldierLevel.ARTILLERY), 4);
    }

    @Test
    void execute() {
        FloodEvent floodEvent = new FloodEvent(map);
        floodEvent.setSelectedTerritories(territories);
        floodEvent.execute(map);
        assertEquals(4, oz.getSoldierArmy().getSoldierCount(new Soldier(SoldierLevel.INFANTRY)));
        assertEquals(3, roshar.getSoldierArmy().getSoldierCount(new Soldier(SoldierLevel.INFANTRY)));
        assertEquals(3, roshar.getSoldierArmy().getSoldierCount(new Soldier(SoldierLevel.CAVALRY)));
    }

}