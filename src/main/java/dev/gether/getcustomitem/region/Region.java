package dev.gether.getcustomitem.region;

import dev.gether.getconfig.domain.Cuboid;
import dev.gether.getcustomitem.item.ItemType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Region {
    private Cuboid cuboid;
    private ItemType itemType;

}
