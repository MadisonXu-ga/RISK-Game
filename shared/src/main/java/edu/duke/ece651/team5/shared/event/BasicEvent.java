package edu.duke.ece651.team5.shared.event;

import java.util.List;

import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.utils.RandomTerritoriesGenerator;

public abstract class BasicEvent implements Event {
    protected List<Territory> selectedTerritories;

    public List<Territory> getSelectedTerritories() {
        return selectedTerritories;
    }

    public BasicEvent(EventType type, RISKMap map){
        selectedTerritories = RandomTerritoriesGenerator.generate(map);
    }

    

}
