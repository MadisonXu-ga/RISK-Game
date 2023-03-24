package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MovePathWithSameOwnerRuleCheckerTest {

    @Test
    void checkMyRule() {
        Player blue = new Player("Blue");
        Player green = new Player("Green");
        RISKMap map = new RISKMap("test_map_config.txt");
        ArrayList<Player> players = new ArrayList<>();
        players.add(blue);
        players.add(green);
        map.initPlayers(players);
        ArrayList<Territory> territories = map.getTerritories();
        for (int i = 0; i < territories.size(); i++) {
            Territory territory = territories.get(i);
            territory.setOwner(blue);
            blue.addTerritory(territory);
        }
        for (int i = territories.size() / 2; i < territories.size(); i++) {
            Territory territory = territories.get(i);
            territory.setOwner(green);
            green.addTerritory(territory);
        }

        MoveOrder moveOrder1 = new MoveOrder("Narnia", "Elantris",
                10, UnitType.SOLDIER, "Blue");
        MoveOrder moveOrder2 = new MoveOrder("Narnia", "Oz",
                10, UnitType.SOLDIER, "Blue");

        MovePathWithSameOwnerRuleChecker movePathWithSameOwnerRuleChecker = new MovePathWithSameOwnerRuleChecker(null);
        assertNull(movePathWithSameOwnerRuleChecker.checkMyRule(moveOrder1, blue, map));
        assertEquals("There is no such a path from Narnia to Oz owned by Blue",
                movePathWithSameOwnerRuleChecker.checkMyRule(moveOrder2, blue, map));
    }
}