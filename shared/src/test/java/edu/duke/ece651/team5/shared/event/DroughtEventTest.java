package edu.duke.ece651.team5.shared.event;

import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DroughtEventTest {
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
    }

    @Test
    void execute() {
        DroughtEvent droughtEvent = new DroughtEvent(map);
        droughtEvent.setSelectedTerritories(territories);
        droughtEvent.getSelectedTerritories();
        droughtEvent.execute(map);
        assertEquals(4, oz.getSoldierArmy().getSoldierCount(new Soldier(SoldierLevel.INFANTRY)));
        assertEquals(1, roshar.getSoldierArmy().getSoldierCount(new Soldier(SoldierLevel.INFANTRY)));
        assertEquals(2, roshar.getSoldierArmy().getSoldierCount(new Soldier(SoldierLevel.CAVALRY)));
    }
}