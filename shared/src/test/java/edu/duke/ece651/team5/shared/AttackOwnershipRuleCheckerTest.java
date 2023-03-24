package edu.duke.ece651.team5.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttackOwnershipRuleCheckerTest {

    @Test
    void checkMyRule() {
        RISKMap map = new RISKMap();
        Territory elantris = map.getTerritoryByName("Elantris");
        Territory narnia = map.getTerritoryByName("Narnia");
        Territory oz = map.getTerritoryByName("Oz");

        Player blue = new Player("blue");
        Player green = new Player("green");

        elantris.setOwner(blue);
        narnia.setOwner(green);
        oz.setOwner(green);

        AttackOrder attack1 = new AttackOrder("Elantris", "Narnia", 5, UnitType.SOLDIER, "Green");
        AttackOrder attack2 = new AttackOrder("Narnia", "Oz", 3, UnitType.SOLDIER, "Green");

        AttackOwnershipRuleChecker attackOwnershipRuleChecker = new AttackOwnershipRuleChecker(null);
        assertNull(attackOwnershipRuleChecker.checkMyRule(attack1, blue, map));

        assertEquals("You cannot attack your own territories",
                attackOwnershipRuleChecker.checkMyRule(attack2, green, map));
    }
}