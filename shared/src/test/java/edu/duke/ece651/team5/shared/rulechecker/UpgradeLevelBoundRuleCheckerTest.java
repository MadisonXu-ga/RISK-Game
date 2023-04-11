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
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;

public class UpgradeLevelBoundRuleCheckerTest {
    private UpgradeLevelBoundRuleChecker ruleChecker;
    private UpgradeOrder upgradeOrder;

    @BeforeEach
    public void setUp() {
        ruleChecker = new UpgradeLevelBoundRuleChecker(null); 
        Soldier soldier1 = new Soldier(SoldierLevel.CAVALRY);
        Map<Pair<Soldier, Integer>, SoldierLevel> soldierToUpgrade = new HashMap<>();
        soldierToUpgrade.put(new Pair<>(soldier1, 1), SoldierLevel.AIRBORNE);
        upgradeOrder = new UpgradeOrder("A", soldierToUpgrade, new Player("Player 1"));
    }

    @Test
    public void testCheckMyRuleWithValidUpgradeLevels() {
        Player player = upgradeOrder.getPlayer();
        player.setCurrTechnologyLevel(3); 
        String result = ruleChecker.checkMyRule(upgradeOrder);
        assertNull(result);
    }

    @Test
    public void testCheckMyRuleWithInvalidUpgradeLevels() {
        Player player = upgradeOrder.getPlayer();
        player.setCurrTechnologyLevel(1);
        String result = ruleChecker.checkMyRule(upgradeOrder);
        assertEquals("Cannot upgrade unit level larger than current technology level.", result);
    }

}
