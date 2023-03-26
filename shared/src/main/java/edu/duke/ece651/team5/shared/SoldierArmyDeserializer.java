package edu.duke.ece651.team5.shared;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class SoldierArmyDeserializer extends JsonDeserializer<SoldierArmy> {
    @Override
    public SoldierArmy deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        SoldierArmy army = new SoldierArmy();
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            Soldier soldier = null;
            int count = 0;
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();
                if (field.equals("soldier")) {
                    soldier = parser.readValueAs(Soldier.class);
                } else if (field.equals("count")) {
                    count = parser.getIntValue();
                }
            }
            army.addSoldier(soldier, count);
        }
        return army;
    }
}
