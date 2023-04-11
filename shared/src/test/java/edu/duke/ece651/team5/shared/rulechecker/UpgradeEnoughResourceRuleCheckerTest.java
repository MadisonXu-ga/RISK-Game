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
import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;

public class UpgradeEnoughResourceRuleCheckerTest {
    private UpgradeEnoughResourceRuleChecker ruleChecker;
    private UpgradeOrder upgradeOrder;

    @BeforeEach
    public void setUp() {
        ruleChecker = new UpgradeEnoughResourceRuleChecker(null); 
        Soldier soldier1 = new Soldier(SoldierLevel.CAVALRY);
        Map<Pair<Soldier, Integer>, SoldierLevel> soldierToUpgrade = new HashMap<>();
        soldierToUpgrade.put(new Pair<>(soldier1, 1), SoldierLevel.AIRBORNE);
        upgradeOrder = new UpgradeOrder("A", soldierToUpgrade, new Player("Player 1"));
    }

    @Test
    public void testCheckMyRuleWithEnoughResources() {
        Player player = upgradeOrder.getPlayer();
        Resource technologyResource = new Resource(ResourceType.TECHNOLOGY);
        player.getResourceToAmount().put(technologyResource, 100);
        String result = ruleChecker.checkMyRule(upgradeOrder);

        assertNull(result);
    }

    @Test
    public void testCheckMyRuleWithInsufficientResources() {
        Player player = upgradeOrder.getPlayer();
        Resource technologyResource = new Resource(ResourceType.TECHNOLOGY);
        player.getResourceToAmount().put(technologyResource, 5);
        String result = ruleChecker.checkMyRule(upgradeOrder);
        assertEquals("You do not have enough technology resources.", result);
    }

}
