package dev.gether.getcustomitem.item.customize;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.gether.getconfig.domain.Item;
import dev.gether.getconfig.domain.config.sound.SoundConfig;
import dev.gether.getconfig.utils.ColorFixer;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.ItemType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonTypeName("frozen_sword")
public class FrozenSword extends CustomItem {
    private int frozenSeconds;
    private double chanceToFrozen;

    public FrozenSword() {}

    public FrozenSword(Item item, ItemType itemType, int cooldown, String permissionBypass, SoundConfig soundConfig, int frozenSeconds, double chanceToFrozen) {
        super(item, itemType, cooldown, permissionBypass, soundConfig);
        this.frozenSeconds = frozenSeconds;
        this.chanceToFrozen = chanceToFrozen;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack itemStack = getItem().getItemStack().clone();
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            List<String> lore = new ArrayList<>();
            if (itemMeta.hasLore())
                lore.addAll(itemMeta.getLore());

            lore.replaceAll(line -> line
                    .replace("{chance}", String.valueOf(chanceToFrozen))
                    .replace("{seconds}", String.valueOf(frozenSeconds))
            );
            itemMeta.setLore(ColorFixer.addColors(lore));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
