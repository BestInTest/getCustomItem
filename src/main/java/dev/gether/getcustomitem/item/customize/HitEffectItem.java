package dev.gether.getcustomitem.item.customize;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.gether.getconfig.domain.Item;
import dev.gether.getconfig.domain.config.potion.PotionEffectConfig;
import dev.gether.getconfig.domain.config.sound.SoundConfig;
import dev.gether.getconfig.utils.ColorFixer;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.ItemType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonTypeName("hit_effect")
public class HitEffectItem extends CustomItem {
    private List<PotionEffectConfig> potionEffectConfigs;
    private double chance;
    public HitEffectItem() {}

    public HitEffectItem(String key, String categoryName, boolean cooldownCategory, int usage, Item item, ItemType itemType, int cooldown, String permissionBypass, SoundConfig soundConfig, List<String> notifyYourself, List<String> notifyOpponents, List<PotionEffectConfig> potionEffectConfigs, double chance) {
        super(key, categoryName, cooldownCategory, usage, item, itemType, cooldown, permissionBypass, soundConfig, notifyYourself, notifyOpponents);
        this.potionEffectConfigs = potionEffectConfigs;
        this.chance = chance;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack itemStack = getItem().getItemStack().clone();
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setUnbreakable(true);

            // set usage to persistent data
            itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER, usage);

            List<String> lore = new ArrayList<>();
            if (itemMeta.hasLore())
                lore.addAll(itemMeta.getLore());

            lore.replaceAll(line -> line
                    .replace("{chance}", String.valueOf(chance))
                    .replace("{usage}", String.valueOf(usage))
            );
            itemMeta.setLore(ColorFixer.addColors(lore));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }


}
