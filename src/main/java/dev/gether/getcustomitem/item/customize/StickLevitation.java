package dev.gether.getcustomitem.item.customize;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.gether.getconfig.domain.Item;
import dev.gether.getconfig.domain.config.sound.SoundConfig;
import dev.gether.getconfig.utils.ColorFixer;
import dev.gether.getconfig.utils.ConsoleColor;
import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.ItemType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonTypeName("stick_levitation")
public class StickLevitation extends CustomItem {

    private String potionName;
    private int second;
    private int level;
    private int radius;

    public StickLevitation() {}

    public StickLevitation(Item item, ItemType itemType, int cooldown, String permissionBypass, SoundConfig soundConfig, String potionName, int second, int level, int radius) {
        super(item, itemType, cooldown, permissionBypass, soundConfig);
        this.potionName = potionName;
        this.second = second;
        this.level = level;
        this.radius = radius;
    }

    @JsonIgnore
    public PotionEffect getPotionEffect() {
        PotionEffectType potionEffectType = PotionEffectType.getByName(potionName);
        if(potionEffectType == null) {
            MessageUtil.logMessage(ConsoleColor.RED, "The potion effect name '" + potionName + "' does not exist!");
        }
        return new PotionEffect(potionEffectType, second * 20, level - 1);
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
                    .replace("{radius}", String.valueOf(radius))
            );
            itemMeta.setLore(ColorFixer.addColors(lore));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    // todo: custom particles etc.
}
