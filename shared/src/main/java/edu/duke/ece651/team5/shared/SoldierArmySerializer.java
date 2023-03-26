package edu.duke.ece651.team5.shared;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class SoldierArmySerializer extends JsonSerializer<SoldierArmy> {

    @Override
    public void serialize(SoldierArmy value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();  // start writing the SoldierArmy object to JSON
        gen.writeArrayFieldStart("soldiers");  // start writing the "soldiers" array

        // write each Soldier object to the "soldiers" array
        for (Soldier soldier : value.getSoldiers()) {
            gen.writeStartObject();  // start writing the Soldier object to JSON
            gen.writeStringField("type", soldier.getType().toString());  // write the soldier type as a string
            gen.writeNumberField("level", soldier.getLevel());  // write the soldier level as a number
            gen.writeNumberField("numSoldiers", soldier.getSoldierNum());  // write the number of soldiers as a number
            gen.writeEndObject();  // end writing the Soldier object to JSON
        }
        gen.writeEndArray();  // end writing the "soldiers" array
        gen.writeEndObject();  // end writing the SoldierArmy object to JSON
    }
}