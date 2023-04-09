package edu.duke.ece651.team5.shared.rulechecker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.order.ResearchOrder;
import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;

public class ResearchEnoughResourceRuleCheckerTest {
    private ResearchEnoughResourceRuleChecker ruleChecker;
    private Player player;

    @BeforeEach
    public void setUp() {
        ruleChecker = new ResearchEnoughResourceRuleChecker(null); // No next rule checker, as it is the last in the chain
        player = new Player("Player 1");
    }

    @Test
    public void testCheckMyRuleWithEnoughResources() {
        ResearchOrder order = new ResearchOrder(player);
        player.addResourceFromTerritory(new Resource(ResourceType.TECHNOLOGY), 50);

        String result = ruleChecker.checkMyRule(order);

        assertNull(result);
    }

    @Test
    public void testCheckMyRuleWithInsufficientResources() {
        ResearchOrder order = new ResearchOrder(player);

        player.addResourceFromTerritory(new Resource(ResourceType.TECHNOLOGY), 1);

        String result = ruleChecker.checkMyRule(order);

        assertEquals("You do not have enough technical resource for this research order.", result);
    }
}
