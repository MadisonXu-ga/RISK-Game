package edu.duke.ece651.team5.shared.utils;

import edu.duke.ece651.team5.shared.event.EventType;
import edu.duke.ece651.team5.shared.resource.WeatherType;

public class RandomDice {
    public static EventType rollEventDice() {
        int roll = (int) (Math.random() * 6) + 1;
        switch (roll) {
            case 1:
            case 2:
            case 3:
                return EventType.NORMAL;
            case 4:
                return EventType.STORM;
            case 5:
                return EventType.DROUGHT;
            case 6:
                return EventType.FLOOD;
            default:
                return EventType.NORMAL;
        }
    }

    public static WeatherType rollWeatherDice() {
        int diceResult = (int) (Math.random() * 4) + 1;
        switch (diceResult) {
            case 1:
                return WeatherType.SUNNY;
            case 2:
                return WeatherType.CLOUDY;
            case 3:
                return WeatherType.RAINY;
            case 4:
                return WeatherType.WINDY;
            default:
                return WeatherType.CLOUDY;
        }
    }
}
