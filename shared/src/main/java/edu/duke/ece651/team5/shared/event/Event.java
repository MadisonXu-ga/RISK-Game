package edu.duke.ece651.team5.shared.event;

import edu.duke.ece651.team5.shared.game.RISKMap;

public interface Event {
    
    /**
     * The actual updates if an order is executed
     *
     * @param map the map
     */
    public void execute(RISKMap map);

}
