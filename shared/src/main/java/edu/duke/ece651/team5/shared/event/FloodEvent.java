package edu.duke.ece651.team5.shared.event;

import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.unit.*;

import java.util.List;
import java.util.Map;

public class FloodEvent extends BasicEvent{

    public FloodEvent(RISKMap map) {
        super(EventType.FLOOD, map);
    }

    public void setSelectedTerritories(List<Territory> territories) {
        this.selectedTerritories = territories;
    }

    @Override
    public void execute(RISKMap map) {
        for(Territory territory: selectedTerritories) {
            SoldierArmy soldierArmy = territory.getSoldierArmy();
            SoldierArmy afterDownGrade = new SoldierArmy();
            for (Map.Entry<Soldier, Integer> entry : soldierArmy.getAllSoldiers().entrySet()) {
                Soldier soldier = entry.getKey();
                int count = entry.getValue();
                if (count == 0) continue;
                SoldierLevel currLevel = soldier.getLevel();
                SoldierLevel targetLevel = (currLevel.ordinal() == 0) ? currLevel : SoldierLevel.values()[(currLevel.ordinal() - 1)];
                //soldierArmy.upgradeSoldier(soldier, count, targetLevel);
                afterDownGrade.addSoldier(new Soldier(targetLevel), count);
                territory.setSoldierArmy(afterDownGrade);
            }
        }

        for(Territory territory: selectedTerritories) {
            SoldierArmy soldierArmy = territory.getSoldierArmy();
            soldierArmy.getAllSoldiers().entrySet().stream().forEach(entry -> {
                int count = entry.getValue();
                Soldier soldier = entry.getKey();
                count = Math.floorDiv(count, 4);
                territory.getSoldierArmy().removeSoldier(soldier, count);
            });
        }

    }
}
