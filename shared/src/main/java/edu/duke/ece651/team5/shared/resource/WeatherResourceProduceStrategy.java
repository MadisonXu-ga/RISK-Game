package edu.duke.ece651.team5.shared.resource;

import java.io.Serializable;

public class WeatherResourceProduceStrategy implements ProduceResourceStrategy, Serializable{

    private final ProduceResourceStrategy strategy;
    private final WeatherType weather;

    public WeatherResourceProduceStrategy(ProduceResourceStrategy strategy, WeatherType weather) {
        this.strategy = strategy;
        this.weather = weather;
    }

    @Override
    public int produceResources(Resource resource) {
        int baseProduction = strategy.produceResources(resource);
        double rate = resource.getType().equals(ResourceType.FOOD) ? getFoodRate() : getTechRate();
        return (int) (baseProduction * rate);
    }
    
    private double getFoodRate() {
        switch (weather) {
            case SUNNY:
                return WeatherRateConstants.SUNNY_RATE_ON_FOOD;
            case RAINY:
                return WeatherRateConstants.RAINY_RATE_ON_FOOD;
            case CLOUDY:
                return WeatherRateConstants.CLOUDY_RATE_ON_FOOD;
            case WINDY:
                return WeatherRateConstants.WINDY_RATE_ON_FOOD;
            default:
                return 1.0;
        }
    }

    private double getTechRate() {
        switch (weather) {
            case SUNNY:
                return WeatherRateConstants.SUNNY_RATE_ON_TECH;
            case RAINY:
                return WeatherRateConstants.RAINY_RATE_ON_TECH;
            case CLOUDY:
                return WeatherRateConstants.CLOUDY_RATE_ON_TECH;
            case WINDY:
                return WeatherRateConstants.WINDY_RATE_ON_TECH;
            default:
                return 1.0;
        }
    }


}
