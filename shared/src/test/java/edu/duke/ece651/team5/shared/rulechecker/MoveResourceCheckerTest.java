package edu.duke.ece651.team5.shared.rulechecker;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.game.Player;
import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.order.MoveOrder;
import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;

public class MoveResourceCheckerTest {

    @Test
    public void testEnoughResource(){
        SoldierArmy soldierToNum = mock(SoldierArmy.class);
        when(soldierToNum.getTotalCountSolider()).thenReturn(10);

        Player player = mock(Player.class);
        MoveOrder order = new MoveOrder("A", "B", soldierToNum, player);

        RISKMap map = mock(RISKMap.class);
        when(map.getShortestPathDistance(any(String.class), any(String.class), anyBoolean())).thenReturn(2);
        when(player.getResourceCount(any(Resource.class))).thenReturn(200);

        MoveResourceChecker checker = new MoveResourceChecker(null);
        assertEquals(null, checker.checkOrder(order, map));

    }

    @Test
    public void testNotEnoughResource(){
        SoldierArmy soldierToNum = mock(SoldierArmy.class);
        when(soldierToNum.getTotalCountSolider()).thenReturn(10);

        Player player = mock(Player.class);
        MoveOrder order = new MoveOrder("A", "B", soldierToNum, player);

        RISKMap map = mock(RISKMap.class);
        when(map.getShortestPathDistance(any(String.class), any(String.class), anyBoolean())).thenReturn(2);
        when(player.getResourceCount(any(Resource.class))).thenReturn(3);

        MoveResourceChecker checker = new MoveResourceChecker(null);
        assertEquals("You do not have enough food resource for this move order.", checker.checkOrder(order, map));

    }

}
