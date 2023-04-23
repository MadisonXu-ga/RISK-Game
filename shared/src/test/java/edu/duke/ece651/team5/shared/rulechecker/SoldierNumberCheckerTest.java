package edu.duke.ece651.team5.shared.rulechecker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.game.*;
import edu.duke.ece651.team5.shared.order.BasicOrder;
import edu.duke.ece651.team5.shared.order.MoveOrder;
import edu.duke.ece651.team5.shared.unit.*;

public class SoldierNumberCheckerTest {

        private SoldierNumberChecker soldierNumberChecker;
        private RISKMap map;
        private Territory source;
        private Territory target;
    
        @BeforeEach
        void setUp() {
            soldierNumberChecker = new SoldierNumberChecker(null);
            map = new RISKMap();
            source = map.getTerritoryById(1);
            SoldierArmy sourceArmy = new SoldierArmy();
            sourceArmy.addSoldier(new Soldier(SoldierLevel.INFANTRY), 5);
            source.setSoldierArmy(sourceArmy);
            target = map.getTerritoryById(2);
        }
    
        @Test
        void testCheckMyRulewError() {
    


            SoldierArmy targetArmy2 = new SoldierArmy();
            targetArmy2.addSoldier(new Soldier(SoldierLevel.INFANTRY), 10);
            MoveOrder order2 = new MoveOrder(source.getName(), "Territory 4", targetArmy2, new Player("1"));
            String expectedErrorMsg = "There are only 5 units in Elantris, but you entered 10";
            assertEquals(expectedErrorMsg, soldierNumberChecker.checkMyRule(order2, map));
        }

        @Test
        void testCheckMyRule() {


            SoldierArmy targetArmy2 = new SoldierArmy();
            targetArmy2.addSoldier(new Soldier(SoldierLevel.INFANTRY), 4);
            MoveOrder order2 = new MoveOrder(source.getName(), "Territory 4", targetArmy2, new Player("1"));
            assertEquals(null, soldierNumberChecker.checkMyRule(order2, map));
        }
    
}
