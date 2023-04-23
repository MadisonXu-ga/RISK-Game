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
import edu.duke.ece651.team5.shared.unit.*;

public class UpgradeOrderRuleCheckerTest {
    private UpgradeOrderRuleChecker ruleChecker;
    private UpgradeOrder upgradeOrder;

    @BeforeEach
    public void setUp() {
        ruleChecker = new UpgradeBackwardRuleChecker(
                new UpgradeEnoughResourceRuleChecker(new UpgradeLevelBoundRuleChecker(null)));
        Soldier soldier1 = new Soldier(SoldierLevel.CAVALRY);
        Map<Pair<Soldier, Integer>, SoldierLevel> soldierToUpgrade = new HashMap<>();
        soldierToUpgrade.put(new Pair<>(soldier1, 1), SoldierLevel.AIRBORNE);
        upgradeOrder = new UpgradeOrder("A", soldierToUpgrade, new Player("Player 1"));
    }

    @Test
    public void testCheckMyRule() {
        Player player = upgradeOrder.getPlayer();
        player.setCurrTechnologyLevel(5);
        player.addResourceFromTerritory(new Resource(ResourceType.TECHNOLOGY), 500);

        String result = ruleChecker.checkMyRule(upgradeOrder);

        assertNull(result);
        assertNull(ruleChecker.checkOrder(upgradeOrder));
    }

    @Test
    public void testCheckMyRuleWithIncorrectInput() {
        Player player = upgradeOrder.getPlayer();
        player.setCurrTechnologyLevel(2);
        player.addResourceFromTerritory(new Resource(ResourceType.TECHNOLOGY), 0);

        String result = ruleChecker.checkOrder(upgradeOrder);

        assertEquals("You do not have enough technology resources.", result);

        player.addResourceFromTerritory(new Resource(ResourceType.TECHNOLOGY), 20);
        assertEquals("Cannot upgrade unit level larger than current technology level.",
                ruleChecker.checkOrder(upgradeOrder));
    }

}
