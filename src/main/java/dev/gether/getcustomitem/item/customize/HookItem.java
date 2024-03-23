package dev.gether.getcustomitem.item.customize;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.gether.getconfig.domain.Item;
import dev.gether.getconfig.domain.config.sound.SoundConfig;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.ItemType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


@Getter
@Setter
@JsonTypeName("hook")
public class HookItem extends CustomItem {
    private double multiply;
    private double divideHeight;

    public HookItem() {

    }
    public HookItem(Item item, ItemType itemType, int cooldown, String permissionByPass, SoundConfig soundConfig, double multiply, double divideHeight) {
        super(item, itemType, cooldown, permissionByPass, soundConfig);
        this.multiply = multiply;
        this.divideHeight = divideHeight;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack itemStack = getItem().getItemStack().clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setUnbreakable(true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }


}
