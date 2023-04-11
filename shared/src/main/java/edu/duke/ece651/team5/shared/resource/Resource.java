package edu.duke.ece651.team5.shared.resource;

public class Resource {
    // the type of the resource
    ResourceType type;

    /**
     * Constructor for Resource
     * @param type the type of the resource
     */
    public Resource(ResourceType type) {
        this.type = type;
    }

    /**
     * Getter for the type
     * @return the type of the resource
     */
    public ResourceType getType() {
        return type;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        return type == resource.type;
    }

    @Override
    public int hashCode() {
        // return type != null ? type.hashCode() : 0;
        return type.hashCode();
    }




}
