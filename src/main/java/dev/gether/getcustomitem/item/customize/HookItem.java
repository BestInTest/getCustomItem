package dev.gether.getcustomitem.item.customize;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.gether.getconfig.domain.Item;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.ItemType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeName("hook")
public class HookItem extends CustomItem {
    private double multiply;
    private double divideHeight;

    public HookItem() {

    }
    public HookItem(Item item, ItemType itemType, int cooldown, String permissionByPass, double multiply, double divideHeight) {
        super(item, itemType, cooldown, permissionByPass);
        this.multiply = multiply;
        this.divideHeight = divideHeight;
    }

}
