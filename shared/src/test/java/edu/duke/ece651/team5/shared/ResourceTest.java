package edu.duke.ece651.team5.shared;

import edu.duke.ece651.team5.shared.game.Territory;
import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;
import edu.duke.ece651.team5.shared.unit.SoldierLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceTest {

    @Test
    public void testEquals() {
        // Create two Resources with the same type
        ResourceType type = ResourceType.FOOD;
        Resource resource1 = new Resource(type);
        Resource resource2 = new Resource(type);

        // Verify that the equals() method returns true for the two Resources
        assertTrue(resource1.equals(resource2));
        assertTrue(resource1.equals(resource1));
    }

    @Test
    public void testNotEquals() {
        // Create two Resources with different types
        Resource resource1 = new Resource(ResourceType.FOOD);
        Resource resource2 = new Resource(ResourceType.TECHNOLOGY);

        // Verify that the equals() method returns false for the two Resources
        assertFalse(resource1.equals(resource2));
        assertFalse(resource1.equals(null));
        assertFalse(resource1.equals(SoldierLevel.INFANTRY));
    }

}