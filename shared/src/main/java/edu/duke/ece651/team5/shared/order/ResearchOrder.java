package edu.duke.ece651.team5.shared.order;

import edu.duke.ece651.team5.shared.game.RISKMap;
import edu.duke.ece651.team5.shared.resource.*;
import edu.duke.ece651.team5.shared.game.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * For a certain player,
 * using research order to upgrade its current technical level
 */
public class ResearchOrder implements Order {
    public final ArrayList<Integer> researchConsumeCost =
            new ArrayList<>(Arrays.asList(20, 40, 80, 160, 320));

    // the player who issued this order
    Player player;

    /**
     * constructor
     * @param player the player who issued this order
     */
    public ResearchOrder(Player player) {
        this.player = player;
    }

    /**
     * Getter for player
     * @return the player who issued this order
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * the actual excecute the research order
     * @param map the map
     */
    @Override
    public void execute(RISKMap map) {
        int currTechnologyLevel = this.player.getCurrTechnologyLevel();
        this.player.consumeResource(new Resource(ResourceType.TECHNOLOGY),
                researchConsumeCost.get(currTechnologyLevel));
        this.player.upgradeTechnologyLevel();
    }
}
