package dev.gether.getcustomitem.item.customize;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.gether.getconfig.annotation.Comment;
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
@JsonTypeName("effect_radius")
public class EffectRadiusItem extends CustomItem {

    @Comment("apply the effect to yourself")
    private boolean includingYou;
    @Comment("apply the effect to players within a radius")
    private boolean otherPlayers;
    private int radius;
    private List<PotionEffectConfig> activeEffect;
    private List<String> removeEffect;

    public EffectRadiusItem() {}

    public EffectRadiusItem(String key, String categoryName, boolean cooldownCategory, int usage, Item item, ItemType itemType, int cooldown, String permissionBypass, SoundConfig soundConfig, List<String> notifyYourself, List<String> notifyOpponents, boolean includingYou, boolean otherPlayers, int radius, List<PotionEffectConfig> activeEffect, List<String> removeEffect) {
        super(key, categoryName, cooldownCategory, usage, item, itemType, cooldown, permissionBypass, soundConfig, notifyYourself, notifyOpponents);
        this.includingYou = includingYou;
        this.otherPlayers = otherPlayers;
        this.radius = radius;
        this.activeEffect = activeEffect;
        this.removeEffect = removeEffect;
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
                    .replace("{radius}", String.valueOf(radius))
                    .replace("{usage}", String.valueOf(usage))
            );
            itemMeta.setLore(ColorFixer.addColors(lore));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    // todo: custom particles etc.
}
