package edu.duke.ece651.team5.shared.rulechecker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.order.AttackOrder;
import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;

public class AttackResourceCheckerTest {

    @Test
    public void checkEnoughResource(){
        SoldierArmy soldierToNum = mock(SoldierArmy.class);
        when(soldierToNum.getTotalCountSolider()).thenReturn(10);

        Player player = mock(Player.class);
        AttackOrder order = new AttackOrder("A", "B", soldierToNum, player);

        RISKMap map = mock(RISKMap.class);
        when(map.getShortestPathDistance(any(String.class), any(String.class), anyBoolean())).thenReturn(2);
        when(player.getResourceCount(any(Resource.class))).thenReturn(200);

        AttackResourceChecker checker = new AttackResourceChecker(null);
        assertEquals(null, checker.checkOrder(order, map));
    }

    @Test
    public void checkNotEnoughResource(){
        SoldierArmy soldierToNum = mock(SoldierArmy.class);
        when(soldierToNum.getTotalCountSolider()).thenReturn(10);

        Player player = mock(Player.class);
        AttackOrder order = new AttackOrder("A", "B", soldierToNum, player);

        RISKMap map = mock(RISKMap.class);
        when(map.getShortestPathDistance(any(String.class), any(String.class), anyBoolean())).thenReturn(2);
        when(player.getResourceCount(any(Resource.class))).thenReturn(2);

        AttackResourceChecker checker = new AttackResourceChecker(null);
        assertEquals("You do not have enough food resource for this attack order.", checker.checkOrder(order, map));
    }
}
