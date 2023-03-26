package edu.duke.ece651.team5.shared;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;

public class SoldierArmySerializer extends JsonSerializer<SoldierArmy> {
    @Override
    public void serialize(SoldierArmy army, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        for (Map.Entry<Soldier, Integer> entry : army.getAllSoldiers().entrySet()) {
            gen.writeStartObject();
            gen.writeObjectField("soldier", entry.getKey());
            gen.writeNumberField("count", entry.getValue());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}

