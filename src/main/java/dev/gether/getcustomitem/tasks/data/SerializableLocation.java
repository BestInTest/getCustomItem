package dev.gether.getcustomitem.tasks.data;

import java.io.Serializable;

public record SerializableLocation(String world, int x, int y, int z) implements Serializable {

}
