package edu.duke.ece651.team5.shared;


import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.resource.Resource;
import edu.duke.ece651.team5.shared.resource.ResourceType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;

public class ResourceTest {
    private ResourceType type1;
    private ResourceType type2;

    @BeforeEach
    public void setUp() {
        type1 = ResourceType.FOOD;
        type2 = ResourceType.TECHNOLOGY;
    }

    @Test
    public void testEqualsAndHashCode() {
        // Test equals() method
        Resource resource1 = new Resource(type1);
        Resource resource2 = new Resource(type1);
        Resource resource3 = new Resource(type2);

        assertEquals(resource1, resource1); // Same object should be equal to itself
        assertEquals(resource1, resource2); // Same type should be equal
        assertNotEquals(resource1, resource3); // Different type should not be equal
        assertNotEquals(resource1, null); // null should not be equal

        // Test hashCode() method
        assertEquals(resource1.hashCode(), resource2.hashCode()); // Equal objects should have same hashCode
    }
}
