package dev.gether.getcustomitem.tasks.data;

import java.io.Serializable;

public record CobwebLocationRecord(SerializableLocation location, int radiusX, int radiusY, long creationTimestamp) implements Serializable {

}
