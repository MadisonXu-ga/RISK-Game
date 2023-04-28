package edu.duke.ece651.team5.shared.event;

import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.unit.*;

import java.util.List;

public class DroughtEvent extends BasicEvent{

    public DroughtEvent(RISKMap map) {
        super(EventType.DROUGHT, map);
    }

    public void setSelectedTerritories(List<Territory> territories) {
        this.selectedTerritories = territories;
    }

    @Override
    public void execute(RISKMap map) {
        for(Territory territory: selectedTerritories){
            SoldierArmy soldierArmy = territory.getSoldierArmy();
            soldierArmy.getAllSoldiers().entrySet().stream()
                    .forEach(entry -> {
                        Soldier soldier = entry.getKey();
                        int count = Math.floorDiv(entry.getValue(), 3);
                        territory.getSoldierArmy().removeSoldier(soldier, count);
                    });
        }
    }
    
}
