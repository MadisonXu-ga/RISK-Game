package edu.duke.ece651.team5.shared;

import edu.duke.ece651.team5.shared.resource.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class WeatherResourceProduceStrategyTest {

    @Test
    void produceResources() {
        DefaultResourceProduceStrategy defaultResourceProduceStrategy = new DefaultResourceProduceStrategy();
        WeatherResourceProduceStrategy cloudy = new WeatherResourceProduceStrategy(defaultResourceProduceStrategy, WeatherType.CLOUDY);
        WeatherResourceProduceStrategy sunny= new WeatherResourceProduceStrategy(defaultResourceProduceStrategy, WeatherType.SUNNY);
        WeatherResourceProduceStrategy windy = new WeatherResourceProduceStrategy(defaultResourceProduceStrategy, WeatherType.WINDY);
        WeatherResourceProduceStrategy rainy = new WeatherResourceProduceStrategy(defaultResourceProduceStrategy, WeatherType.RAINY);
        assertEquals(5, cloudy.produceResources(new Resource(ResourceType.TECHNOLOGY)));
        assertEquals(5, cloudy.produceResources(new Resource(ResourceType.FOOD)));
        assertEquals(6, sunny.produceResources(new Resource(ResourceType.TECHNOLOGY)));
        assertEquals(6, sunny.produceResources(new Resource(ResourceType.FOOD)));
        assertEquals(5, windy.produceResources(new Resource(ResourceType.TECHNOLOGY)));
        assertEquals(4, windy.produceResources(new Resource(ResourceType.FOOD)));
        assertEquals(4, rainy.produceResources(new Resource(ResourceType.TECHNOLOGY)));
        assertEquals(5, rainy.produceResources(new Resource(ResourceType.FOOD)));
    }

    // this cannot be tested since enum is final
    // @Test
    // void testNoWeather() {
    //     DefaultResourceProduceStrategy defaultResourceProduceStrategy = new DefaultResourceProduceStrategy();
    //     WeatherType other = Mockito.mock(WeatherType.class);
    //     when(other.ordinal()).thenReturn(7);
    //     WeatherResourceProduceStrategy otherWeather= new WeatherResourceProduceStrategy(defaultResourceProduceStrategy, other);
    //     assertEquals(5, otherWeather.produceResources(new Resource(ResourceType.TECHNOLOGY)));
    //     assertEquals(5, otherWeather.produceResources(new Resource(ResourceType.FOOD)));
    //
    // }
}