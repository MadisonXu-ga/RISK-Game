package edu.duke.ece651.team5.shared.allResource;

public class Resource {
    ResourceType type;

    public Resource(ResourceType type) {
        this.type = type;
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
