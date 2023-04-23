package edu.duke.ece651.team5.shared.event;

import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.unit.*;

public class FloodEvent extends BasicEvent{

    public FloodEvent(RISKMap map) {
        super(EventType.FLOOD, map);
    }

    @Override
    public void execute(RISKMap map) {
        for(Territory territory: selectedTerritories){
            SoldierArmy soldierArmy = territory.getSoldierArmy();
            soldierArmy.getAllSoldiers().entrySet().stream()
                    .forEach(entry -> {
                        int count = entry.getValue();
                        Soldier soldier = entry.getKey();
                        SoldierLevel currLevel = soldier.getLevel();
                        SoldierLevel targetLevel = (currLevel.ordinal() == 1) ? currLevel : SoldierLevel.values()[(currLevel.ordinal() - 1)];
                        territory.getSoldierArmy().upgradeSoldier(soldier, count, targetLevel);

                        count = Math.floorDiv(entry.getValue(), 4);
                        territory.getSoldierArmy().removeSoldier(soldier, count);
                    });
        }
    }
}
