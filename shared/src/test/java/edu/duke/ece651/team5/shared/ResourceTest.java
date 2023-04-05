package edu.duke.ece651.team5.shared;


import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.allResource.Resource;
import edu.duke.ece651.team5.shared.allResource.ResourceType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ResourceTest {
    Resource resource = new Resource(ResourceType.FOOD);

    @Test
    void testEquals() {
        assertEquals(resource, new Resource(ResourceType.FOOD));
        assertNotEquals(resource, new Resource(ResourceType.TECHNOLOGY));
    }

    @Test
    void testHashcode() {
        assertEquals(resource.hashCode(), new Resource(ResourceType.FOOD).hashCode());
        assertNotEquals(resource.hashCode(), new Resource(ResourceType.TECHNOLOGY).hashCode());
    }
}
