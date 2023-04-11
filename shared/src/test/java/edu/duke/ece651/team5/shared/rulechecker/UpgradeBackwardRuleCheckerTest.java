package edu.duke.ece651.team5.shared.rulechecker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.datastructure.Pair;
import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.order.UpgradeOrder;
import edu.duke.ece651.team5.shared.resource.*;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;

public class UpgradeBackwardRuleCheckerTest {
    private UpgradeBackwardRuleChecker ruleChecker;
    private UpgradeOrder upgradeOrder;
    private UpgradeOrder upgradeOrder2;

    @BeforeEach
    public void setUp() {
        ruleChecker = new UpgradeBackwardRuleChecker(null); 
        Soldier soldier1 = new Soldier(SoldierLevel.CAVALRY);
        Map<Pair<Soldier, Integer>, SoldierLevel> soldierToUpgrade = new HashMap<>();
        soldierToUpgrade.put(new Pair<>(soldier1, 1), SoldierLevel.AIRBORNE);
        upgradeOrder = new UpgradeOrder("A", soldierToUpgrade, new Player("Player 1"));

        Map<Pair<Soldier, Integer>, SoldierLevel> soldierToUpgrade2 = new HashMap<>();
        soldierToUpgrade2.put(new Pair<>(soldier1, 1), SoldierLevel.CAVALRY);
        upgradeOrder2 = new UpgradeOrder("A", soldierToUpgrade2, new Player("Player 1"));

    }

    @Test
    public void testCheckMyRuleWithUpgradeLevelsHigherThanCurrentLevels() {
        Player player = upgradeOrder.getPlayer();
        player.addResourceFromTerritory(new Resource(ResourceType.TECHNOLOGY), 500); // Set enough resources
        String result = ruleChecker.checkMyRule(upgradeOrder);

        assertNull(result);
    }

    @Test
    public void testCheckMyRuleWithUpgradeLevelsLowerOrEqualThanCurrentLevels() {
        String result = ruleChecker.checkMyRule(upgradeOrder2);
        assertEquals("Cannot upgrade unit level smaller or equal to current soldier level.", result);
    }
}
