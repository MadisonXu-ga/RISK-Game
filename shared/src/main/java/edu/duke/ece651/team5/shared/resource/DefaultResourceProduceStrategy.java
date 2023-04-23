package edu.duke.ece651.team5.shared.resource;


import java.io.Serializable;

import edu.duke.ece651.team5.shared.constant.Constants;
public class DefaultResourceProduceStrategy implements ProduceResourceStrategy, Serializable{

    @Override
    public int produceResources(Resource resource) {
        return resource.getType().equals(ResourceType.FOOD) ? Constants.PRODUCE_FOOD_RESOURCE_PER_TURN : Constants.PRODUCE_FOOD_RESOURCE_PER_TURN;
    }
    
}
