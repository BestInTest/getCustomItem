package dev.gether.getcustomitem.item.customize;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.gether.getconfig.domain.Item;
import dev.gether.getconfig.domain.config.sound.SoundConfig;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.ItemType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@JsonTypeName("hook")
public class HookItem extends CustomItem {
    private double multiply;
    private double divideHeight;

    public HookItem() {

    }

    public HookItem(String key, String categoryName, boolean cooldownCategory, int usage, Item item, ItemType itemType, int cooldown, String permissionBypass, SoundConfig soundConfig, List<String> notifyYourself, List<String> notifyOpponents, double multiply, double divideHeight) {
        super(key, categoryName, cooldownCategory, usage, item, itemType, cooldown, permissionBypass, soundConfig, notifyYourself, notifyOpponents);
        this.multiply = multiply;
        this.divideHeight = divideHeight;
    }


}
