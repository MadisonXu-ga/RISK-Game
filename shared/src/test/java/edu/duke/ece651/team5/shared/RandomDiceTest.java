package edu.duke.ece651.team5.shared;

import edu.duke.ece651.team5.shared.event.EventType;
import edu.duke.ece651.team5.shared.resource.WeatherType;
import edu.duke.ece651.team5.shared.utils.RandomDice;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class RandomDiceTest {

    @Test
    public void testRollEventDice() {
        for (int i = 0; i < 100; i++) {
            EventType eventType = RandomDice.rollEventDice();
            assertNotNull(eventType);
        }
    }

    @Test
    public void testRollWeatherDice() {
        for (int i = 0; i < 100; i++) {
            WeatherType weatherType = RandomDice.rollWeatherDice();
            assertNotNull(weatherType);
        }
    }
}