package edu.duke.ece651.team5.shared.order;

import edu.duke.ece651.team5.shared.allResource.*;
import edu.duke.ece651.team5.shared.game.*;

import java.util.ArrayList;
import java.util.Arrays;

public class ResearchOrder implements Order {
    public final ArrayList<Integer> researchConsumeCost =
            new ArrayList<>(Arrays.asList(20, 40, 80, 160, 320));
    Player player;

    public ResearchOrder(Player player) {
        this.player = player;
    }

    public Player getPlayer(){
        return player;
    }

    @Override
    public void execute(RISKMap map) {
        int currTechnologyLevel = this.player.getCurrTechnologyLevel();
        this.player.consumeResource(new Resource(ResourceType.TECHNOLOGY),
                researchConsumeCost.get(currTechnologyLevel));
        this.player.upgradeTechnologyLevel();
    }
}
