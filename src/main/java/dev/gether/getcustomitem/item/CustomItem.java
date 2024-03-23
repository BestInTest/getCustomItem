package dev.gether.getcustomitem.item;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.gether.getconfig.domain.Item;
import dev.gether.getcustomitem.item.customize.CrossBowItem;
import dev.gether.getcustomitem.item.customize.HookItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HookItem.class, name = "hook"),
        @JsonSubTypes.Type(value = CrossBowItem.class, name = "crossbow"),
})
public class CustomItem {
    private boolean enabled = true;
    private Item item;
    private ItemType itemType;
    private int cooldown; // time in second
    private String permissionBypass;

    public CustomItem() {}

    public CustomItem(Item item, ItemType itemType, int cooldown, String permissionBypass) {
        this.item = item;
        this.itemType = itemType;
        this.cooldown = cooldown;
        this.permissionBypass = permissionBypass;
    }

}
