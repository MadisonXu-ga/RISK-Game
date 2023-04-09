package edu.duke.ece651.team5.shared.rulechecker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.order.ResearchOrder;
import edu.duke.ece651.team5.shared.constant.*;

public class ResearchLevelBoundRuleCheckerTest {
    private ResearchLevelBoundRuleChecker ruleChecker;
    private Player player;

    @BeforeEach
    public void setUp() {
        ruleChecker = new ResearchLevelBoundRuleChecker(null); // No next rule checker, as it is the last in the chain
        player = new Player("Player 1");
    }

    @Test
    public void testCheckMyRuleWithLowerTechnologyLevel() {
        ResearchOrder order = new ResearchOrder(player);
        String result = ruleChecker.checkMyRule(order);

        assertNull(result);
    }

    @Test
    public void testCheckMyRuleWithMaxTechnologyLevel() {
        ResearchOrder order = new ResearchOrder(player);
        player.setCurrTechnologyLevel(Constants.GAME_MAX_TECHNOLOGY_LEVEL);
        String result = ruleChecker.checkMyRule(order);
        assertEquals("Cannot upgrade technology level anymore", result);
    }
}
