package edu.duke.ece651.team5.shared.rulechecker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.order.ResearchOrder;
import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;

public class ReserachOrderRuleCheckerTest {
    private ResearchOrderRuleChecker ruleChecker;
    private ResearchOrder order;

    @BeforeEach
    public void setUp() {
        ruleChecker = new ResearchLevelBoundRuleChecker(new ResearchEnoughResourceRuleChecker(null)); // No next rule checker, as it is the last in the chain
        order = new ResearchOrder(new Player("Player 1"));
    }

    @Test
    public void testCheckMyRule() {
        Player player = order.getPlayer();
        player.setCurrTechnologyLevel(2);
        player.addResourceFromTerritory(new Resource(ResourceType.TECHNOLOGY), 500); // Set enough resources

        String result = ruleChecker.checkMyRule(order);

        assertNull(result);
    }

    @Test
    public void testCheckMyRuleWithIncorrectInput() {
        Player player = order.getPlayer();
        player.setCurrTechnologyLevel(2);
        player.addResourceFromTerritory(new Resource(ResourceType.TECHNOLOGY), 50); // Set insufficient resources

        String result = ruleChecker.checkOrder(order);

        assertEquals("You do not have enough technical resource for this research order.", result);
    }
}
