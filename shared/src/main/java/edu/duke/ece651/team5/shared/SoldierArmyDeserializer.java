package edu.duke.ece651.team5.shared;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SoldierArmyDeserializer extends JsonDeserializer<SoldierArmy> {
    @Override
    public SoldierArmy deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        SoldierArmy soldierArmy = new SoldierArmy();

        // read the JSON object as a tree
        JsonNode node = parser.getCodec().readTree(parser);

        // read the "soldiers" array and add each soldier to the soldierArmy object
        JsonNode soldiersNode = node.get("soldiers");
        if (soldiersNode != null) {
            for (JsonNode soldierNode : soldiersNode) {
                // read the properties of each soldier and create a new Soldier object
                JsonNode typeNode = soldierNode.get("type");
                JsonNode levelNode = soldierNode.get("level");
                JsonNode numSoldiersNode = soldierNode.get("numSoldiers");
                if (typeNode != null && levelNode != null && numSoldiersNode != null) {
                    SoldierType type = SoldierType.valueOf(typeNode.asText());
                    int level = levelNode.asInt();
                    int numSoldiers = numSoldiersNode.asInt();
                    Soldier soldier = new Soldier(type, level, numSoldiers);
                    soldierArmy.getSoldiers().add(soldier);
                }
            }
        }

        return soldierArmy;
    }
}
