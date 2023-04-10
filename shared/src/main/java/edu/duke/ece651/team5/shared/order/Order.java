package edu.duke.ece651.team5.shared.order;

import edu.duke.ece651.team5.shared.game.RISKMap;

public interface Order {
    /**
     * The actual updates if an order is executed
     *
     * @param map the map
     */
    public void execute(RISKMap map);
}
