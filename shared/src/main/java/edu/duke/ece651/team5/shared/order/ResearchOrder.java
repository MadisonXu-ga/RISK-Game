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
    public static final ArrayList<Integer> researchConsumeCost =
            new ArrayList<>(Arrays.asList(20, 40, 80, 160, 320));

    // the player who issued this order
    private Player player;
    private Game game;

    /**
     * constructor
     * @param player the player who issued this order
     * @param game the game
     */
    public ResearchOrder(Player player, Game game) {
        this.game = game;
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
        this.game.getPlayer(player).upgradeTechnologyLevel();
    }

    public void execute(RISKMap map, Player targetPlayer) {
        int currTechnologyLevel = targetPlayer.getCurrTechnologyLevel();
        targetPlayer.consumeResource(new Resource(ResourceType.TECHNOLOGY),
                researchConsumeCost.get(currTechnologyLevel));
        targetPlayer.upgradeTechnologyLevel();
    }
}
