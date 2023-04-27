package edu.duke.ece651.team5.shared.event;

import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;
import edu.duke.ece651.team5.shared.unit.Soldier;
import edu.duke.ece651.team5.shared.unit.SoldierArmy;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;

public class StormEvent extends BasicEvent{

    public StormEvent(RISKMap map) {
        super(EventType.STORM, map);
    }

    @Override
    public void execute(RISKMap map) {
        for(Territory territory: selectedTerritories){
            SoldierArmy soldierArmy = territory.getSoldierArmy();
            soldierArmy.getAllSoldiers().entrySet().stream()
                    .forEach(entry -> {
                        Soldier soldier = entry.getKey();
                        int count = entry.getValue();
                        SoldierLevel currLevel = soldier.getLevel();
                        SoldierLevel targetLevel = (currLevel.ordinal() == 0) ? currLevel : SoldierLevel.values()[(currLevel.ordinal() - 1)];
                        territory.getSoldierArmy().upgradeSoldier(soldier, count, targetLevel);
                    });
        }
    }
    
}
