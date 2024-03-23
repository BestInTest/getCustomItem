package dev.gether.getcustomitem.item.customize;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.gether.getconfig.domain.Item;
import dev.gether.getconfig.utils.ColorFixer;
import dev.gether.getcustomitem.config.addon.ParticleConfig;
import dev.gether.getcustomitem.config.addon.SoundConfig;
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
@JsonTypeName("crossbow")
public class CrossBowItem extends CustomItem {
    private SoundConfig soundConfig;
    private ParticleConfig particleConfig;
    private String permissionIgnoreEffect = "getcustomitem.crossbow.effect.bypass";
    private double chance = 30;
    public CrossBowItem() {}

    public CrossBowItem(Item item, ItemType itemType, int cooldown, String permissionBypass, SoundConfig soundConfig, ParticleConfig particleConfig) {
        super(item, itemType, cooldown, permissionBypass);
        this.soundConfig = soundConfig;
        this.particleConfig = particleConfig;
    }


    @Override
    public Item getItem() {
        ItemStack itemStack = super.getItem().getItemStack().clone();
        ItemMeta itemMeta = itemStack.getItemMeta();

        if(itemMeta != null) {
            List<String> lore = new ArrayList<>();
            if (itemMeta.hasLore())
                lore.addAll(itemMeta.getLore());

            lore.replaceAll(line -> line.replace("{chance}", String.valueOf(chance)));
            itemMeta.setLore(ColorFixer.addColors(lore));
        }
        itemStack.setItemMeta(itemMeta);
        return Item.builder()
                .itemStack(itemStack)
                .build();
    }
}
